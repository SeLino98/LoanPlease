package com.d105.loanplease.domain.friend.service;

import com.d105.loanplease.domain.friend.dto.FriendDetailDto;
import com.d105.loanplease.domain.friend.dto.response.FriendListRes;
import com.d105.loanplease.domain.friend.repository.FriendRepository;
import com.d105.loanplease.domain.user.entity.Friendship;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.BaseResponseBody;
import com.d105.loanplease.global.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class FriendService {


    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

//ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of("200", tokenResDto));
    @Transactional
    public ResponseEntity<BaseResponseBody<Boolean>> processFriendRequest(boolean accept, Long acceptorId) {

//        User senderNickName = userRepository.findById(requestId);
        Long userId = SecurityUtil.getCurrentUserId(); // 요청에 대한 답변을 한 사람


        return friendRepository.findById(userId).map(request -> { //요청자의 freind 값을 찾는다.
            if (accept) { //수락한 상태 -> 답변자의 테이블에 친구 테이블을 생성한다.
                              // 피답변자의 테이블의 친구 테이블을 생성한다.z
                request.setIsAccess(true);
                notificationService.sendFriendAcceptNotification(request.getFrom().getUserId(), "Your friend request has been accepted by " + request.getTo());

                //양쪽이 저장되어야 한다.
                friendRepository.save(request);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(BaseResponseBody.of("200",true));
            } else { //거절한 상태
//                friendRequestRepository.delete(request);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(BaseResponseBody.of("200",true));
            }
        }).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of("432",false)));
    }

    @Transactional
    public ResponseEntity<BaseResponseBody<FriendListRes>> getMyFriendList() {
        Long userId = SecurityUtil.getCurrentUserId(); // 현재 사용자의 ID를 가져옴
        log.info("userId : "+userId);

        List<Friendship> friends = friendRepository.findByFrom_UserId(userId);

        List<Friendship> realFriends = new ArrayList<>();

        for (Friendship value : friends) { //isAccept가 true인 애들만 list에 새로 담는다.
            if (value.getIsAccess()) {
                realFriends.add(value);
                log.info(value.getFrom()+" : "+value.getTo()+" : "+value.getFriendshipId());
            }else{
                log.info(value.getFrom()+" : "+value.getTo()+" : "+value.getFriendshipId());
            }
        }

        List<FriendDetailDto> friendList = realFriends.stream()
                .map(friend -> new FriendDetailDto(friend.getTo().getNickname(), friend.getTo().getEmail(), friend.getTo().getProfileImg()))
                .toList();
        FriendListRes friendListRes = FriendListRes.builder().friends(friendList).build();
        return ResponseEntity.ok(BaseResponseBody.of("200", friendListRes));
    }

    @Transactional
    public ResponseEntity<BaseResponseBody<FriendListRes>> searchFriendList(String friendName) {
        List<User> potentialFriends = userRepository.findByNicknameContaining(friendName);
        List<FriendDetailDto> friendList = potentialFriends.stream()
                .map(user ->new FriendDetailDto(user.getNickname(), user.getNickname(), user.getProfileImg()))
                .toList();
        FriendListRes friendListRes = FriendListRes.builder().friends(friendList.stream().toList()).build();
        return ResponseEntity.ok(BaseResponseBody.of("200",friendListRes));
    }


}
