package com.security.prac.config;

import com.security.prac.domain.Member;
import com.security.prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberRepository memberRepository;
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests((req) -> req
                .requestMatchers(
                        mvc.pattern("/myCards"),
                        mvc.pattern("/myLoans"),
                        mvc.pattern("/myAccount"),
                        mvc.pattern("/myBalence")
                ).authenticated()
                .requestMatchers(mvc.pattern("/notices"),mvc.pattern("/contact")).permitAll()
                .anyRequest().permitAll()
        );
        /* deny All the request */
        /*
        http.authorizeHttpRequests((req) -> req
                .anyRequest().denyAll()
        );
        */
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        //TODO: optional 처리하기 in case when user does not exists
        return username -> {
            Member findUser = memberRepository.findByEmail(username).get();
            return new UserPrincipal(findUser);
        };
    }

    //@Bean
    InMemoryUserDetailsManager defaultUser() {
        UserDetails user1 = User.withUsername("user1")
                .password("1234")
                .roles("ADMIN")
                .build();
        UserDetails user2 = User.withUsername("user2")
                .password("1234")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1,user2);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
