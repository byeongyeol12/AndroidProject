
window.addEventListener("DOMContentLoaded", (event) => {
    const navItems = document.querySelectorAll(".nav-item");
    navItems.forEach((item) => {
            item.addEventListener("click", (event) => {
            // 클릭된 아이템에 대한 동작을 여기에 작성합니다.
            // 예: 각 버튼에 맞는 페이지로 이동하도록 설정
            const itemId = item.id;
            switch (itemId) {
                case "search":
                    window.location.href = "file:///android_asset/search.html";
                    break;
                case "find":
                    window.location.href = "file:///android_asset/find.html";
                    break;
                case "camera":
                    window.location.href = "file:///android_asset/camera.html";
                    break;
                case "recommend":
                    window.location.href = "file:///android_asset/recommend.html";
                    break;
                case "mypage":
                    window.location.href = "file:///android_asset/mypage.html";
                    break;
                default:
                    break;
            }
        });
    });
});
