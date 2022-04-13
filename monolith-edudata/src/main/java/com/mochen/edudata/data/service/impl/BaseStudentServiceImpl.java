package com.mochen.edudata.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.edudata.data.entity.xdo.BaseStudentDO;
import com.mochen.edudata.data.mapper.BaseStudentMapper;
import com.mochen.edudata.data.service.IBaseStudentService;
import com.mochen.edudata.major.entity.xdo.UserDO;
import com.mochen.edudata.major.mapper.UserMapper;
import com.mochen.redis.common.manager.RedisManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-18
 */
@Service
public class BaseStudentServiceImpl extends ServiceImpl<BaseStudentMapper, BaseStudentDO> implements IBaseStudentService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private UserMapper userMapper;

    @Override
    public void saveRedisDate() {
        List<UserDO> userDOList = userMapper.selectList(new QueryWrapper<UserDO>().eq("year", 2019));
        userDOList.forEach(userDO -> {
            redisManager.setCacheHashMapValue("student:2019",userDO.getAnalysisNo(),userDO.getId());
        });
    }
}
