package com.security.prac;

import com.security.prac.domain.Member;
import com.security.prac.domain.MemberRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    void init() {
        initService.dbInit();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;

        public void dbInit() {
            Member test1 = Member.builder()
                    .name("test1")
                    .email("test1@gmail.com")
                    .password("1234")
                    .role(MemberRole.USER)
                    .build();
            Member test2 = Member.builder()
                    .name("test2")
                    .email("test2@gmail.com")
                    .password("1234")
                    .role(MemberRole.ADMIN)
                    .build();
            em.persist(test1);
            em.persist(test2);
        }
    }
}
