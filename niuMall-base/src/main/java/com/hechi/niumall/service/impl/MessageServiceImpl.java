package com.hechi.niumall.service.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.hechi.niumall.service.MessageService;
import com.hechi.niumall.utils.MailUtils;
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
    MailUtils mailUtils;
    private  static  final String PRE ="message";

    private final String accessKeyId="LTAI5tJfVqaZzHnxvKmBvUFe";
    private final String accessKeySecret="Gj7lOqYQdQk3w7OHNnZTmBeBpXiEuT";

   private com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
    @Override
    public void sentMessage(String phonenumber) throws Exception {
        //todo 做幂等性 防止多刷
        String template="{\"code\":\"*******\"}";
        String str="*******";
        Random random = new Random();
        int code = random.nextInt(100000);
        String replace = template.replace(str, Integer.toString(code));
        com.aliyun.dysmsapi20170525.Client client = createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phonenumber)
                .setTemplateParam(replace);
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
            redisCache.setCacheObject(PRE+phonenumber,code,1000, TimeUnit.MINUTES);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception error1) {
            TeaException error = new TeaException(error1.getMessage(), error1);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    @Override
    public void sentMailCode(String to) throws MessagingException {
        Random random = new Random();
        int code = random.nextInt(100000);
        mailUtils.sentMailCode(to,Integer.toString(code));
        redisCache.setCacheObject(PRE+to,code,1000, TimeUnit.MINUTES);
    }
}
