package com.d105.loanplease.domain.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/hi")
    public String test() {
        return "hi";
    }
}
