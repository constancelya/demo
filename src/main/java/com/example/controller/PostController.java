package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.vo.CommentVo;
import com.example.vo.PostVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController extends BaseController{

    @GetMapping("/category/{id:\\d*}")
    public String category(@PathVariable(name = "id") Long id){
        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);


        req.setAttribute("currentCategoryId", id);
        req.setAttribute("pn", pn);

        return "post/category";
    }

    @GetMapping("/post/{id:\\d*}")
    public String detail(@PathVariable(name = "id") Long id){

        PostVo vo = postService.selectOnePost(new QueryWrapper<Post>().eq("p.id", id));
        Assert.notNull(vo, "文章已删除");

        postService.putViewCount(vo);


        //1分页，2文章id， 3用户id，排序
        IPage<CommentVo> results = commentService.paging(getPage(), vo.getId(), null, "created");

        req.setAttribute("currentCategoryId", vo.getCategoryId());

        req.setAttribute("post", vo);
        req.setAttribute("pageData", results);

        return "post/detail";
    }
}
