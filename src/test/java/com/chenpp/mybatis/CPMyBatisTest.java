package com.chenpp.mybatis;

import com.chenpp.mybatis.mapper.Blog;
import com.chenpp.mybatis.mapper.BlogMapper;
import com.chenpp.mybatis.session.SqlSession;
import com.chenpp.mybatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public class CPMyBatisTest {

    @Test
    public void testSelect() throws Exception {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.build().openSession();
        // 获取MapperProxy代理
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = mapper.selectBlogById(1);

        System.out.println("第一次查询: " + blog);
        System.out.println();
        blog = mapper.selectBlogById(1);
        System.out.println("第二次查询: " + blog);

        List<Blog> blogs = mapper.seletAllBlog();
        System.out.println("查询全部blogs:"+blogs);
    }

    @Test
    public void testInsert() throws Exception {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.build().openSession();
        Blog blog = new Blog();
        blog.setBid(9);
        blog.setAuthorId(3);
        blog.setName("测试添加博客");
        sqlSession.insert("com.chenpp.mybatis.mapper.BlogMapper.insertBlog",new Object[]{9,"测试新增博客",3});
    }

    @Test
    public void testUpdate() throws Exception {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.build().openSession();
        Blog blog = new Blog();
        blog.setBid(1);
        blog.setAuthorId(3);
        blog.setName("测试修改博客");
        sqlSession.update("com.chenpp.mybatis.mapper.BlogMapper.updateBlog",new Object[]{"测试修改博客",3});
    }

    @Test
    public void testDelete() throws Exception {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.build().openSession();
        sqlSession.delete("com.chenpp.mybatis.mapper.BlogMapper.deleteBlog",9);
    }

}
