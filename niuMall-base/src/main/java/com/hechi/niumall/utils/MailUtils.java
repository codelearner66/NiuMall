package com.hechi.niumall.utils;

import com.hechi.niumall.entity.EmailParam;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailUtils {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    //发送方邮箱
    private  final String senter="chenbyf1314@foxmail.com";

    /**发送普通文本邮件不带附件
     *
     * @param subject 邮件标题
     * @param context  邮件正文
     * @param acceptor  接收方邮箱
     * @return true/false
     */
    @Async
    public  boolean sendSimply(String subject, String context, String... acceptor){
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件标题
        message.setSubject(subject);
        //设置邮件内容
        message.setText(context);
        //设置接收方邮箱
        message.setTo(acceptor);

        message.setFrom(senter);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new RuntimeSqlException("发生未知错误！");
        }
    }

    /**发送普通文本邮件带附件
     * @param subject 邮件标题
     * @param filePath 附件路径数组
     * @param context   邮件正文 可以是text字符串还可以是Html文本
     * @param acceptor  接收方邮箱
     * @return   true/false
     * @throws MessagingException
     */
    @Async
    public boolean sendWithAccessory(String subject,String[] filePath, String context, String... acceptor) throws MessagingException {

        MimeMessage message= mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //设置邮件标题
        helper.setSubject(subject);
        //设置邮件内容
        helper.setText(context,true);
        //设置接收方邮箱
        helper.setTo(acceptor);

        helper.setFrom(senter);
        //添加附件
        for (String file: filePath) {
            File file1 = new File(file);
            String name = file1.getName();
            helper.addAttachment(name,file1);
        }
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new RuntimeSqlException("发生未知错误！");
        }

    }
    @Async
    public void thymeleafEmail(String[] to, String subject, EmailParam emailParam, String template, String imgPath) throws MessagingException {
        MimeMessage mimeMessage =mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(senter);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        // 利用 Thymeleaf 模板构建 html 文本
        Context ctx = new Context();
        // 给模板的参数的上下文
        ctx.setVariable("emailParam", emailParam);
        // 执行模板引擎，执行模板引擎需要传入模板名、上下文对象
        // Thymeleaf的默认配置期望所有HTML文件都放在 **resources/templates ** 目录下，以.html扩展名结尾。
        // String emailText = templateEngine.process("email/templates", ctx);
        String emailText = templateEngine.process(template, ctx);
        mimeMessageHelper.setText(emailText, true);
        // FileSystemResource logoImage= new FileSystemResource("D:\\image\\logo.jpg");
        //绝对路径
        //FileSystemResource logoImage = new FileSystemResource(imgPath);
        //相对路径，项目的resources路径下
        ClassPathResource logoImage = new ClassPathResource(imgPath);
        // 添加附件,第一个参数表示添加到 Email 中附件的名称，第二个参数是图片资源
        //一般图片调用这个方法
        mimeMessageHelper.addInline("logoImage", logoImage);
        //一般文件附件调用这个方法
//		mimeMessageHelper.addAttachment("logoImage", resource);
        mailSender.send(mimeMessage);
    }
    @Async
    public  void sentMailCode(String to,String code) throws MessagingException {
        MimeMessage mimeMessage =mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(senter);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("niuMaill商城验证码");
        // 利用 Thymeleaf 模板构建 html 文本
        Context ctx = new Context();
        // 给模板的参数的上下文
        Map<String, Object> message = new HashMap<>();
        message.put("message",code);
        ctx.setVariables(message);
        // String emailText = templateEngine.process("email/templates", ctx);
        String emailText = templateEngine.process("mailCode", ctx);
        mimeMessageHelper.setText(emailText, true);
        mailSender.send(mimeMessage);
    }
}