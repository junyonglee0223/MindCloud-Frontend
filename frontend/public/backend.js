//const baseUrl = "http://localhost:8080";
const baseUrl = "http://43.201.62.22";

function createApiUrl(endpoint){
  return `${baseUrl}${endpoint}`;
}

// 백엔드로 북마크 데이터를 보내는 함수
function sendBookmarkToBackend(bookmark) {
    //const backendUrl = "http://localhost:8080/api/tagbookmark/in"; // 백엔드 API URL
    const backendUrl = createApiUrl("/api/tagbookmark/in");
    const userName = "test1";
    const email = "test1@gmail.com";

// RequestFrontDto 형식에 맞게 데이터 구성
const requestFrontDto = {
    title: bookmark.title,
    url: bookmark.url,
    tags: bookmark.tags,
    userName: userName,   
    email: email  
};
  // 요청 전 로그 출력
  console.log('백엔드로 보낼 데이터:', requestFrontDto);

  return fetch(backendUrl, {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestFrontDto),
  })
  .then(response => {
      // 응답 상태 코드 확인
      console.log('응답 상태 코드:', response.status);
      if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
  })
  .then(data => {
      console.log('백엔드로부터 응답 받음:', data);
      return data;
  })
  .catch(error => {
      console.error('백엔드로 데이터 전송 중 오류 발생:', error);
  });
}



// 백엔드에서 북마크 데이터를 받아오는 함수
function getBookmarksFromBackend(tagName, userEmail) {
  //const backendUrl2 = `http://localhost:8080/api/tagbookmark/out?tagName=${encodeURIComponent(tagName)}&userEmail=${encodeURIComponent(userEmail)}`; // 백엔드 API URL
  const backendUrl2 = createApiUrl(`/api/tagbookmark/out?tagName=${encodeURIComponent(tagName)}&userEmail=${encodeURIComponent(userEmail)}`);
  return fetch(backendUrl2, {
      method: 'GET',
      headers: {
          'Content-Type': 'application/json',
      },
  })
  .then(response => {
      if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
  })
  .then(data => {
      console.log('백엔드에서 북마크 목록을 받았습니다:', data);
      return data;
  })
  .catch(error => {
      console.error('백엔드에서 데이터 불러오는 중 오류 발생:', error);
  });
}

function getAllBookmarksFromBackend(userEmail){
    // const backendUrl3 = 
    // `http://localhost:8080/api/tagbookmark/outAll?userEmail=${encodeURIComponent(userEmail)}`; // 백엔드 API URL
    
    const backendUrl3 = createApiUrl(`/api/tagbookmark/outAll?userEmail=${encodeURIComponent(userEmail)}`);
 
    return fetch(backendUrl3, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('백엔드에서 북마크 목록을 받았습니다:', data);
        return data;
    })
    .catch(error => {
        console.error('백엔드에서 데이터 불러오는 중 오류 발생:', error);
    });
}


// 검색 결과를 백엔드에서 가져오는 함수
function fetchSearchResults(word, email) {
    //const apiUrl = `http://localhost:8080/api/search?word=${encodeURIComponent(word)}&email=${encodeURIComponent(email)}`;
    const apiUrl = createApiUrl(`/api/search?word=${encodeURIComponent(word)}&email=${encodeURIComponent(email)}`);
 
    return fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .catch((error) => {
        console.error('백엔드 검색 API 호출 중 오류 발생:', error);
        throw error; // 오류를 상위로 전달
      });
}

// 백엔드에서 북마크 삭제 요청
function deleteBookmarkFromBackend(title, email) {
    // title과 email을 쿼리 파라미터로 전달
    //const apiUrl = `http://localhost:8080/api/search/bookmarks?title=${encodeURIComponent(title)}&email=${encodeURIComponent(email)}`;
    const apiUrl = createApiUrl(`/api/search/bookmarks?title=${encodeURIComponent(title)}&email=${encodeURIComponent(email)}`);
 
    return fetch(apiUrl, {
      method: 'DELETE', // HTTP DELETE 메서드 사용
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .catch((error) => {
        console.error('백엔드 삭제 API 호출 중 오류 발생:', error);
        throw error; // 상위로 오류 전달
      });
  }
  
  