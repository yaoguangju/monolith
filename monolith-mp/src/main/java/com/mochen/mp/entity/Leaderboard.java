package com.mochen.mp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Leaderboard {
    private Integer studentId;
    private String name;
    private Integer poetry;
}
