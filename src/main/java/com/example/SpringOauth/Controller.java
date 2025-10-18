package com.example.SpringOauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    @GetMapping("/")
    public String securedPage(@AuthenticationPrincipal OAuth2User oauth2User) {
        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        SecurityContext ctx=SecurityContextHolder.getContext();

        return "secured";
    }

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo() {

        return ResponseEntity.ok("Hi "+SecurityContextHolder.getContext().getAuthentication().getName());
    }


}