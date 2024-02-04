package com.demo.user.controller;

import com.demo.common.response.CommonResponse;
import com.demo.user.domain.JoinRequestDto;
import com.demo.user.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/api/v1/join")
    public ResponseEntity<CommonResponse> joinPorcess(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        joinService.joinProcess(joinRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse("100", new HashMap()));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginPorcess(@RequestBody JoinRequestDto joinRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse("100", new HashMap()));
    }
}
