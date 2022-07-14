package com.hechi.niumall.service.impl;

import com.hechi.niumall.service.MessageService;
import com.hechi.niumall.utils.MailUtils;
import com.hechi.niumall.utils.MessageUtils;
import com.hechi.niumall.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author ccx
 * 阿里云验证码登录
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    RedisCache redisCache;
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    MailUtils mailUtils;
    private  static  final String PRE ="message";
    @Override
    public void sentMessage(String phonenumber) throws Exception {
        //todo 做幂等性 防止多刷
        Random random = new Random();
        int code = random.nextInt(100000);
        messageUtils.sentMessage(phonenumber,Integer.toString(code));
        redisCache.setCacheObject(PRE+phonenumber,code,1000, TimeUnit.MINUTES);
    }
    @Override
    public void sentMailCode(String to) throws MessagingException {
        Random random = new Random();
        int code = random.nextInt(100000);
        mailUtils.sentMailCode(to,"niuMaill商城验证码","mailCode",Integer.toString(code));
        redisCache.setCacheObject(PRE+to,code,1000, TimeUnit.MINUTES);
    }
}
