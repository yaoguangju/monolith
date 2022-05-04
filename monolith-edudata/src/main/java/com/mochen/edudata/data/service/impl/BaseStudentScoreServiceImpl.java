package com.mochen.edudata.data.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.edudata.common.datasource.DatasourceManager;
import com.mochen.edudata.data.entity.excel.EasyExcelData;
import com.mochen.edudata.data.entity.xdo.BaseStudentDO;
import com.mochen.edudata.data.entity.xdo.BaseStudentScoreDO;
import com.mochen.edudata.data.mapper.BaseStudentMapper;
import com.mochen.edudata.data.mapper.BaseStudentScoreMapper;
import com.mochen.edudata.data.service.IBaseStudentScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-18
 */
@Service
@Slf4j
public class BaseStudentScoreServiceImpl extends ServiceImpl<BaseStudentScoreMapper, BaseStudentScoreDO> implements IBaseStudentScoreService {

    @Resource
    private IBaseStudentScoreService baseStudentScoreService;

    @Resource
    private BaseStudentMapper baseStudentMapper;

    @Resource
    private Executor asyncServiceExecutor;

    @Override
    public void cacheStudentScore() throws InterruptedException {
        DynamicDataSourceContextHolder.push(DatasourceManager.getDatasource("2019"));
        // 封装获取studentId的方法
        Map<String, Long> map = baseStudentMapper.selectList(
                new QueryWrapper<BaseStudentDO>().select("id","analysis_no"))
                .stream()
                .collect(Collectors.toMap(BaseStudentDO::getAnalysisNo, BaseStudentDO::getId));

        ExecutorService pool = Executors.newFixedThreadPool(9);
        CountDownLatch countDownLatch = new CountDownLatch(9);

        List<String> stringList = new ArrayList<>(9);
//        String basePath = "D:\\Cache\\百度网盘\\BaiduNetdiskWorkspace\\2021年7月份高二期末考试数据\\01 数据\\单科原始成绩\\";
        String basePath = "D:\\Cache\\百度网盘同步空间\\同步空间\\2021年7月份高二期末考试数据\\01 数据\\单科原始成绩\\";
        stringList.add(0, basePath + "单科原始成绩_高中语文.xls");
        stringList.add(1, basePath + "单科原始成绩_高中数学.xls");
        stringList.add(2, basePath + "单科原始成绩_高中英语.xls");
        stringList.add(3, basePath + "单科原始成绩_高中物理.xls");
        stringList.add(4, basePath + "单科原始成绩_高中化学.xls");
        stringList.add(5, basePath + "单科原始成绩_高中生物.xls");
        stringList.add(6, basePath + "单科原始成绩_高中政治.xls");
        stringList.add(7, basePath + "单科原始成绩_高中历史.xls");
        stringList.add(8, basePath + "单科原始成绩_高中地理.xls");

        for (int i = 0; i < 9; i++) {

            int j = i;
            CompletableFuture.runAsync(() -> {
                //获取上传文件输入流
                EasyExcel.read(stringList.get(j), EasyExcelData.class, new ReadListener<EasyExcelData>() {

                    /**
                     * 单次缓存的数据量
                     */
                    public static final int BATCH_COUNT = 1000;

                    /**
                     *临时存储
                     */
                    private List<BaseStudentScoreDO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    @Override
                    public void invoke(EasyExcelData easyExcelData, AnalysisContext analysisContext) {

//                              log.info("解析到一条数据:{}", JSON.toJSONString(easyExcelData));
                        BaseStudentScoreDO baseStudentScoreDO = new BaseStudentScoreDO();
                        BeanUtils.copyProperties(easyExcelData,baseStudentScoreDO);
                        baseStudentScoreDO.setSubjectId(j + 1);
                        baseStudentScoreDO.setExamId(1L);
                        baseStudentScoreDO.setStudentId(map.get(easyExcelData.getAnalysisNo()));
                        cachedDataList.add(baseStudentScoreDO);
                        if (cachedDataList.size() >= BATCH_COUNT) {
                            saveData();
                            // 存储完成清理 list
                            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                        }
                    }

                    /**
                     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
                     */
                    @Override
                    public void onException(Exception exception, AnalysisContext context) {
                        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
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
                        DynamicDataSourceContextHolder.push(DatasourceManager.getDatasource("2019"));
                        baseStudentScoreService.saveBatch(cachedDataList);
                        log.info("存储数据库成功！");
                    }
                }).sheet().headRowNumber(4).doRead();
                // 配置sheet，配置从第几行开始读

            },asyncServiceExecutor).join();
        }
    }
}
