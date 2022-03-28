package com.mochen.complex.pdf.controller;


import com.itextpdf.text.pdf.PdfReader;
import com.mochen.complex.web.entity.vo.SchoolStudentVO;
import com.mochen.complex.web.service.IComplexWebStudentService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/complex-web")
public class PdfController {

    @GetMapping("/getPdfPage")
    public Result getPdfPage() throws IOException {
        File file = new File("C:\\Users\\姚广举\\Desktop\\5.6对接云印[21-05-003] 内文对接云印 封皮乐迪制作\\5.6对接云印[21-05-003] 内文对接云印 封皮乐迪制作\\[21-05-003]-1-中药一课-322p【290本胶装】5.6发货 多个210x285\\[21-05-003] -1-中药一课-322p【290本胶装】5.6发货 多个210x285.pdf");
        PdfReader pdfReader = new PdfReader(new FileInputStream(file));
        int pages = pdfReader.getNumberOfPages();
        System.out.println("pdf文件的总页数为:" + pages);
        return Result.success();
    }

}

