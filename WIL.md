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
## password encoding
- 1.types => encoding,encrytion,hashing
- 2.encoding: 완전 reversable하다 plain pwd보다는 나으나 인코딩 포맷을 알 경우 디코딩이 쉽게 가능
- 3.encrytion: key를 설정하여 암호화 가능하나, developer는 설정파일에서 접근 가능하다.
- 4.hasing: hasing function을 이용하여 generated되며 non-reversible한 특징을 가진다.
  대표적인 방식: Bcrypt, Scrypt
  PasswordEncoder -> encode => 비밀 번호 해싱, 
    -> matches => 비밀번호 비교 
