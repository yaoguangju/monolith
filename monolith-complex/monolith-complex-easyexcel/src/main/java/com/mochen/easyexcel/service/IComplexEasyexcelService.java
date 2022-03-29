package com.mochen.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mochen.easyexcel.entity.xdo.ComplexEasyexcelDO;
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
