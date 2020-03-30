package club.banyuan.studyweb.util;

import club.banyuan.studyweb.StudywebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailClientTest {

    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Test
    void sendTextEmail() {
        mailClient.sendEmail("18974760843@163.com","text邮件","测试");
    }

    @Test
    void sendHtmlEmail() {
        Context context=new Context();
        context.setVariable("username","yusijia");
        String content=templateEngine.process("/mail/test.html",context);
        mailClient.sendEmail("18974760843@163.com","text邮件",content);
    }


}