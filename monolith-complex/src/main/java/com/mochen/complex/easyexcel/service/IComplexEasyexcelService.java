package com.mochen.complex.easyexcel.service;

import com.mochen.complex.easyexcel.entity.xdo.ComplexEasyexcelDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
public interface IComplexEasyexcelService extends IService<ComplexEasyexcelDO> {

    void upload(MultipartFile file) throws IOException;

    void download(HttpServletResponse response) throws IOException;
}
