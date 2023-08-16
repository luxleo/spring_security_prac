package com.security.prac.config;

import com.security.prac.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {
    private final Long userId;

    public UserPrincipal(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(
                new SimpleGrantedAuthority(member.getRole().name())
        ));
        userId = member.getId();
    }

}
