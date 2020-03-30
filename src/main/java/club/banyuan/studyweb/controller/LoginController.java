package club.banyuan.studyweb.controller;

import club.banyuan.studyweb.annotation.LoggerAnnotation;
import club.banyuan.studyweb.config.KaptchaConfig;
import club.banyuan.studyweb.entity.User;
import club.banyuan.studyweb.service.UserService;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class LoginController implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    Producer kaptchaproducer;

    @Autowired
    UserService userService;

    @LoggerAnnotation
    @GetMapping("/loginPage")
    public String loginGet(Model model){
//        Object object=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if(object instanceof User){
//            model.addAttribute("user",object);
//        }
        return "site/login";
    }

    @GetMapping("/denied")
    public String deniedGet(){
        return "/error/404";
    }

    @LoggerAnnotation
    @GetMapping("/kaptcha")
    public void kaptchaGet(HttpServletResponse response, HttpSession session){
        String kaptchaText=kaptchaproducer.createText();
        BufferedImage kaptchaImage=kaptchaproducer.createImage(kaptchaText);
        session.setAttribute("kaptcha",kaptchaText);
        try {
            response.setContentType("image/png");
            OutputStream output=response.getOutputStream();
            ImageIO.write(kaptchaImage,"png",output);
        } catch (IOException e) {
            logger.error("响应验证码错误：{}",e.getMessage());
        }
    }

    @LoggerAnnotation
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletRequest.setAttribute("error",e.getMessage());
        httpServletRequest.getRequestDispatcher("/loginPage").forward(httpServletRequest,httpServletResponse);
    }

    @LoggerAnnotation
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        DefaultSavedRequest savedRequest=(DefaultSavedRequest)httpServletRequest.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if(savedRequest!=null){
            String redirectUrl=savedRequest.getRedirectUrl();
            String parameter=savedRequest.getQueryString();
            if(parameter!=null) {
                httpServletResponse.sendRedirect(redirectUrl + "?" + parameter);
            }
            httpServletResponse.sendRedirect(redirectUrl);
        }else {
            httpServletResponse.sendRedirect("/index");
        }
    }
}
