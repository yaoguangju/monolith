package com.mochen.edudata.common.datasource;

import org.springframework.stereotype.Component;

@Component
public class DynamicDatasourceManager {

    public String getDataSource(String analysisNo){

        if(analysisNo.contains("2019")){
            return "edudata2019";
        }
        if(analysisNo.contains("2020")){
            return "edudata2020";
        }
        if(analysisNo.contains("2021")){
            return "edudata2021";
        }
        return "edudata2019";
    }


}
