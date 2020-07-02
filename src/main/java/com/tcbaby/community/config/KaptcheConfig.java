package com.tcbaby.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author tcbaby
 * @date 20/05/05 20:46
 */
@Configuration
public class KaptcheConfig {

    @Bean
    public Producer getProducer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties prop = new Properties();
        prop.setProperty("kaptcha.image.width", "100");
        prop.setProperty("kaptcha.image.height", "40");
        prop.setProperty("kaptcha.textproducer.font.size", "32");
        prop.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        prop.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        prop.setProperty("kaptcha.textproducer.char.length", "4");
        prop.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        kaptcha.setConfig(new Config(prop));
        return kaptcha;
    }
}
