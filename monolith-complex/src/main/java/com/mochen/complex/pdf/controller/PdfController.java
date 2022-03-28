package com.mochen.complex.pdf.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.itextpdf.text.pdf.PdfReader;
import com.mochen.complex.pdf.contanst.CommonConstant;
import com.mochen.complex.web.entity.vo.SchoolStudentVO;
import com.mochen.complex.web.service.IComplexWebStudentService;
import com.mochen.core.common.xbo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

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
@Slf4j
public class PdfController {

    @GetMapping("/getPdfPage")
    public Result getPdfPage() throws IOException {
        File file = new File("C:\\Users\\姚广举\\Desktop\\5.6对接云印[21-05-003] 内文对接云印 封皮乐迪制作\\5.6对接云印[21-05-003] 内文对接云印 封皮乐迪制作\\[21-05-003]-1-中药一课-322p【290本胶装】5.6发货 多个210x285\\[21-05-003] -1-中药一课-322p【290本胶装】5.6发货 多个210x285.pdf");
        PdfReader pdfReader = new PdfReader(new FileInputStream(file));
        int pages = pdfReader.getNumberOfPages();
        System.out.println("pdf文件的总页数为:" + pages);
        return Result.success();
    }

    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            OSS ossClient = new OSSClientBuilder().build(CommonConstant.ALI_YUN_OSS_END_POINT,
                    CommonConstant.ALI_YUN_ACCESS_KEY_ID,
                    CommonConstant.ALI_YUN_ACCESS_KEY_SECRET);
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String fileType = getFileType(Objects.requireNonNull(originalFilename)).toLowerCase();
            // 文件防覆盖
            String uuid = UUID.randomUUID().toString();
            String fileName = "";
            if("jpg".equals(fileType)
                    || "png".equals(fileType)){
                fileName = "image/" + uuid + "." + fileType;
            }else {
                fileName = "file/" + uuid + "." + fileType;
            }
            //第一个参数 bucket名称
            //第二个参数 上传oss文件名称和路径
            //第三个参数，上传输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(CommonConstant.ALI_YUN_OSS_BUCKET_NAME, fileName, file.getInputStream());
            ossClient.putObject(putObjectRequest);
            //关闭OssClient
            ossClient.shutdown();
            //上传阿里云之后的文件路径返回
            log.info("https://" + CommonConstant.ALI_YUN_OSS_BUCKET_NAME + "."+ CommonConstant.ALI_YUN_OSS_END_POINT + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    private static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}

