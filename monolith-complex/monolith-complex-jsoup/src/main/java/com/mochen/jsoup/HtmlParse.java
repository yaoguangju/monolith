package com.mochen.jsoup;

import com.mochen.jsoup.entity.xdo.SchoolDO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParse {



    public static void main(String[] args) throws Exception {
        parseSchool();
    }

    public static void parseSchool() throws IOException {
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
        System.out.println(schoolDOList);
        // 5.返回 list

    }

}
