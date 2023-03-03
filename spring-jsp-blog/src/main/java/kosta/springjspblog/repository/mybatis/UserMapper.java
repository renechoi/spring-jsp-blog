package kosta.springjspblog.repository.mybatis;

import kosta.springjspblog.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int save(User user);
    List<User> findAll();
    User findById(String id);
    User findByObject(User user);
}
