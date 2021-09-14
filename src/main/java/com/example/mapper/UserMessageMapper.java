package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vo.UserMessageVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuanO
 * @since 2021-08-31
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    IPage<UserMessageVo> selectMessage(Page page,@Param(Constants.WRAPPER) QueryWrapper<UserMessage> wrapper);

}
