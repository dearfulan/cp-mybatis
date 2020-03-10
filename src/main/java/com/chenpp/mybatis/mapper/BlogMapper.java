package com.chenpp.mybatis.mapper;

import com.chenpp.mybatis.annoation.Select;
import com.chenpp.mybatis.domain.Blog;

import java.util.List;

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
