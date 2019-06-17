package pl.wnb.communicator.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.wnb.communicator.R;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClientUtil {

    private static Retrofit retrofit = null;

    private static OkHttpClient createClient(Context context) {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = context.getResources().openRawResource(R.raw.cer);

            Certificate ca = cf.generateCertificate(cert);
            cert.close();

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .cookieJar(new CookieStoreUtil())
                    .hostnameVerifier((String hostname, SSLSession session) -> true)
                    .addInterceptor(logging)
                    .build();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Retrofit getClient() {
        synchronized (APIClientUtil.class) {
            if (retrofit == null) {
                OkHttpClient okHttpClient = createClient(ContextUtil.getAppContext());
                if (okHttpClient != null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl("https://83.0.171.108:8443")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(okHttpClient)
                            .build();
                }
            }
        }
        return retrofit;
    }
}
