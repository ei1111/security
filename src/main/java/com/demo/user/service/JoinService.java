package com.demo.user.service;


import com.demo.user.domain.JoinRequestDto;

public interface JoinService {
    void joinProcess(JoinRequestDto joinRequestDto);
}
