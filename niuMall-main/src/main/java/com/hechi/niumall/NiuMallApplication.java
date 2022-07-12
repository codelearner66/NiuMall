package com.hechi.niumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author ccx
 */
@SpringBootApplication
@EnableAsync
public class NiuMallApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(NiuMallApplication.class,args);
    }
}
