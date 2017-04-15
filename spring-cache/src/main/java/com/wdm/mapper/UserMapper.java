package com.wdm.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.wdm.model.User;

public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "account", column = "account"),
        @Result(property = "passwd", column = "passwd"),
        @Result(property = "mobile", column = "mobile")
    })
    public User getById(@Param("id") Integer id);
}
