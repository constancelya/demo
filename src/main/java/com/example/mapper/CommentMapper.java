package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vo.CommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuanO
 * @since 2021-08-31
 */
@Component
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVo> selectComment(Page page, @Param(Constants.WRAPPER) QueryWrapper<Comment> wrapper);
}
