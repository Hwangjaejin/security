package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller // View를 리턴
public class IndexController {

    @GetMapping({"","/"})
    public String index() {
        // 머스테치 기본폴더 : src/main/resources
        // 뷰리졸버 설정 : templates(prefix), .mustache(suffix)  생략가능.
        return "index"; // src/main/resources/templates/index.mustache 로 찾게됨
    }
}
