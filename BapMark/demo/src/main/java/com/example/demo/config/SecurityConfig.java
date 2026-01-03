package com.example.demo.config;

import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 프리플라이트 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 공개 엔드포인트
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/openapi/**",
                                "/openapi.json",
                                "/openapi.yaml",
                                "/docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/login-test.html" //로그인 테스트용
                        ).permitAll()

                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, userRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ✅ CORS 설정 (localhost:5173, ngrok 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration conf = new CorsConfiguration();

        // 프론트/프록시 도메인 허용
        conf.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:*",
                "https://*.ngrok-free.app" // ngrok 주소가 바뀌어도 허용
        ));

        conf.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        conf.setAllowedHeaders(List.of("Content-Type","Authorization","X-Requested-With","Accept"));
        conf.setExposedHeaders(List.of("Authorization")); // 필요시 노출
        conf.setAllowCredentials(true); // 쿠키/인증정보 허용(헤더 JWT만 써도 true 안전)

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", conf);
        return src;
        // 참고: credentials(true)일 때는 allowedOrigins("*")를 사용할 수 없고,
        // allowedOriginPatterns 또는 구체 도메인만 가능해요.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
