package com.hechi.niumall.service;

import com.hechi.niumall.entity.EditotrNotification;
import com.hechi.niumall.result.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EditorNotificationService {
    /**
     * 添加新的通知
     * @param editotr 通知实体
     * @return
     */
    ResponseResult addeditorNotification(EditotrNotification editotr);

    /**
     * 移除通知
     * @param editor 通知实体
     * @return
     */
    ResponseResult removeeditorNotification(EditotrNotification editor);
    ResponseResult removeeditorNotification(EditotrNotification ...editor);
    /**
     * 修改通知
     * @param editor 通知实体
     * @return
     */
    ResponseResult modifyNotification(EditotrNotification editor);

    ResponseResult modifyNotification(EditotrNotification ...editor);

    /**
     * 查询通知
     * @return
     */
    ResponseResult getModifyNotification();
    /**
     * 添加通知图片
     * @param file 图片          文件
     * @return
     */
    ResponseResult addEditorImages(MultipartFile file) throws IOException;

    /**
     * 删除通知图片
     * @param fileName
     * @return
     */
    ResponseResult deleteEditorImages(String fileName);
}
