package club.banyuan.studyweb.service;

import club.banyuan.studyweb.entity.DiscussPost;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DiscussPostService {
    PageInfo<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit);

    DiscussPost selectDiscussPostById(Integer id);
}
