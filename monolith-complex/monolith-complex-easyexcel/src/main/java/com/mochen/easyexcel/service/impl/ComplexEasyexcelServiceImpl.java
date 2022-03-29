package com.mochen.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.easyexcel.entity.excel.EasyExcelData;
import com.mochen.easyexcel.entity.xdo.ComplexEasyexcelDO;
import com.mochen.easyexcel.mapper.ComplexEasyexcelMapper;
import com.mochen.easyexcel.service.IComplexEasyexcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Slf4j
@Service
public class ComplexEasyexcelServiceImpl extends ServiceImpl<ComplexEasyexcelMapper, ComplexEasyexcelDO> implements IComplexEasyexcelService {

    @Resource
    private IComplexEasyexcelService easyExcelService;

    @Override
    public void upload(MultipartFile file) throws IOException {
        //获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream, EasyExcelData.class, new ReadListener<EasyExcelData>() {

            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 1000;

            /**
             *临时存储
             */
            private List<ComplexEasyexcelDO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(EasyExcelData easyExcelData, AnalysisContext analysisContext) {

                log.info("解析到一条数据:{}", JSON.toJSONString(easyExcelData));
                ComplexEasyexcelDO complexEasyexcelDO = new ComplexEasyexcelDO();
                BeanUtils.copyProperties(easyExcelData,complexEasyexcelDO);
                cachedDataList.add(complexEasyexcelDO);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}数据", cachedDataList);
                easyExcelService.saveBatch(cachedDataList);
                log.info("存储数据库成功！");
            }
        }).sheet().headRowNumber(5).doRead();
        // 配置sheet，配置从第几行开始读
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        List<ComplexEasyexcelDO> easyExcelDOList = this.list(new QueryWrapper<ComplexEasyexcelDO>()
                .select("analysis_no", "name", "school", "class_id", "score", "class_rank", "school_rank", "total_rank"));
        String fileName = System.currentTimeMillis() + ".xlsx";
        //设置response header
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"utf-8"));
        // 文件输出
        EasyExcel.write(response.getOutputStream(), EasyExcelData.class).sheet("sheet1").doWrite(easyExcelDOList);

    }


}
