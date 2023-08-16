# security
## 시큐리티는 항상 초기에 설정하라
## work flow of spring security:
## falsy config
     5.8.5, 6.0.5, 6.1.2 버젼에서 취약하다고 판단하는 오류가있다.
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests((req) -> req
                .requestMatchers(mvc.pattern("/myCards")).authenticated()
                .anyRequest().permitAll()
        );
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
    와 같이 requestMatchers에 mvc로 명시해주어야한다.
## default config
    SecurityFilterChainConfiguration -> SecurityFilterChain 재정의 -> default동작 재정의 가능
