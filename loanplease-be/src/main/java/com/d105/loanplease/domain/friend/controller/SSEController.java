package com.d105.loanplease.domain.friend.controller;

import com.d105.loanplease.domain.friend.service.NotificationService;
import com.d105.loanplease.domain.friend.service.SseEmitters;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class SSEController {

    private final NotificationService notificationService;
    private final SseEmitters sseEmitters;

    //처음 연결시 사용된다.
//    @GetMapping("/notifications/{userId}")
//    public SseEmitter handle(@PathVariable Long userId) {//
//        return notificationService.createEmitter(userId);
//    }
    @GetMapping(value = "api/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

}

