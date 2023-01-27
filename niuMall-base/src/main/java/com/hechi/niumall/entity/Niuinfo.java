package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * (Niuinfo)表实体类
 *
 * @author ccx
 * @since 2022-11-03 17:20:33
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("niuinfo")
public class Niuinfo  implements Serializable {
    //消息id@TableId
    private Long id;
    //发送者昵称
    private String fromname;
    //发送者id
    private Long fromid;
    //接收者昵称
    private String toname;
    //接收者id
    private Long toid;
    //消息主题
    private String content;
    //消息类型
    private Integer type;
    //是否已读 0 未读，1 已读
    private Integer flag;
    //消息是否被删除
    private Integer deleted;
    //发送时间
    private Date sendtime;



}
