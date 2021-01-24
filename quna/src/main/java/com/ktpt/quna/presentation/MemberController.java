package com.ktpt.quna.presentation;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ktpt.quna.application.MemberService;
import com.ktpt.quna.application.dto.LoginRequest;
import com.ktpt.quna.application.dto.MemberCreateRequest;
import com.ktpt.quna.application.dto.MemberResponse;
import com.ktpt.quna.application.dto.TokenResponse;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> create(@RequestBody @Valid MemberCreateRequest request) {
        MemberResponse response = memberService.create(request);
        return ResponseEntity.created(URI.create("/members/" + response.getId())).body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }
}
