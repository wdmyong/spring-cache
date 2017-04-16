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
        @Result(property = "passwd", column = "passwd"),
        @Result(property = "mobile", column = "mobile"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "modifyTime", column = "modify_time")
    })
    public User getById(@Param("id") Integer id);

    @Insert("INSERT INTO user (name, account, passwd, mobile, create_time, modify_time) VALUES "
            + "(#{name}, #{account}, #{passwd}, #{mobile}, #{createTime}, #{modifyTime})")
    public void insert(User user);

    @Update("UPDATE user SET name = #{name}, mobile = #{mobile}, passwd = #{passwd}, "
            + "modify_time = #{modifyTime} WHERE id = #{id}")
    public void update(User user);
}
