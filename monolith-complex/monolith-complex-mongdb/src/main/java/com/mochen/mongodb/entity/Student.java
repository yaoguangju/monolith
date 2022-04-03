package com.mochen.mongodb.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Document("student")
public class Student {

    @Id
    private Long id;
    // 附别名
    @Field("username")
    private String name;
    private Integer age;

}
