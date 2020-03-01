package com.chenpp.mybatis.mapper;

import com.chenpp.mybatis.annoation.Entity;
import com.chenpp.mybatis.annoation.Select;

import java.util.List;

@Entity(Blog.class)
public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    @Select("select * from blog where bid = ?")
    public Blog selectBlogById(Integer bid);

    @Select("select * from blog ")
    public List<Blog> seletAllBlog();

}
