package net.junios.api.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //    anonymous()
        //    인증되지 않은 사용자가 접근할 수 있습니다.
        //    authenticated()
        //    인증된 사용자만 접근할 수 있습니다.
        //            fullyAuthenticated()
        //    완전히 인증된 사용자만 접근할 수 있습니다(?)
        //    hasRole() or hasAnyRole()
        //    특정 권한을 가지는 사용자만 접근할 수 있습니다.
        //            hasAuthority() or hasAnyAuthority()
        //    특정 권한을 가지는 사용자만 접근할 수 있습니다.
        //            hasIpAddress()
        //    특정 아이피 주소를 가지는 사용자만 접근할 수 있습니다.
        //    access()
        //    SpEL 표현식에 의한 결과에 따라 접근할 수 있습니다.
        //    not() 접근 제한 기능을 해제합니다.
        //    permitAll() or denyAll()
        //    접근을 전부 허용하거나 제한합니다.
        //    rememberMe()
        //    리멤버 기능을 통해 로그인한 사용자만 접근할 수 있습니다.
        http
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
//                .and()
//                .headers() //h2-console 사용을 위한 설정
//                .addHeaderWriter(
//                        new XFrameOptionsHeaderWriter(
//                                new WhiteListedAllowFromStrategy(Arrays.asList("localhost"))    // 여기!
//                        )
//                )
//                .frameOptions().sameOrigin()    // 여기도 추가!!
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                .antMatchers("/*/signin", "/*/signup").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//                .antMatchers("/h2-console/**").permitAll() // db test 접근
//                .antMatchers("/h2-console/**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .antMatchers(HttpMethod.GET, "/helloworld/**").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
                .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣는다

    }

    @Override // ignore check swagger resource, ignore h2-console
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/h2-console/**");

    }
}