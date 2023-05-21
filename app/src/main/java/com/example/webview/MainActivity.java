package com.example.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.webview.MariaDBConnector;

public class MainActivity extends AppCompatActivity {
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LocationManager 객체 생성
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        webview = findViewById(R.id.mainWebview);
        // 웹뷰 설정 추가

        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permission2 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        //권한 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 ==
                PackageManager.PERMISSION_DENIED) {
            //마쉬멜로우 버전 이상부터 권한 묻기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1000
                );
            }
            return;
        }


        webview.getSettings().setJavaScriptEnabled(true);
        //webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 위치 정보 제공자 설정
                String provider = LocationManager.GPS_PROVIDER;

                // 위치 정보 요청
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                    return;
                }
                locationManager.requestLocationUpdates(provider, 1000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude(); // 위도 값
                        double longitude = location.getLongitude(); // 경도 값

                        // WebView에서 JavaScript 함수 호출하여 위치 정보 전달
                        String javascript = "javascript:updateMapLocation(" + latitude + "," + longitude + ")";
                        webview.evaluateJavascript(javascript, null);
                    }
                });

                fetchDataFromDatabase();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        webview.loadUrl("file:///android_asset/main.html");
        /*
        webview.addJavascriptInterface(new JavaScriptInterface(),"AndroidInterface");
        webview.post(new Runnable() {
            @Override
            public void run() {
                // WebView가 준비된 후에 AndroidInterface.callAPI() 호출
                webview.loadUrl("javascript:AndroidInterface.callAPI()");
            }
        });
         */
    }

    /*
    public class JavaScriptInterface{
        @JavascriptInterface
        public void callAPI(){
            webview.loadUrl("javascript:fetch(url,{headers})");
        }
    }

     */

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {

            } else {
                finish();
            }
        }
    }

    //메인 스레드에서 네트워크 작업을 하지 않게 Executor 사용, db 정보 불러오기
    private void fetchDataFromDatabase() {
        Executor executor = Executors.newSingleThreadExecutor();
        //ArrayList<Double> latitudeList=new ArrayList<>();
        //ArrayList<Double> longitudeList=new ArrayList<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = MariaDBConnector.getConnection();
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM culture";
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        String column1 = resultSet.getString("문화재명");
                        String column2 = resultSet.getString("소재지");
                        Double column3 = resultSet.getDouble("위도");
                        Double column4= resultSet.getDouble("경도");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String jsCode2="javascript:markers("+column3.toString()+","+column4.toString()+");";
                                webview.evaluateJavascript(jsCode2,null);
                            }
                        });


                        //latitudeList.add(column3);
                        //longitudeList.add(column4);
                    }
                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("column3", String.valueOf(latitudeList));
                            Log.d("column4", String.valueOf(longitudeList));
                            // ArrayList를 javascipt 배열로 변환
                            StringBuilder jsArray=new StringBuilder();
                            jsArray.append("[");
                            for (int i=0; i<latitudeList.size();i++){
                                double latitude=latitudeList.get(i);
                                double longitude=longitudeList.get(i);
                                jsArray.append("["+latitude + "," + longitude+"]");
                                if (i < latitudeList.size() - 1) {
                                    jsArray.append(", ");
                                }
                            }
                            jsArray.append("]");

                            String jsCode2="javascript:markers("+jsArray.toString()+");";
                            webview.evaluateJavascript(jsCode2,null);


                        }


                    });
                    */
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}


