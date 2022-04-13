package com.mochen.validation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.validation.entity.xdo.ComplexValidationDO;
import com.mochen.validation.mapper.ComplexValidationMapper;
import com.mochen.validation.service.IComplexValidationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 绑定学生 服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-03
 */
@Service
public class ComplexValidationServiceImpl extends ServiceImpl<ComplexValidationMapper, ComplexValidationDO> implements IComplexValidationService {

}
