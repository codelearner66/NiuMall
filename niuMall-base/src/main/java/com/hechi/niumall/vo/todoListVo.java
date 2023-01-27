package com.hechi.niumall.vo;

import lombok.Data;

import java.util.Date;

@Data
public class todoListVo {
    private Long id;
    private String title;
    private Boolean status;
    private Date date;
}
