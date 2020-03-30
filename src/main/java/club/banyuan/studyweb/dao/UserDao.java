package club.banyuan.studyweb.dao;

import club.banyuan.studyweb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User selectById(Integer id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    Integer insertUser(User user);

    Integer updateStatus(Integer id,Integer status);

    Integer updateHeaderUrl(Integer id,String headerUrl);

    Integer updatePassword(Integer id,String password);
}
