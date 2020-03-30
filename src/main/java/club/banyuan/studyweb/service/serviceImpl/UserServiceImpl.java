package club.banyuan.studyweb.service.serviceImpl;

import club.banyuan.studyweb.dao.UserDao;
import club.banyuan.studyweb.entity.User;
import club.banyuan.studyweb.service.UserService;
import club.banyuan.studyweb.util.StudywebConstant;
import club.banyuan.studyweb.util.MailClient;
import club.banyuan.studyweb.util.StudyWebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, StudywebConstant, UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    MailClient mailClient;

    @Value("${server.servlet.context-path}")
    String ContextPath;

    @Value("${studyweb.path.domain}")
    String domian;

    @Override
    public User selectById(Integer id) {
        return userDao.selectById(id);
    }

    @Override
    public User selectByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    @Override
    public User selectByEmail(String email) {
        return userDao.selectByEmail(email);
    }

    @Override
    public Map insertUser(User user) {
        Map map=new HashMap();
        if (user == null) {
            throw new UsernameNotFoundException("用户信息不存在");
        }
        User sqlUser = userDao.selectByUsername(user.getUsername());
        if (sqlUser != null) {
            map.put("usernameMes","该用户名以存在");
            return map;
        }
        sqlUser = userDao.selectByEmail(user.getEmail());
        if (sqlUser != null) {
            map.put("emailMes","该邮箱已被注册");
            return map;
        }

        user.setCreateTime(new Date());
        user.setStatus(0);
        user.setType(0);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setActivationCode(StudyWebUtil.generateUUID());
        user.setHeaderUrl("http://images.nowcoder.com/head/1t.png");
        userDao.insertUser(user);

        Context context=new Context();
        context.setVariable("email",user.getEmail());
        context.setVariable("url",domian+ContextPath+"/activationCode/"+user.getId()+"/"+user.getActivationCode());
        String content=templateEngine.process("/mail/activation",context);
        mailClient.sendEmail(user.getEmail(),"激活邮件",content);

        return map;
    }

    @Override
    public Integer updateStatus(Integer id, Integer status) {
        return null;
    }

    @Override
    public Integer updateHeaderUrl(Integer id, String headerUrl) {
        return null;
    }

    @Override
    public Integer updatePassword(Integer id, String password) {
        return null;
    }

    @Override
    public Integer activation(Integer userId, String code) {
        User user=userDao.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userDao.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.selectByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("该用户不存在");
        }
        return user;
    }
}
