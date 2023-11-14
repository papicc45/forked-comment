package com.weatherfit.comment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @GetMapping("/test")
    public String test() {
        return "test2";
    }
}
