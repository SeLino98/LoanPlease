package com.d105.loanplease.domain.friend.service;

import com.d105.loanplease.domain.friend.repository.FriendRepository;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.BaseResponseBody;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@AllArgsConstructor
@RequestMapping("/api")
public class FriendService {


    private FriendRepository friendRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;

//ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of("200", tokenResDto));
    @Transactional
    public ResponseEntity<BaseResponseBody<Boolean>> processFriendRequest(Long requestId, boolean accept) {
//        User senderNickName = userRepository.findById(requestId);
        return friendRepository.findById(requestId).map(request -> {
            if (accept) { //수락한 상태
                request.setAccept(true);
                notificationService.sendFriendAcceptNotification(request.getFromId().getUserId(), "Your friend request has been accepted by " + request.getToId());

                //양쪽이 저장되어야 한다.
                friendRepository.save(request);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(BaseResponseBody.of("200",true));
            } else { //거절한 상태
//                friendRequestRepository.delete(request);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(BaseResponseBody.of("200",true));
            }
        }).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of("432",false)));
    }

}
