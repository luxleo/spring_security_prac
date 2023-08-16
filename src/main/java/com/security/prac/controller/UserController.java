package com.security.prac.controller;

import com.security.prac.domain.Member;
import com.security.prac.domain.MemberRole;
import com.security.prac.repository.MemberRepository;
import com.security.prac.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final MemberRepository memberRepository;
    @PostMapping("/register")
    public void register(@RequestBody @Validated SignupRequest signUp) {
        Optional<Member> foundMember = memberRepository.findByEmail(signUp.getEmail());
        if (foundMember.isPresent()) {
            return;
        }
        // 기본으로 user를 부여한다.

        Member newMember = Member.builder()
                .name(signUp.getName())
                .email(signUp.getEmail())
                .password(signUp.getPassword())
                .role(MemberRole.USER)
                .build();
        memberRepository.save(newMember);
    }
}
