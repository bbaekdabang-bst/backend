package com.bbacks.bst.global.config;

import com.bbacks.bst.global.jwt.repository.CustomAuthorizationRequestRepository;
import com.bbacks.bst.domain.auth.service.AuthService;
import com.bbacks.bst.global.jwt.service.JwtService;
import com.bbacks.bst.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.bbacks.bst.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.bbacks.bst.global.oauth2.service.CustomOAuth2UserService;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.global.jwt.filter.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final JwtService jwtService;
    //private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    //private final RedisTemplate redisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(frame -> frame.disable()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/**", "/oauth2/**", "/auth/**", "/api/v1/auth/refresh")
                            .permitAll()
                        .requestMatchers("/", "/favicon.ico", "/**.png", "/**.jpg", "/**.html", "/**.css", "/**.js")
                            .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/authorize")
//                                .authorizationRequestRepository(customAuthorizationRequestRepository) // 권한 요청과 관련된 모든 상태는 지정된 authorizationRequestRepository 를 사용하여 저장된다.
                        )
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/login/oauth2/code/**")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                    );
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(authService, jwtService);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());;
//        return new ProviderManager(provider);
//    }

//    @Bean
//    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
//        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
//        return jwtAuthenticationFilter;
//    }
}
