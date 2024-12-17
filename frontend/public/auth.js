// 로그인 버튼 클릭 이벤트
document.getElementById("login-button").addEventListener("click", () => {
  chrome.runtime.sendMessage({ action: "login" }, () => {
    chrome.storage.sync.get("userData", ({ userData }) => {
      if (userData) {
        window.location.href = "../bookmark_check/bookmark_check.html";
        // document.getElementById("user-info").innerHTML = `
        //   <p>Welcome, ${userData.name} (${userData.email})</p>
        // `;
      } else {
        document.getElementById("user-info").innerHTML = `
          <p>Login failed. Please try again.</p>
        `;
      }
    });
  });
});

// 페이지 로드 시 기존 사용자 정보 표시
document.addEventListener("DOMContentLoaded", () => {
  chrome.storage.sync.get("userData", ({ userData }) => {
    if (userData) {
      window.location.href = "../bookmark_check/bookmark_check.html";

      document.getElementById("user-info").innerHTML = `
        <p>Welcome back, ${userData.name} (${userData.email})</p>
      `;
    } else {
      document.getElementById("user-info").innerHTML = "<p>Please log in, sign up, or log out.</p>";
    }
  });
});
