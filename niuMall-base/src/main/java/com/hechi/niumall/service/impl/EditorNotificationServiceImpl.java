package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.hechi.niumall.entity.EditotrNotification;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.EditorNotificationService;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.utils.TxyunUtils;
import javassist.ClassMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EditorNotificationServiceImpl implements EditorNotificationService {
    @Autowired
    TxyunUtils txyunUtils;
    @Autowired
    RedisCache redisCache;


    private static final String PREFIX = "NOTIFICATION";

    @Override
    @Transactional
    public ResponseResult addeditorNotification(EditotrNotification editor) {
        String key = UUID.randomUUID().toString().replace("-", "");
        Long userId = SecurityUtils.getUserId();
        editor.setId(key);
        editor.setCreateBy(userId);
        editor.setCreateDate(new Date());
        List<EditotrNotification> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList != null) {
            cacheList.add(editor);
        } else {
            cacheList = new ArrayList<>();
            cacheList.add(editor);
        }
        redisCache.deleteObject(PREFIX);
        long l = redisCache.setCacheList(PREFIX, cacheList);
        return l > 0 ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    @Transactional
    public ResponseResult removeeditorNotification(EditotrNotification editor) {
        HashMap<String, EditotrNotification> map = this.getEditotrNotificationMap();
        if (map != null && map.size() > 0) {
            map.remove(editor.getId());
        }
        String images = editor.getImages();
        if (images != null && !"".equals(images)) {
            List<String> list = JSON.parseArray(images, String.class);
            if (list != null && list.size() > 0) {
                for (String fileName : list) {
                    this.deleteEditorImages(fileName);
                }
            }
        }
        return this.saveEditotrNotification(map) <= map.size() ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult removeeditorNotification(EditotrNotification... editor) {
        HashMap<String, EditotrNotification> map = this.getEditotrNotificationMap();
        if (map != null && map.size() > 0) {
            for (EditotrNotification item : editor) {
                EditotrNotification remove = map.remove(item.getId());
                String images = remove.getImages();
                if (images != null && !"".equals(images)) {
                    List<String> list = JSON.parseArray(images, String.class);
                    if (list != null && list.size() > 0) {
                        for (String fileName : list) {
                            this.deleteEditorImages(fileName);
                        }
                    }
                }
            }

        }
        return this.saveEditotrNotification(map) <= map.size() ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult modifyNotification(EditotrNotification... editor) {
        HashMap<String, EditotrNotification> map = this.getEditotrNotificationMap();
        if (map != null && map.size() > 0) {
            for (EditotrNotification item : editor) {
                map.remove(item.getId());
                map.put(item.getId(), item);
            }
        }
        return this.saveEditotrNotification(map) == map.size() ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    @Transactional
    public ResponseResult modifyNotification(EditotrNotification editor) {
        HashMap<String, EditotrNotification> map = this.getEditotrNotificationMap();
        if (map != null && map.size() > 0) {
            map.remove(editor.getId());
            map.put(editor.getId(), editor);
        }
        return this.saveEditotrNotification(map) == map.size() ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult getModifyNotification() {
        List<EditotrNotification> cacheList = redisCache.getCacheList(PREFIX);
        List<EditotrNotification> resoult = new ArrayList<>();
        if (cacheList != null && cacheList.size() > 0) {
            if (SecurityUtils.isAdmin()) {
                resoult.addAll(cacheList);
            } else {
                List<EditotrNotification> collect = cacheList.stream()
                        .filter(item -> item.getFlag() == 0 && item.getType() == 1)
                        .collect(Collectors.toList());
                resoult.addAll(collect);
            }
        }
        return ResponseResult.okResult(resoult);
    }


    private synchronized HashMap<String, EditotrNotification> getEditotrNotificationMap() {
        List<EditotrNotification> cacheList = redisCache.getCacheList(PREFIX);
        HashMap<String, EditotrNotification> map = new ClassMap();
        if (cacheList != null && !cacheList.isEmpty()) {
            for (EditotrNotification item : cacheList) {
                map.put(item.getId(), item);
            }
        }
        return map;
    }

    private synchronized Long saveEditotrNotification(HashMap<String, EditotrNotification> map) {
        redisCache.deleteObject(PREFIX);
        List<EditotrNotification> collect = new ArrayList<>(map.values());
        if (collect.size() > 0) {
            long l = redisCache.setCacheList(PREFIX, collect);
            return l;
        }
        return 0L;
    }

    @Override
    public ResponseResult addEditorImages(MultipartFile file) throws IOException {
        String s = txyunUtils.doUpdata(file, "/editor/");
        return ResponseResult.okResult(s);
    }

    @Override
    public ResponseResult deleteEditorImages(String fileName) {
        String pre = "https://miumall-1306251195.cos.ap-chengdu.myqcloud.com/";
        String replace = fileName.replace(pre, "");
        txyunUtils.doDelete(replace);
        return ResponseResult.okResult();
    }
}
