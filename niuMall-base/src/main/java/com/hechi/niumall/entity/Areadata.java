package com.hechi.niumall.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * (Areadata)表实体类
 *
 * @author ccx
 * @since 2022-08-03 23:54:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("areadata")
public class Areadata  {

    //城市编号，三级用int类型，四级用long类型；
    private Long id;
    //上级ID
    private Long pid;
    //层级深度；0：省，1：市，2：区，3：镇
    private Integer deep;
    //如：武汉，为数据源原始名称精简过后的短名称，和ext_name字段值的前半部分完全相同，如需完整名称请使用ext_name字段
    private String name;
    //为name的拼音前缀
    private String pinyinPrefix;
    //为name的完整拼音
    private String pinyin;
    //数据源原始的编号
    private String extId;
    //如：武汉市，为数据源原始的完整名称
    private String extName;



}
