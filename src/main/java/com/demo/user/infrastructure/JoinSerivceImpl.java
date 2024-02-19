package com.demo.user.infrastructure;

import com.demo.user.domain.JoinRequestDto;
import com.demo.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JoinSerivceImpl implements JoinService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public void joinProcess(JoinRequestDto joinRequestDto) {
            String encode = passwordEncoder.encode(joinRequestDto.getPassword());
            System.out.println("encode: " + encode);
          System.out.println(passwordEncoder.matches(joinRequestDto.getPassword(), encode));
    }
}
