package com.hechi.niumall.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ccx
 * 腾讯云对象存储工具类
 */
@Slf4j
@Component
public class TxyunUtils {
    private COSClient cosClient = null;
    private final String PREPATH = "https://miumall-1306251195.cos.ap-chengdu.myqcloud.com/";
    private final String bucketName = "miumall-1306251195";

    TxyunUtils() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDywoSxFKSO9h2B6oKdlgQxHNErGQRFpCL";
        String secretKey = "yjHbidKn4A5oJaeV8XjiT1LUQ9h3B6nG";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.http);
        // 3 生成 cos 客户端。
        this.cosClient = new COSClient(cred, clientConfig);
    }

    //上传文件到腾讯云

    /**
     * 文件上传
     *
     * @param url 上传文件的路径
     * @param key 上传到腾讯云的地址
     * @return 返回腾讯云的对外访问链接
     */
    public String doUpdate(String url, String key) {
        if (url == null || "".equals(url)) {
            throw new RuntimeException("文件路径为空！");
        }
        // 指定要上传的文件
        File localFile = new File(url);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return PREPATH + key;
    }
    public String doUpdata(MultipartFile file, String prefix) throws IOException {
        log.info("文件上传！");
        //文件校验
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".jpg")&&!originalFilename.endsWith(".jpeg")){
            throw  new RuntimeException("文件格式不正确！");
        }
        long size = file.getSize();
        if (size >1024*1024*5) {
            throw new RuntimeException("文件大小超出规定5MB");
        }
        //校验完成后 使用File.createTempFile(prefix,subfix)
        // 创建一个临时文件 将file 写入到临时文件 然后再由 腾讯云api进行上传
        //将上传结果返回
        File localFile;
        localFile = File.createTempFile("temp", "jpg");
        file.transferTo(localFile);
        String replace = UUID.randomUUID().toString().replace("-", "");
        return this.doUpdate(String.valueOf(localFile), prefix+replace+".jpg");
    }

    /**
     * 获取路径前缀下的所有文件
     *
     * @param prefix 路径前缀
     * @return 文件列表
     */
    public List<String> getUrlList(String prefix) {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(prefix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        List<String> pathList = new ArrayList<>();
        try {
            objectListing = cosClient.listObjects(listObjectsRequest);
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
        List<String> commonPrefixs = objectListing.getCommonPrefixes();

        // object summary表示所有列出的object列表
        List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
        for (int i = 1; i < cosObjectSummaries.size(); i++) {
            COSObjectSummary cosObjectSummary = cosObjectSummaries.get(i);
            // 文件的路径key
            String key = cosObjectSummary.getKey();
            pathList.add(PREPATH + key);
//            // 文件的etag
//            String etag = cosObjectSummary.getETag();
//            // 文件的长度
//            long fileSize = cosObjectSummary.getSize();
//            // 文件的存储类型
//            String storageClasses = cosObjectSummary.getStorageClass();
        }
//        //设置下一页的起点
//        String nextMarker = objectListing.getNextMarker();
//        listObjectsRequest.setMarker(nextMarker);
        return pathList;
    }

    /**
     * 删除指定的文件
     *
     * @param key 文件的路径
     */
    public void doDelete(String key) {
        cosClient.deleteObject(bucketName, key);
    }
}
