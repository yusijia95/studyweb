package club.banyuan.studyweb.service.serviceImpl;

import club.banyuan.studyweb.dao.DiscussPostDao;
import club.banyuan.studyweb.entity.DiscussPost;
import club.banyuan.studyweb.service.DiscussPostService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    DiscussPostDao discussPostDao;

    @Override
    public PageInfo<DiscussPost> selectDiscussPosts(Integer userId,Integer offset,Integer limit) {
        PageHelper.startPage(offset,limit);
        PageInfo<DiscussPost> pageInfo=new PageInfo<>(discussPostDao.selectDiscussPosts(userId));
        return pageInfo;
    }
}
