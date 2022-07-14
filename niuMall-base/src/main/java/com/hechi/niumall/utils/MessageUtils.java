package com.hechi.niumall.utils;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
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
   public void sentMessage(String phonenumber,String code) throws Exception {
        //todo 做幂等性 防止多刷
        String template="{\"code\":\"*******\"}";
        String str="*******";
        String replace = template.replace(str, code);
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
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception error1) {
            TeaException error = new TeaException(error1.getMessage(), error1);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
    //todo 多模板封装
}
