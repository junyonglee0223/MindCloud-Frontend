package kr.brain.our_app.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.brain.our_app.user.domain.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class PageController {
    private final RestTemplate restTemplate;
    public PageController(){
        this.restTemplate = new RestTemplate();
    }

    // 서버 기본 URL을 동적으로 가져오는 메서드
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http 또는 https
        String serverName = request.getServerName();     // 서버 이름 또는 IP
        int serverPort = request.getServerPort();        // 포트 번호
        String contextPath = request.getContextPath();   // 컨텍스트 경로

        // 기본 경로 생성
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }
    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // src/main/resources/templates/login.html 파일을 렌더링
    }
    // 로그인 처리
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, Model model, HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/users/profile?email=" + email;
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(apiUrl, User.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                model.addAttribute("message", "Login successful, welcome " + response.getBody().getUsername());
                return "index";  // 로그인 성공 시 리다이렉트할 페이지
            } else {
                model.addAttribute("message", "Login failed: Email not found.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Login failed: An error occurred.");
            return "login";
        }
    }

    // 사용자 목록 페이지 렌더링
    @GetMapping("/list")
    public String userListPage(Model model, HttpServletRequest request) {
        // /api/users/all 경로로 GET 요청하여 사용자 데이터 가져옴
        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/users/all"; // UserController의 엔드포인트
        User[] users = restTemplate.getForObject(apiUrl, User[].class);
        List<User> userList = Arrays.asList(users);

        model.addAttribute("users", userList);
        return "user_list";  // src/main/resources/templates/user_list.html 파일을 렌더링
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerPage() {
        return "register";  // src/main/resources/templates/register.html 렌더링
    }
    // 회원가입 요청 처리
    @PostMapping("/register")
    public String handleRegistration(@RequestParam String username, @RequestParam String email, Model model
                                     ,HttpServletRequest srequest) {
        // 회원가입을 위한 데이터 준비
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);

        // API 호출 경로
        //String apiUrl = "http://localhost:8080/api/users"; // UserController의 엔드포인트
        String baseUrl = getBaseUrl(srequest);
        String apiUrl = baseUrl + "/api/users";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 객체 생성
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);

        // POST 요청을 보내 회원가입 처리
        ResponseEntity<User> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, User.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 회원가입 성공 시
            model.addAttribute("message", "회원가입이 성공했습니다.");
            return "redirect:/"; // 성공 시 기존 페이지로 리다이렉트
        } else {
            // 회원가입 실패 시
            model.addAttribute("message", "회원가입에 실패했습니다.");
            return "register"; // 실패 시 다시 회원가입 페이지로
        }
    }
}
