package com.demo.user.infrastructure;

import com.demo.common.exception.UserNotFoundException;
import com.demo.user.domain.JoinRequestDto;
import com.demo.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JoinSerivceImpl implements JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void joinProcess(JoinRequestDto joinRequestDto) {
        String userId = joinRequestDto.getUserId();
        UserEntity userEntity = new UserEntity();
        //유저 존재 확인
        Boolean isExistBool = userRepository.existsByUserId(userId);
        if(Boolean.TRUE.equals(isExistBool)){
            throw new UserNotFoundException("존재하는 회원이 있습니다");
        }else{
            String userName = joinRequestDto.getUserName();
            String password = bCryptPasswordEncoder.encode(joinRequestDto.getPassword());

            userEntity.setUserId(userId);
            userEntity.setUserName(userName);
            userEntity.setPassword(password);
            userEntity.setRole("ROLE_ADMIN");
            userRepository.save(userEntity);
        }
    }
}
