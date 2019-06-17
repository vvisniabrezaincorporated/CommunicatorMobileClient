package pl.wnb.communicator.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieStoreUtil implements CookieJar {

    private final List<Cookie> cookieStore = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies.size() != 0 && cookieStore.size() > 0) {
            cookieStore.clear();
        }

        cookieStore.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        for (Cookie cookie : cookieStore) {
            Log.e("cookie", cookie.toString());
        }
        return cookieStore != null ? cookieStore : new ArrayList<Cookie>();
    }
}