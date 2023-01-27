package com.hechi.niumall;

import com.hechi.niumall.utils.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author ccx
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class NiuMallApplication
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext run = SpringApplication.run(NiuMallApplication.class, args);
        WebSocketServer.setApplicationContext(run);
    }
}
