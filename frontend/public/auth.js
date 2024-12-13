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

// 회원가입 버튼 클릭 이벤트
document.getElementById("signup-button").addEventListener("click", () => {
  chrome.runtime.sendMessage({ action: "signup" }, () => {
    chrome.storage.sync.get("userData", ({ userData }) => {
      if (userData) {
        window.location.href = "../bookmark_check/bookmark_check.html";
        // document.getElementById("user-info").innerHTML = `
        //   <p>Welcome (or signed up), ${userData.name} (${userData.email})</p>
        // `;
      } else {
        document.getElementById("user-info").innerHTML = `
          <p>Sign up failed. Please try again.</p>
        `;
      }
    });
  });
});

// 로그아웃 버튼 클릭 이벤트
document.getElementById("logout-button").addEventListener("click", () => {
  chrome.runtime.sendMessage({ action: "logout" }, () => {
    document.getElementById("user-info").innerHTML = `
      <p>You have been logged out. Please log in or sign up.</p>
    `;
  });
});

// 페이지 로드 시 기존 사용자 정보 표시
document.addEventListener("DOMContentLoaded", () => {
  chrome.storage.sync.get("userData", ({ userData }) => {
    if (userData) {
      window.location.href = "../bookmark_check/bookmark_check.html";

      // document.getElementById("user-info").innerHTML = `
      //   <p>Welcome back, ${userData.name} (${userData.email})</p>
      // `;
    } else {
      document.getElementById("user-info").innerHTML = "<p>Please log in, sign up, or log out.</p>";
    }
  });
});
