package com.mochen.pdf.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.itextpdf.text.pdf.PdfReader;
import com.mochen.core.common.xbo.Result;
import com.mochen.pdf.common.contanst.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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


    @PostMapping("/getWordPage")
    public Result getWordPage(@RequestParam("file") MultipartFile file) throws IOException {
        int pageCount = 0;
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (XWPFDocument docx = new XWPFDocument(file.getInputStream())) {
            pageCount = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();//总页数
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("pdf文件的总页数为:" + pageCount);
        return Result.success(pageCount);
    }

    @PostMapping("/uploadFileToOSS")
    public Result uploadFileToOSS(@RequestParam("file") MultipartFile file) throws IOException {
        String path = "";
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
            path = "https://" + CommonConstant.ALI_YUN_OSS_BUCKET_NAME + "."+ CommonConstant.ALI_YUN_OSS_END_POINT + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(path);
    }

    @PostMapping("/zipFile")
    public Result zipFile() throws IOException {
        //被压缩的文件夹
        String sourceFile = "C:\\Users\\姚广举\\Desktop\\12312_20220402094041\\";
        //压缩结果输出，即压缩包
        FileOutputStream fos = new FileOutputStream("C:\\Users\\姚广举\\Desktop\\数印材料卡.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);
        //递归压缩文件夹
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        //关闭输出流
        zipOut.close();
        fos.close();
        return Result.success();
    }

    @PostMapping("/fileUpload")
    public boolean fileUpload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        if (file.getSize() == 0) {
            return false;
        }

//        System.err.println("文件是否为空 ： " + file.isEmpty());
//        System.err.println("文件的大小为 ：" + file.getSize());
//        System.err.println("文件的媒体类型为 ： " + file.getContentType());
//        System.err.println("文件的名字： " + file.getName());
//        System.err.println("文件的originName为： " + file.getOriginalFilename());

        File newFile = new File("/www/wwwroot/file/" + file.getOriginalFilename());
        file.transferTo(newFile);
        return true;
    }

    private static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 将fileToZip文件夹及其子目录文件递归压缩到zip文件中
     * @param fileToZip 递归当前处理对象，可能是文件夹，也可能是文件
     * @param fileName fileToZip文件或文件夹名称
     * @param zipOut 压缩文件输出流
     * @throws IOException
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        //不压缩隐藏文件夹
        if (fileToZip.isHidden()) {
            return;
        }
        //判断压缩对象如果是一个文件夹
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                //如果文件夹是以“/”结尾，将文件夹作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                //如果文件夹不是以“/”结尾，将文件夹结尾加上“/”之后作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            //遍历文件夹子目录，进行递归的zipFile
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            //如果当前递归对象是文件夹，加入ZipEntry之后就返回
            return;
        }
        //如果当前的fileToZip不是一个文件夹，是一个文件，将其以字节码形式压缩到压缩包里面
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }


}

