package com.hechi.niumall.entity;

import lombok.Data;

/**
 * 邮件参数实体类
 * @author 
 *
 */
@Data
public class EmailParam {
	private String itemName;//产品名称
	private String stuName;//学生姓名
	private String updateContent;//变更操作
	private String updatePerson;//操作人员
	private String updateDate;//操作时间
	private String remarks;//备注
}