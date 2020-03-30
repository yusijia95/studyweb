package club.banyuan.studyweb.service;

import club.banyuan.studyweb.entity.User;

import java.util.Map;

public interface UserService {
    User selectById(Integer id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    Map insertUser(User user);

    Integer updateStatus(Integer id,Integer status);

    Integer updateHeaderUrl(Integer id,String headerUrl);

    Integer updatePassword(Integer id,String password);

    Integer activation(Integer userId,String code);
}
