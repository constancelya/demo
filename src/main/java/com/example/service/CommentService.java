package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.CommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuanO
 * @since 2021-08-31
 */
public interface CommentService extends IService<Comment> {

    IPage<CommentVo> paging(Page page, Long postId, Long userId, String order);
}
