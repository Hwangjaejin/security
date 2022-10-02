package com.example.security.controller;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller // View를 리턴
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"","/"})
    public String index() {
        // 머스테치 기본폴더 : src/main/resources
        // 뷰리졸버 설정 : templates(prefix), .mustache(suffix)  생략가능.
        return "index"; // src/main/resources/templates/index.mustache 로 찾게됨
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 이 주소를 낚아채서 로그인페이지로 이동됨. -> SecurityConfig 파일 생성 후 안 낚아챔.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user = " + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user); // 회원가입 잘 됨. 비밀번호 : 1234 => 시큐리티로 로그인 할 수 없음. 패스원드가 암호화 안되었기 때문. 위에처럼 인코딩해줘야 함.
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 메서드 실행전 권한확인. (여러 권한 확인할 때 유용)
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
