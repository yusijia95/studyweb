package club.banyuan.studyweb.controller;

import club.banyuan.studyweb.annotation.LoggerAnnotation;
import club.banyuan.studyweb.entity.User;
import club.banyuan.studyweb.form.RegisterForm;
import club.banyuan.studyweb.service.UserService;
import club.banyuan.studyweb.util.StudywebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegisterController implements StudywebConstant {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerGet() {
        return "/site/register";
    }

    @LoggerAnnotation
    @PostMapping("/register")
    public String registerPost(@Valid RegisterForm registerForm, BindingResult result,
                               Model model) {
        User user = registerForm.toUser();
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            if (result.getFieldError("username") != null) {
                model.addAttribute("usernameMes", result.getFieldError("username").getDefaultMessage());
            }
            if(result.getFieldError("password")!=null) {
                model.addAttribute("passwordMes", result.getFieldError("password").getDefaultMessage());
            }
            if(result.getFieldError("email")!=null) {
                model.addAttribute("emailMes", result.getFieldError("email").getDefaultMessage());
            }
            return "/site/register";
        }
        Map map = userService.insertUser(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("message", "您的账号已注册成功，我们已发送一份激活邮件至您的邮箱，请您尽快激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMes", map.get("usernameMes"));
            model.addAttribute("emailMes", map.get("emailMes"));
            return "/site/register";
        }
    }

    @GetMapping("/activationCode/{userId}/{activationCode}")
    public String activationGet(@PathVariable("userId") Integer userId,
                                @PathVariable("activationCode") String activationCode,
                                Model model) {
        Integer result = userService.activation(userId, activationCode);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("message", "您的账号已激活成功，可以正常使用了");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_FAILURE) {
            model.addAttribute("message", "您的账号激活失败，您提供的激活码不正确");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("message", "无效操作，您的账号已激活过了");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

}
