package club.banyuan.studyweb.config;

import club.banyuan.studyweb.annotation.LoggerAnnotation;
import club.banyuan.studyweb.controller.LoginController;
import club.banyuan.studyweb.controller.LogoutController;
import club.banyuan.studyweb.entity.User;
import club.banyuan.studyweb.service.UserService;
import club.banyuan.studyweb.service.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger= LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    UserServiceImpl userService;

    @Autowired
    LoginController loginController;

    @Autowired
    LogoutController logoutController;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();
                User user = userService.selectByUsername(username);
                if (user == null) {
                    throw new UsernameNotFoundException("账号不存在");
                }
                if (!new BCryptPasswordEncoder().matches(password,user.getPassword())) {
                    throw new BadCredentialsException("密码不正确");
                }
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return UsernamePasswordAuthenticationToken.class.equals(aClass);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/index", "/login", "/register","/loginPage").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .and().exceptionHandling().accessDeniedPage("/denied")
//                .anyRequest().authenticated()

                .and().formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/loginPage").permitAll()
                .loginProcessingUrl("/login")
                .failureHandler(loginController)
                .successHandler(loginController)

                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutController)

                .and()
                .rememberMe().rememberMeParameter("rememberme")
                .tokenValiditySeconds(20*3600)
                .userDetailsService(userService)
                .tokenRepository(new InMemoryTokenRepositoryImpl());

        //    增加一个filter用于验证验证码
        http.addFilterBefore(new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest request=(HttpServletRequest)servletRequest;
                HttpServletResponse response=(HttpServletResponse)servletResponse;
                String kaptcha=(String) request.getSession().getAttribute("kaptcha");
                if(request.getServletPath().equals("/login")){
                    String verifyCode=request.getParameter("verifyCode");
                    if (verifyCode==null || !verifyCode.equals(kaptcha)){
                        System.out.println("-------------");
                        request.setAttribute("verifyCodeError","验证码错误");
                        request.getRequestDispatcher("/loginPage").forward(request,response);
                        return;
                    }
                }
                filterChain.doFilter(request,response);
            }
        }, UsernamePasswordAuthenticationFilter.class);

    }


    //    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userService);
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        return provider;
//    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
}
