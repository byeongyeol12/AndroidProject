

WebView webView = (WebView) findViewById(R.id.web_view);
webView.setWebViewClient(new WebViewClient());
webView.getSettings().setJavaScriptEnabled(true);
webView.loadUrl("file:///android_asset/3-8.html");

webView.setWebViewClient(new WebViewClient() {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("#search")) {
            // 검색 버튼 클릭 시 동작할 코드 작성
        } else if (url.contains("#camera")) {
            // 촬영 버튼 클릭 시 동작할 코드 작성
        } else if (url.contains("#recommend")) {
            // 추천 버튼 클릭 시 동작할 코드 작성
        } else if (url.contains("#mypage")) {
            // 마이페이지 버튼 클릭 시 동작할 코드 작성
        }

        return true;
    }
});