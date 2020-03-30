package club.banyuan.studyweb.controller;

import club.banyuan.studyweb.annotation.LoggerAnnotation;
import club.banyuan.studyweb.entity.DiscussPost;
import club.banyuan.studyweb.entity.User;
import club.banyuan.studyweb.service.DiscussPostService;
import club.banyuan.studyweb.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class HomeController {

    private Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    UserService userService;

    @LoggerAnnotation
    @GetMapping("/index")
    public String homeGet(@RequestParam("offset") Optional<Integer> offset,
                          @RequestParam("limit") Optional<Integer> limit,
                          Model model){
        Object object= SecurityContextHolder.getContext().getAuthentication();
        List<Map<String, Object>> list=new ArrayList<>();
        PageInfo discussPosts=discussPostService.selectDiscussPosts(0,offset.orElse(1),limit.orElse(10));
        List<DiscussPost> listBefore=(List<DiscussPost>) discussPosts.getList();
        for (DiscussPost discusspost:listBefore) {
            User user=userService.selectById(discusspost.getUserId());
            Map<String,Object> map=new HashMap<>();
            map.put("post",discusspost);
            map.put("user",user);
            list.add(map);
        }
        discussPosts.setList(list);
        model.addAttribute("DISCUSSPOSTS",discussPosts);
        logger.info("model {}",model);
        return "index";
    }
}
