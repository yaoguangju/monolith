package com.mochen.thread.mapper;

import com.mochen.thread.entity.xdo.ThreadLockDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-02
 */
@Mapper
public interface ThreadLockMapper extends BaseMapper<ThreadLockDO> {

}
