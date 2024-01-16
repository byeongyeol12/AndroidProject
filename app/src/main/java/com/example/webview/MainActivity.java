package com.example.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    WebView webview;
    WebViewInterface mWebViewInterface;
    OkHttpInterface mOkHttpInterface;


    // CountDownLatch 초기화
    private final CountDownLatch latch = new CountDownLatch(1);

    //경로추천용 db 배열
    ArrayList<Double> latitudeList = new ArrayList<>();
    ArrayList<Double> longitudeList = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();
    ArrayList<Integer> freeList = new ArrayList<>();
    ArrayList<Integer> viewList = new ArrayList<>();
    ArrayList<String> addrList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();

    //경로추천용 관람시간 db 배열
    ArrayList<String> t_labelList=new ArrayList<>();
    ArrayList<String> t_openList=new ArrayList<>();
    ArrayList<String> t_closeList=new ArrayList<>();


    //검색용 db 배열
    ArrayList<String> slabelList=new ArrayList<>();
    ArrayList<String> saddrList=new ArrayList<>();
    ArrayList<String> simageList=new ArrayList<>();

    //설명용 db 배열
    ArrayList<String> ilabelList=new ArrayList<>();
    ArrayList<String> iaddrList=new ArrayList<>();
    ArrayList<String> iimageList=new ArrayList<>();
    ArrayList<String> infoList=new ArrayList<>();
    ArrayList<String> tourinfoList=new ArrayList<>();

    //탐색용 db 배열
    ArrayList<Double> flatList = new ArrayList<>();
    ArrayList<Double> flngList = new ArrayList<>();
    ArrayList<String> flabelList = new ArrayList<>();
    ArrayList<String> fimageList = new ArrayList<>();

    //권한설정
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    //현재위치
    double latitude=0.0;
    double longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(REQUIRED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, locationListener);

        fetchData();

        webview = findViewById(R.id.mainWebview);

        // 웹뷰 설정 추가
        webview.getSettings().setJavaScriptEnabled(true);



        //webview 인터페이스 초기화
        mWebViewInterface = new WebViewInterface(MainActivity.this, webview);
        webview.addJavascriptInterface(mWebViewInterface, "android");

        mOkHttpInterface = new OkHttpInterface(MainActivity.this, webview);
        webview.addJavascriptInterface(mOkHttpInterface, "okhttp");


        String cameraResult = getIntent().getStringExtra("camera_result");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (cameraResult != null) {
                    Log.d("notice2","null이 아닐시 evaluate");
                    Log.d("cameraresult2", cameraResult);
                    String jsCodeCamera="javascript:updateCameraResult('"+cameraResult+"')";
                    webview.evaluateJavascript(jsCodeCamera,null);
                }



                dataGowebview();  // 페이지 로드가 끝날 때마다 dataGowebView() 호출

                String javascript = "javascript:updateMapLocation(" + latitude + "," + longitude + ")";
                webview.evaluateJavascript(javascript, null);
                String jsCode1 = "javascript:nowLocation(" + latitude + "," + longitude + ")";
                webview.evaluateJavascript(jsCode1, null);

            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });


        if (cameraResult != null) {
            Log.d("notice","null이 아닐시 load");
            Log.d("cameraresult", cameraResult);
            webview.loadUrl("file:///android_asset/camera.html");
        } else {
            webview.loadUrl("file:///android_asset/main.html");
        }

    }

    //권한 관련
    private boolean hasPermissions(String[] permissions){
        int result;
        for (String perms : permissions){
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

                    boolean fineLocationPermissionAccepted = grantResults[1]
                            == PackageManager.PERMISSION_GRANTED;

                    boolean coarseLocationPermissionAccepted = grantResults[2]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted || !fineLocationPermissionAccepted || !coarseLocationPermissionAccepted) {
                        finish();
                        return;
                    }
                }
                break;
        }
    }


    //현재위치관련
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 위치가 변경되었을 때 동작
            latitude = location.getLatitude(); // 위도
            longitude = location.getLongitude(); // 경도
        }
    };


    //뒤로가기 버튼
    @Override
    public void onBackPressed(){
        if(webview!=null){
            webview.evaluateJavascript("goBack();",null);
        }else{
            super.onBackPressed();
        }
    }

    //오늘 날짜
    public static int getCurrentDate(){
        Date dateNow= Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("M", Locale.getDefault());
        String formattedDate = format.format(dateNow);
        return Integer.parseInt(formattedDate);
    }

    //오늘 요일
    public static int getCurrentWeek(){
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeekNumber;
    }

    private CountDownLatch threadCount = new CountDownLatch(2);

    private void dataRecommend(){
        int week_num=getCurrentWeek();
        int month_num=getCurrentDate();

        String dayOfWeek="";
        String season="";
        if(week_num==1){
            dayOfWeek="일";
        }
        else if(week_num==2){
            dayOfWeek="월";
        }
        else if(week_num==3){
            dayOfWeek="화";
        }
        else if(week_num==4){
            dayOfWeek="수";
        }
        else if(week_num==5){
            dayOfWeek="목";
        }
        else if(week_num==6){
            dayOfWeek="금";
        }
        else if(week_num==7){
            dayOfWeek="토";
        }

        if(month_num>=3 && month_num<=5){
            season="봄";
        }
        else if(month_num>=6 && month_num<=8){
            season="여름";
        }
        else if(month_num>=9 && month_num<=11){
            season="가을";
        }
        else if(month_num==12 || month_num<=2){
            season="겨울";
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.12.6/dbaccess/data_recommend.php?dayOfWeek=" + dayOfWeek + "&season=" + season;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String myResponse = response.body().string();
                Log.d("response", "Response: " + myResponse);

                try{
                    JSONArray jsonArray = new JSONArray(myResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String column1 = jsonObject.getString("문화재명");
                        Double column2 = jsonObject.getDouble("위도");
                        Double column3 = jsonObject.getDouble("경도");
                        Integer column4 = jsonObject.getInt("관람요금");
                        String column5 = jsonObject.getString("여는시간");
                        String column6 = jsonObject.getString("마감시간");
                        Integer column7 = jsonObject.getInt("조회수");
                        String column8 = jsonObject.getString("소재지");
                        String column9 = jsonObject.getString("대표이미지");

                        labelList.add(column1);
                        latitudeList.add(column2);
                        longitudeList.add(column3);
                        freeList.add(column4);
                        t_openList.add(column5);
                        t_closeList.add(column6);
                        viewList.add(column7);
                        addrList.add(column8);
                        imageList.add(column9);
                    }

                    threadCount.countDown();
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void dataInfo(){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        String url = "http://192.168.12.6/dbaccess/data_info.php";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String myResponse2 = response.body().string();
                Log.d("response", "Response: " + myResponse2);

                try{
                    JSONArray jsonArray = new JSONArray(myResponse2);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String column1 = jsonObject.getString("문화재명");
                        Double column2 = jsonObject.getDouble("위도");
                        Double column3 = jsonObject.getDouble("경도");;
                        String column4 = jsonObject.getString("소재지");
                        String column5 = jsonObject.getString("대표이미지");
                        String column6 = jsonObject.getString("설명");
                        String column7 = jsonObject.getString("관람정보");

                        slabelList.add(column1);
                        saddrList.add(column4);
                        simageList.add(column5);

                        ilabelList.add(column1);
                        iaddrList.add(column4);
                        iimageList.add(column5);
                        infoList.add(column6);
                        tourinfoList.add(column7);

                        flabelList.add(column1);
                        flatList.add(column2);
                        flngList.add(column3);
                        fimageList.add(column5);
                    }

                    threadCount.countDown();
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void fetchData(){
        dataRecommend();
        dataInfo();
    }


    public void dataGowebview(){
        try{
            threadCount.await();

            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    for (int i = 0; i < labelList.size(); i++) {
                        String label = labelList.get(i);
                        double latitude = latitudeList.get(i);
                        double longitude = longitudeList.get(i);
                        int free = freeList.get(i);
                        String topen= t_openList.get(i);
                        String tclose= t_closeList.get(i);
                        int views = viewList.get(i);
                        String addr = addrList.get(i);
                        String image = imageList.get(i);

                        //Log.d("confirm",label+latitude+longitude+free+topen+tclose);

                        // JavaScript 코드를 생성하여 WebView에서 실행
                        String jsCode2 = "javascript:recommendData(['" + label + "', " + latitude + ", " + longitude + ", " + free +",'"+topen+"', '"+tclose+"', "+views+", '"+addr+"', '"+image+"'])";
                        webview.evaluateJavascript(jsCode2, null);
                    }

                    for(int i=0; i<slabelList.size();i++){
                        String slabel= slabelList.get(i);
                        String saddr= saddrList.get(i);
                        String simage= simageList.get(i);

                        //Log.d("confirm",slabel+saddr+simage);

                        String jsCode3="javascript:searchData(['"+slabel+"', '"+saddr+"', '"+simage+"'])";
                        webview.evaluateJavascript(jsCode3, null);
                    }

                    String jsCode5="javascript:compareLabel()";
                    webview.evaluateJavascript(jsCode5, null);

                    for(int i=0; i<ilabelList.size();i++){
                        String ilabel= ilabelList.get(i);
                        String iaddr= iaddrList.get(i);
                        String iimage= iimageList.get(i);
                        String cpinfo= infoList.get(i);
                        String tourinfo= tourinfoList.get(i);

                        //Log.d("confirm",slabel+saddr+simage);

                        String jsCode4="javascript:infoData(['"+ilabel+"', '"+iaddr+"', '"+iimage+"', '"+cpinfo+"', '"+tourinfo+"'])";
                        webview.evaluateJavascript(jsCode4, null);
                    }

                    String jsCode6="javascript:loadDataAndRender()";
                    webview.evaluateJavascript(jsCode6, null);

                    for (int i = 0; i < flabelList.size(); i++) {
                        String flabel = flabelList.get(i);
                        double flat = flatList.get(i);
                        double flng = flngList.get(i);
                        String fimage= fimageList.get(i);

                        //Log.d("confirm",flabel+flat+flng+fimage);

                        String jsCode7 = "javascript:findData(['" + flabel + "', " + flat + ", " + flng +",'"+fimage+"'])";
                        webview.evaluateJavascript(jsCode7, null);
                    }
                }
            });
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }

}
