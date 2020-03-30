package club.banyuan.studyweb.form;

import club.banyuan.studyweb.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Size;

public class RegisterForm {

    @Size(min = 1,message ="用户名不能为空")
    private String username;

    @Size(min = 8,message = "密码不能少于8位")
    private String password;

    @Size(min = 1,message = "邮箱不能为空")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password =password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toUser(){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }
}
