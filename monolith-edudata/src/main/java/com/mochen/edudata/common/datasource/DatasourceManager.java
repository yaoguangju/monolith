package com.mochen.edudata.common.datasource;


import org.springframework.stereotype.Component;

@Component
public class DatasourceManager {
    public static String getDatasource(String year) {
        String dsKey;
        switch(year){
            case "2019":
                dsKey =  "edudata2019";
                break;
            case "2020":
                dsKey =  "edudata2020";
                break;
            case "2021":
                dsKey =  "edudata2021";
                break;
            default:
                dsKey =  "edudata";
        }
        return dsKey;
    }
}
