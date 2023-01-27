package com.hechi.niumall.vo;

import lombok.Data;

@Data
//未读消息
public class NiuinfoVo {
    //发送者id
    private Long fromid;
    //发送者昵称
    private String fromname;
    //   头像
    private String url;

    private String lastInfo;
    //  未读数量
    private Long count;
}
