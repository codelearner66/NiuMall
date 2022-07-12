package com.hechi.niumall.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static  void responseUtils(HttpServletResponse response, AppHttpCodeEnum enums, String string){
        response.setStatus(enums.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(new ObjectMapper().writeValueAsString(ResponseResult.okResult(string)));
        }catch (IOException e) {
            System.out.println("认证失败！ ");
        }

    }


    public static void setDownLoadHeader(String filename, ServletContext context, HttpServletResponse response) throws UnsupportedEncodingException {
        String mimeType = context.getMimeType(filename);//获取文件的mime类型
        response.setHeader("content-type",mimeType);
        String fname= URLEncoder.encode(filename,"UTF-8");
        response.setHeader("Content-disposition","attachment; filename="+fname);
    }
}