package pl.wnb.communicator.presenter;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.wnb.communicator.model.KeyDetails;
import pl.wnb.communicator.model.Response;
import pl.wnb.communicator.model.SqlUser;
import pl.wnb.communicator.model.service.UsersService;
import pl.wnb.communicator.model.sqlite.UserManager;
import pl.wnb.communicator.model.util.APIClientUtil;
import pl.wnb.communicator.model.util.AesUtil;
import pl.wnb.communicator.model.util.EncryptionUtil;
import pl.wnb.communicator.model.util.GlobalUserUtil;

public class EncryptionPresenter {

    private View view;
    private static final SecureRandom SECURE_RANDOM;
    private EncryptionUtil encryptionUtil;
    private UserManager userManager;

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize a strong secure random instance", e);
        }
    }

    public EncryptionPresenter(View view) {
        this.view = view;
        userManager = new UserManager();
    }

    @SuppressLint("CheckResult")
    public void generateKey(String name, String email, String password) {
        encryptionUtil = new EncryptionUtil(SECURE_RANDOM);

        Single.fromCallable(() -> encryptionUtil.generateKeys(2048, name, email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((EncryptionUtil.ArmoredKeyPair armoredKeyPairOne) -> {
                    userManager.addUser(name, email, password, armoredKeyPairOne.publicKey(), armoredKeyPairOne.privateKey());
                    setEmail();
                    setPublicKey();
                    this.view.showNotify("Key created.");
                });
    }

    public void importKeys(String name, String email, String password, String keyPassword) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/");

        String zippedFile = Environment.getExternalStorageDirectory().getPath() + "/Download/encryptedKeys.zip";
        byte[] buffer = new byte[1024];
        String publicKeyEncrypted;
        String privateKeyEncrypted;
        String iv;
        String salt;

        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zippedFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(dir, zipEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();

            publicKeyEncrypted = readFile(dir, "publicKey.pgp");
            privateKeyEncrypted = readFile(dir, "privateKey.pgp");
            iv = readFile(dir, "iv.txt");
            salt = readFile(dir, "salt.txt");
        } catch (Exception e) {
            e.printStackTrace();
            this.view.showNotify("Failed to import keys, make sure zip file is provided in proper path and app has proper permissions.");
            return;
        }

        AesUtil util = new AesUtil(128, 10000);
        String publicKey;
        String privateKey;
        try {
            publicKey = util.decrypt(salt, iv, keyPassword, publicKeyEncrypted);
            privateKey = util.decrypt(salt, iv, keyPassword, privateKeyEncrypted);
        } catch (Exception e) {
            e.printStackTrace();
            this.view.showNotify("Failed to import keys, make sure AES password is correct.");
            return;
        }

        userManager.addUser(name, email, password, publicKey, privateKey);
        setEmail();
        setPublicKey();
        this.view.showNotify("Key imported.");

    }

    public void exportKeys(String password) {
        SqlUser sqlUser = userManager.getUser();
        AesUtil aesUtil = new AesUtil(128, 10000);

        String encodedSalt = AesUtil.random(128 / 8);
        String encodedIv = AesUtil.random(128 / 8);

        String encryptPrivateKey;
        String encryptPublicKey;

        try {
            encryptPrivateKey = aesUtil.encrypt(encodedSalt, encodedIv, password, sqlUser.getPrivateKey());
            encryptPublicKey = aesUtil.encrypt(encodedSalt, encodedIv, password, sqlUser.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            this.view.showNotify("Failed key encryption.");
            return;
        }

        createFile("salt.txt", encodedSalt);
        createFile("iv.txt", encodedIv);
        createFile("privateKey.pgp", encryptPrivateKey);
        createFile("publicKey.pgp", encryptPublicKey);
        String[] fileList = {"salt.txt", "iv.txt", "privateKey.pgp", "publicKey.pgp"};

        BufferedInputStream bufferedInputStream;
        String zippedFile = Environment.getExternalStorageDirectory().getPath() + "/Download/encryptedKeys.zip";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zippedFile);
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            byte[] buffer = new byte[2048];
            for (int i = 0; i < fileList.length; i++) {
                FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileList[i]);
                bufferedInputStream = new BufferedInputStream(fileInputStream, 2048);
                ZipEntry zipEntry = new ZipEntry(fileList[i].substring(fileList[i].lastIndexOf("/") + 1));
                zipOutputStream.putNextEntry(zipEntry);
                int count;
                while ((count = bufferedInputStream.read(buffer, 0, 2048)) != -1) {
                    zipOutputStream.write(buffer, 0, count);
                }
                bufferedInputStream.close();
            }
            zipOutputStream.finish();
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            this.view.showNotify("Failed to export keys to zip, make sure app has proper permissions.");
            return;
        }

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/");
        new File(dir, "salt.txt").delete();
        new File(dir, "iv.txt").delete();
        new File(dir, "privateKey.pgp").delete();
        new File(dir, "publicKey.pgp").delete();

        this.view.showNotify("Keys exported.");
    }

    private void setPublicKey() {
        UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
        SqlUser user = userManager.getUser();
        KeyDetails keyDetails = new KeyDetails(user.getEmail(), user.getPublicKey().getBytes(StandardCharsets.UTF_8));
        Log.e("keyDetails", keyDetails.toString());
        Observable<Response> postPublicKeyObservable = apiService.postPublicKey(
                GlobalUserUtil.getInstance().getName(),
                user.getPublicKey().getBytes(StandardCharsets.UTF_8));

        postPublicKeyObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Call", "onSubscribe - postPublicKey");
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.e("Call", "onNext - postPublicKey ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Call", "onError - postPublicKey " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Call", "onComplete - postPublicKey");
                    }
                });
    }

    private void setEmail() {
        UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
        Observable<Response> postPublicKeyObservable = apiService.postEmail(
                GlobalUserUtil.getInstance().getName(),
                userManager.getUser().getEmail());

        postPublicKeyObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Call", "onSubscribe - postEmail");
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.e("Call", "onNext - postEmail ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Call", "onError - postEmail " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Call", "onComplete - postEmail");
                    }
                });
    }

    private void createFile(String fileName, String fileContent) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/");
        dir.mkdir();
        File file = new File(dir, fileName);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(File dir, String file) throws IOException {
        File filePath = new File(dir, file);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        bufferedReader.close();
        filePath.delete();
        return stringBuilder.toString().replaceAll("(\\r|\\n)", "");
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public interface View {
        void showNotify(String info);
    }
}
