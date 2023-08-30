package com.security.prac.config;

import com.security.prac.domain.Member;
import com.security.prac.filter.MyCsrfCookieFilter;
import com.security.prac.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collections;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {
        // 아래는 csrf 토큰을 header에 노출 시켜줌 => 자세히 알아볼 것
        CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
        csrfRequestHandler.setCsrfRequestAttributeName("_csrf"); // default name이랑 같다.


        http.securityContext(context -> context.requireExplicitSave(false)) // 개발자가 JSESSION 쿠키 직접 저장 하지 않고 시큐리티에게 위임
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        //csrf 쿠키 설정 시작 => 매 요청 마다 BasicAuthenticationFilter시행 이후(로그인 성공)에 csrf 토큰 발급
        http.csrf(csrf -> csrf.csrfTokenRequestHandler(csrfRequestHandler).ignoringRequestMatchers(mvc.pattern("/contact"), mvc.pattern("/register"))
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // csrfToken을 메모리에 상주시킴 + client(react,angular)가 읽을 수 있게 설정(httpOnlyFalse())
                .addFilterAfter(new MyCsrfCookieFilter(), BasicAuthenticationFilter.class);
        //csrf 토큰 발급 끝
        http.authorizeHttpRequests((req) -> req
                .requestMatchers(
                        mvc.pattern("/myCards"),
                        mvc.pattern("/myLoans"),
                        mvc.pattern("/myAccount"),
                        mvc.pattern("/myBalence")
                ).authenticated()
                .requestMatchers(mvc.pattern("/notices"),mvc.pattern("/contact")).permitAll()
                .requestMatchers(toH2Console()).permitAll()
        );
        /* h2 config start */
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().permitAll()
                )
                .headers(header->header.frameOptions(option->option.disable()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
        /* h2 config end */

        /* deny All the request */
        /*
            http.authorizeHttpRequests((req) -> req
                    .anyRequest().denyAll()
            );
        */
        //LEARN cors 설정 start
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }
        }));
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

    //@Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    PasswordEncoder passwordEncoderV1() {
        return new BCryptPasswordEncoder();
    }
}
