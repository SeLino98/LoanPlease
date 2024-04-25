package com.d105.loanplease.domain.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin("*")
public class ServerController {

    @GetMapping("/hi")
    public String test() {
        return "hi";
    }

    @GetMapping("/test")
    public String apitest() {
        log.info("test 함수 접속");
        return "API hi";
    }
}
