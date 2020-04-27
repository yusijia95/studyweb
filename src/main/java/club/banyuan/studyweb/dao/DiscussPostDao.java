package club.banyuan.studyweb.dao;


import club.banyuan.studyweb.entity.DiscussPost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussPostDao {
    List<DiscussPost> selectDiscussPosts(Integer userId);

    DiscussPost selectDiscussPostById(Integer Id);
}
