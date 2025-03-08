package com.duongpham26.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.duongpham26.demo.util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

   @Value("${duongpham26.jwt.base64-secret}")
   private String jwtKey;

   private String[] whiteList = { "/", "/api/v1/auth/login", "/api/v1/auth/login", "/api/v1/auth/refresh",
         "/storage/**",
         "api/v1/companies/**", "/api/v1/jobs/**" };

   @Bean
   public PasswordEncoder passwordEncoder() { // Cau hinh ma hoa mat khau su dụng Bcrypt
      return new BCryptPasswordEncoder();
   }

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http,
         CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
      http
            .csrf(c -> c.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(
                  authz -> authz
                        .requestMatchers(this.whiteList).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/companies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/skill/**").permitAll()
                        .anyRequest().authenticated())
            .oauth2ResourceServer(
                  (oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()) // Customizer.withDefaults() == {} => thêm
                                                        // BeareTokenAuthenticationFilter => cần giải mã(thêm @Bean
                                                        // JwtDecoder(JwtDecode()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint)) // xử lí ngoại lệ cho security filter
                                                                                   // // 401
            // .exceptionHandling(exceptions -> exceptions
            // .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
            // cần đăng nhập
            // .accessDeniedHandler(new BearerTokenAccessDeniedHandler()) // 403 cần có
            // quyền truy cập khi đăng nhập rồi
            // )
            .formLogin(f -> f.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return http.build();
   }

   // nap vao context sao khi decode token
   @Bean
   public JwtAuthenticationConverter jwtAuthenticationConverter() {
      JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
      grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
      grantedAuthoritiesConverter.setAuthoritiesClaimName("permission"); // lấy tương ứng với key permession => nạp vào
                                                                         // context

      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
      return jwtAuthenticationConverter;
   }

   @Bean
   public JwtDecoder jwtDecoder() {
      NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
            .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
      return token -> {
         try {
            return jwtDecoder.decode(token);
         } catch (Exception e) {
            System.out.println(">>> JWT ERROR: " + e.getMessage());
            throw e;
         }
      };
   }

   // khai báo cho spring biết các mã hóa jwt
   @Bean
   public JwtEncoder jwtEncoder() {
      return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
   }

   private SecretKey getSecretKey() {
      byte[] keyBytes = Base64.from(this.jwtKey).decode(); // giải mã base 64
      return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
   }
}
