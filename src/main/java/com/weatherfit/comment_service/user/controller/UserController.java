package com.weatherfit.comment_service.user.controller;

import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/check-id-duplicate")
    public Mono<Boolean> checkIdDuplicate(@RequestParam String userId) {
        return userService.duplicateCheck(userId);
    };

    @PostMapping("/find-userId")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> findUserId(@RequestParam String userName, @RequestParam String userEmail) {
        return userService.checkUserNameAndEmailMatch(userName, userEmail);
    }
}
