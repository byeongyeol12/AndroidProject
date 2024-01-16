package com.example.webview;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpInterface {
    private WebView mWebView;
    private Activity mContext;

    public OkHttpInterface(Activity activity, WebView view) {
        mWebView = view;
        mContext = activity;
    }

    @JavascriptInterface
    public void postLogin(String username, String password) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/login.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
           @Override
           public void onFailure(Call call, IOException e){
               e.printStackTrace();
           }

           @Override
            public void onResponse(Call call, Response response) throws IOException {
               if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

               String responseData = response.body().string();
               Log.d("response", "Responselogin: " + responseData);
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl("file:///android_asset/mypage.html");
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

           }
        });
    }

    @JavascriptInterface
    public void updatePassword(String password, String new_password){
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("password",password)
                .add("new_password",new_password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/update_password.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/change_password.html");
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }


    @JavascriptInterface
    public void updateEmail(String new_email, String password){
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("new_email",new_email)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/update_email.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/change_email.html");
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @JavascriptInterface
    public void updateNickname(String nickname, String password){
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("new_nickname",nickname)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/update_nickname.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/change_nickname.html");
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @JavascriptInterface
    public void findIdAndNickname() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/id_nickname.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    if (status.equals("success")) {
                        String username = json.getString("username");
                        String nickname = json.getString("nickname");

                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // WebView로 사용자 정보 전달하기
                                mWebView.evaluateJavascript(
                                        "document.getElementById('username').innerText='" + username + "';" +
                                                "document.getElementById('nickname').innerText='" + nickname + "';",
                                        null);
                            }
                        });
                    } else if (status.equals("fail")) {
                        mContext.runOnUiThread(new Runnable() {
                            String message = json.getString("message");
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void checkLogged() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/check_login.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    if (status.equals("success")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });
                    } else if (status.equals("fail")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl("file:///android_asset/mypage.html");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void logOut() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/logout.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    if (status.equals("success")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage.html");
                            }
                        });

                    } else if (status.equals("fail")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage2.html");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void deleteAccount(String password){
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/delete_account.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/mypage.html");
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                                mWebView.loadUrl("file:///android_asset/unregister.html");
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @JavascriptInterface
    public void increaseViewCount(String cpName) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/dbaccess/increase_viewcount.php?cpName=" + cpName)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // JSON 파싱
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        Log.i("OkHttp", "조회수 증가 성공");
                    } else if ("fail".equals(status)) {
                        Log.i("OkHttp", "조회수 증가 실패: " + jsonObject.getString("message"));
                    } else {
                        Log.i("OkHttp", "로그인 상태 아님");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //리뷰 관련
    @JavascriptInterface
    public void insertReview(String content ,String cpName, String currentTime) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/dbaccess/insert_review.php?content="+ content+ "&cpName=" + cpName+"&currentTime="+currentTime)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // JSON 파싱
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        Log.i("OkHttp", "리뷰 입력 성공");
                    } else if ("fail".equals(status)) {
                        Log.i("OkHttp", "리뷰 입력 실패: " + jsonObject.getString("message"));
                    } else {
                        Log.i("OkHttp", "로그인 상태 아님");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void checkLoggedInReview() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/check_login.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    if (status.equals("success")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("javascript:openReviewWindow()",null);
                            }
                        });
                    } else if (status.equals("fail")) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('로그인이 필요합니다.');",null);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void showReviewsOfCpName(String cpName) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/dbaccess/show_reviews_of_cpName.php?cpName=" + cpName)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // JSON 파싱
                String json = response.body().string();
                try {
                    final JSONArray jsonArray = new JSONArray(json);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = null;
                                try{
                                    jsonObject = jsonArray.getJSONObject(i);

                                    String column1 = jsonObject.getString("리뷰내용");
                                    String column2 = jsonObject.getString("닉네임");
                                    String column3 = jsonObject.getString("작성일");

                                    String jsCodeToReview = "javascript:showReview('"+column1+"', '"+column2+"', '"+column3+"')";
                                    mWebView.evaluateJavascript(jsCodeToReview,null);
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void showReviewsOfUserName() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/dbaccess/show_reviews_of_userName.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // JSON 파싱
                String json = response.body().string();
                try {
                    final JSONArray jsonArray = new JSONArray(json);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = null;
                                try{
                                    jsonObject = jsonArray.getJSONObject(i);

                                    String column1 = jsonObject.getString("리뷰내용");
                                    String column2 = jsonObject.getString("닉네임");
                                    String column3 = jsonObject.getString("작성일");
                                    String column4 = jsonObject.getString("문화재명");
                                    String column5 = jsonObject.getString("대표이미지");

                                    String jsCodeToReview = "javascript:showReview('"+column1+"', '"+column2+"', '"+column3+"', '"+column4+"', '"+column5+"')";
                                    mWebView.evaluateJavascript(jsCodeToReview,null);
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void updateReview(String writeTime ,String content) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/update_review.php?content="+ content+ "&write_time=" + writeTime)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void deleteReview(String writeTime) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/mypage/delete_review.php?write_time=" + writeTime)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                try{
                    JSONObject json = new JSONObject(responseData);
                    String status = json.getString("status");
                    String message = json.getString("message");

                    if (status.equals("success")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                            }
                        });

                    } else if (status.equals("fail")){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("alert('"+ message +"');",null);
                            }
                        });

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }





    //번역 관련 이스케이프 정리
    public String escapeJavaScriptString(String s) {
        s = s.replace("\\", "\\\\")
                .replace("\'", "\\\'")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n ", "\\n ")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
        return s;
    }

    //번역
    @JavascriptInterface
    public void translateUsingPapago(String content) {
        Locale currentLocale = Locale.getDefault();
        String language = currentLocale.getLanguage();


        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar(mContext))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.12.6/etc/translate_lang.php?content="+ content+ "&language="+ language)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                Log.d("translate", "translate_content: " + responseData);
                try {
                    JSONObject json = new JSONObject(responseData);
                    String translatedText = json.getJSONObject("message").getJSONObject("result").getString("translatedText");
                    Log.d("translateResult","content: "+translatedText);
                    String escapedTranslatedText = escapeJavaScriptString(translatedText);

                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("javascript:translateResult('"+escapedTranslatedText+"')",null);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}




