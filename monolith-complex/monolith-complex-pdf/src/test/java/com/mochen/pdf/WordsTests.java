package com.mochen.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class WordsTests {

    @Test
    public void wordToPdfByJacob() throws IOException {
        String wordPath = "E:\\Work\\云印下单助手\\API接口说明文档-数印.doc";
        String pdfPath = "E:\\Work\\云印下单助手\\API接口说明文档-数印.pdf";

        try {
            ActiveXComponent app = new ActiveXComponent("KWPS.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch dispatch = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.call(dispatch, "Open", wordPath).toDispatch();
            Dispatch.call(doc, "SaveAs", pdfPath, 17);
            Dispatch.call(doc, "Close", false);
            app.invoke("Quit", new Variant[]{});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //结束后关闭进程
        ComThread.Release();
        File file = new File(pdfPath);
        PdfReader pdfReader = new PdfReader(new FileInputStream(file));
        int pages = pdfReader.getNumberOfPages();
        System.out.println("pdf文件的总页数为:" + pages);
    }
}
