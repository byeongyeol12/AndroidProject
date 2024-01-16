package com.example.webview;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
    private SharedPreferences sharedPreferences;

    public MyCookieJar(Context context){
        sharedPreferences = context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(Cookie cookie: cookies){
            editor.putString(url.host(), cookie.toString());
        }

        editor.apply();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url){
        List<Cookie> cookies = new ArrayList<>();

        String savedCookieString = sharedPreferences.getString(url.host(),null);

        if (savedCookieString != null){
            for(String individualCookieString : savedCookieString.split(";")){
                Cookie parsedCookie = Cookie.parse(url, individualCookieString);
                if (parsedCookie != null){
                    cookies.add(parsedCookie);

                    Log.d("MyCookieJar", "Loading cookie: " + url.host() + " = " + individualCookieString);
                }
            }
        }
        return cookies;
    }
}