package club.banyuan.studyweb.dao;

import club.banyuan.studyweb.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    void selectById() {
        User user=userDao.selectById(1);
        System.out.println(user);
    }

    @Test
    void selectByUsername() {
        User user=userDao.selectByUsername("SYSTEM");
        System.out.println(user);
    }

    @Test
    void selectByEmail() {
        User user=userDao.selectByEmail("nowcoder1@sina.com");
        System.out.println(user);
    }

    @Test
    void insertUser() {
        User user= new User();
        user.setUsername("俞思笳");
        Integer count=userDao.insertUser(user);
        System.out.println(user.getId());
    }

    @Test
    void updateStatus() {
        Integer s=userDao.updateStatus(1,0);
        System.out.println(s);
    }

    @Test
    void updateHeaderUrl() {

    }

    @Test
    void updatePassword() {
        Integer s=userDao.updatePassword(1,"system");
        System.out.println(s);
    }
}