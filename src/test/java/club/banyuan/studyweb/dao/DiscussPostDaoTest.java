package club.banyuan.studyweb.dao;

import club.banyuan.studyweb.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiscussPostDaoTest {

    @Autowired
    DiscussPostDao discussPostDao;

    @Test
    void selectDiscussPosts() {
        List<DiscussPost> list=discussPostDao.selectDiscussPosts(131);
        System.out.println(list);
    }

    @Test
    void selectDiscussPostById(){
        System.out.println(discussPostDao.selectDiscussPostById(109));
    }
}