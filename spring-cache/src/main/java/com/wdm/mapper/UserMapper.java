package com.wdm.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wdm.model.User;

/*
 * @author wdmyong
 * 20170415
 */
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "account", column = "account"),
        @Result(property = "password", column = "password"),
        @Result(property = "mobile", column = "mobile"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    User getById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE account = #{account} AND password = #{password}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "account", column = "account"),
            @Result(property = "password", column = "password"),
            @Result(property = "mobile", column = "mobile"),
            @Result(property = "status", column = "status"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    User getByAccountPassword(@Param("account") String account, @Param("password") String password);

    @Insert("INSERT INTO user (name, account, password, mobile, status, create_time, update_time) VALUES "
            + "(#{name}, #{account}, #{password}, #{mobile}, #{status}, #{createTime}, #{updateTime})")
    void insert(User user);

    @Update("UPDATE user SET name = #{name}, mobile = #{mobile}, password = #{password}, status = #{status}, "
            + "update_time = #{updateTime} WHERE id = #{id}")
    void update(User user);
}
