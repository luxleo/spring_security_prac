package com.security.prac.repository;

import com.security.prac.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    void findMember() {
        //given
        String userEmail = "binzino@";
        Member user1 = Member.builder()
                .name("binzino")
                .email(userEmail)
                .password("1234")
                .build();
        memberRepository.saveAndFlush(user1);

        //when
        Member findUser = memberRepository.findByEmail(userEmail).get();
        //then
        Assertions.assertThat(findUser.getEmail()).isEqualTo(userEmail);
    }

}