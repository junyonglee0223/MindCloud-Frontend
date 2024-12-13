// Google 로그인 함수
async function loginWithGoogle() {
  const authUrl = `https://accounts.google.com/o/oauth2/auth?client_id=228124254280-evoreqge20hcd58okm0ptbje1i160u4p.apps.googleusercontent.com&response_type=token&redirect_uri=https://${chrome.runtime.id}.chromiumapp.org/&scope=profile%20email`;

  return new Promise((resolve, reject) => {
    chrome.identity.launchWebAuthFlow(
      { url: authUrl, interactive: true },
      (redirectUrl) => {
        if (chrome.runtime.lastError) {
          console.error("Login failed:", chrome.runtime.lastError.message);
          reject(new Error("Login failed"));
          return;
        }

        const urlParams = new URLSearchParams(new URL(redirectUrl).hash.slice(1));
        const accessToken = urlParams.get("access_token");

        if (!accessToken) {
          reject(new Error("Access token not found"));
          return;
        }

        fetch("https://www.googleapis.com/oauth2/v2/userinfo", {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        })
          .then((response) => response.json())
          .then((user) => {
            const userData = {
              email: user.email,
              name: user.name,
            };
            chrome.storage.sync.set({ userData }, () => {
              console.log("User data saved:", userData);
              resolve(userData);
            });
          })
          .catch((error) => {
            console.error("Error fetching user info:", error);
            reject(error);
          });
      }
    );
  });
}

// 회원가입 처리
async function signupWithGoogle() {
  try {
    // 이미 등록된 사용자인지 확인
    const existingUser = await new Promise((resolve) => {
      chrome.storage.sync.get("userData", ({ userData }) => {
        resolve(userData);
      });
    });

    if (existingUser && existingUser.email) {
      console.log("User already exists, logging in:", existingUser);
      return existingUser; // 이미 가입된 경우 로그인 처리
    }

    // 신규 사용자 가입 처리
    const userData = await loginWithGoogle();
    console.log("Signup successful:", userData);
    return userData;
  } catch (error) {
    console.error("Signup failed:", error.message);

    // 회원가입 실패 시 데이터 초기화
    chrome.storage.sync.set({ userData: null }, () => {
      console.error("User data reset due to signup failure.");
    });
    throw error;
  }
}

// 로그아웃 처리
function logoutUser() {
  chrome.storage.sync.remove("userData", () => {
    console.log("User has been logged out.");
  });
}

// 메시지 처리
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "login") {
    loginWithGoogle()
      .then(() => sendResponse({ success: true }))
      .catch(() => sendResponse({ success: false }));
    return true; // 비동기 응답
  }

  if (message.action === "signup") {
    signupWithGoogle()
      .then(() => sendResponse({ success: true }))
      .catch(() => sendResponse({ success: false }));
    return true; // 비동기 응답
  }

  if (message.action === "logout") {
    logoutUser();
    sendResponse({ success: true });
  }
});
