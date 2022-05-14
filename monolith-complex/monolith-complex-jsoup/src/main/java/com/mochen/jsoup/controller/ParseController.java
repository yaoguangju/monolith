package com.mochen.jsoup.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.core.common.xbo.Result;
import com.mochen.jsoup.entity.xdo.SchoolDO;
import com.mochen.jsoup.entity.xdo.SubjectDO;
import com.mochen.jsoup.mapper.SchoolMapper;
import com.mochen.jsoup.mapper.SubjectMapper;
import com.mochen.jsoup.service.ISchoolService;
import com.mochen.jsoup.service.ISubjectService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ParseController {

    @Resource
    private ISchoolService schoolService;

    @Resource
    private ISubjectService subjectService;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/parseSchool")
    public Result parseSchool() throws IOException {
        /// 使用前需要联网
        // 请求url
        String url = "https://xkkm.sdzk.cn/web/xx.html";
        // 1.解析网页(jsoup 解析返回的对象是浏览器Document对象)
        Document document = Jsoup.parse(new URL(url), 30000);
        // 使用document可以使用在js对document的所有操作
        // 2.获取元素（通过id）

        Elements scrollTbody = document.getElementsByClass("scrollTbody");
        Element element = scrollTbody.get(0);
        Elements trs = element.getElementsByTag("tr");

        List<SchoolDO> schoolDOList = new ArrayList<>();
        for (Element tr : trs) {
            String schoolId = tr.select("td").get(0).text();
            String city = tr.select("td").get(1).text();
            String code = tr.select("td").get(2).text();
            String school = tr.select("td").get(3).text();
            String officialWebsite = tr.select("td").get(5).text();
            String dm = tr.getElementsByAttributeValue("name","dm").attr("value");
            String mc = tr.getElementsByAttributeValue("name","mc").attr("value");
            String yzm = tr.getElementsByAttributeValue("name","yzm").attr("value");
            String nf = tr.getElementsByAttributeValue("name","nf").attr("value");
            SchoolDO schoolDO = new SchoolDO();
            schoolDO.setSchoolId(schoolId);
            schoolDO.setCity(city);
            schoolDO.setCode(code);
            schoolDO.setSchool(school);
            schoolDO.setOfficialWebsite(officialWebsite);
            schoolDO.setDm(dm);
            schoolDO.setMc(mc);
            schoolDO.setYzm(yzm);
            schoolDO.setNf(nf);
            schoolDOList.add(schoolDO);
        }
        schoolService.saveBatch(schoolDOList);
        return new Result();
    }



    @GetMapping("/parseSelectBySubject")
    public Result parseSelectBySubject(){

        Set<String> collect = subjectMapper.selectList(null).stream().map(SubjectDO::getSchool).collect(Collectors.toSet());

        List<SchoolDO> schoolDOList = schoolMapper.selectList(new QueryWrapper<SchoolDO>().notIn("school",collect));
        schoolDOList.forEach(schoolDO ->{

//            try {
//                Thread.sleep(RandomUtil.randomInt(3000, 5000));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            // 设置 HTTP Header
            HttpHeaders headers = new HttpHeaders();

            //封装参数 不要替换为Map与HashMap否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("dm", schoolDO.getDm());
            params.add("mc", schoolDO.getMc());
            params.add("yzm", schoolDO.getYzm());
            params.add("nf", schoolDO.getNf());

            //设置请求 Entity
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<String> messageResponse = restTemplate.exchange(
                    "https://xkkm.sdzk.cn/xkkm/queryXxInfor",
                    HttpMethod.POST,
                    entity,
                    String.class);
            String body = messageResponse.getBody();
            assert body != null;
            Document document = Jsoup.parse(body);
            Element ccc = document.getElementById("ccc");
            Elements trs = ccc.getElementsByTag("tr");
            List<SubjectDO> subjectDOList = new ArrayList<>();
            for (Element tr : trs) {
                String subjectId = tr.select("td").get(0).text();
                String level = tr.select("td").get(1).text();
                String professionClass = tr.select("td").get(2).text();
                String scope = tr.select("td").get(3).text();
                String profession = tr.select("td").get(4).text();
                SubjectDO subjectDO = new SubjectDO();
                subjectDO.setSubjectId(subjectId);
                subjectDO.setSchool(schoolDO.getSchool());
                subjectDO.setSchoolCode(schoolDO.getCode());
                subjectDO.setLevel(level);
                subjectDO.setProfessionClass(professionClass);
                subjectDO.setScope(scope);
                subjectDO.setProfession(profession);
                subjectDOList.add(subjectDO);
            }
            subjectService.saveBatch(subjectDOList);
        });

        return new Result();
    }

    @GetMapping("/getSchoolCount")
    public Result getSchoolCount(){
        List<SubjectDO> subjectDOList = subjectMapper.selectList(null);

        Set<String> collect = subjectDOList.stream().map(SubjectDO::getSchool).collect(Collectors.toSet());
        System.out.println(collect.size());
        return new Result();
    }


}
