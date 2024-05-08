package com.d105.loanplease.domain.friend.controller;

import com.d105.loanplease.domain.friend.request.FriendReq;
import com.d105.loanplease.domain.friend.service.FriendService;
import com.d105.loanplease.domain.friend.service.NotificationService;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendRequestController {

    private NotificationService notificationService;

    private UserRepository userRepository;
    private FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendReq request) {
        // 예를 들어, DTO에는 요청을 보내는 userId와 친구가 될 userId가 포함됩니다.
        Optional<User> sender = userRepository.findById(request.getSenderId());
        Optional<User> receiver = userRepository.findById(request.getReceiverId());
        if (sender.isPresent() && receiver.isPresent()) {
            // 데이터베이스에 친구 요청을 저장하는 로직 추가
            // 예: friendRequestRepository.save(new FriendRequest(...));
            // NotificationService를 사용하여 요청을 수신자에게 통지
            notificationService.sendFriendRequestNotification(request.getReceiverId(), "You have a new friend request from " + sender.get().getUsername());
            return ResponseEntity.ok("Friend request sent successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid sender or receiver ID");
    }

//    @PostMapping("/accept/{requestId}")
//    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long requestId) {
//        return friendService.processFriendRequest(requestId, true);
//    }
//
//    @PostMapping("/reject/{requestId}")
//    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long requestId) {
//        return friendService.processFriendRequest(requestId, false);
//    }
}

