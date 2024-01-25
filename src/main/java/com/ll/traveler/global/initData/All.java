package com.ll.traveler.global.initData;

import com.ll.traveler.global.app.AppConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class All {
    @Autowired
    @Lazy
    private All self;

    @Bean
    @Order(2)
    ApplicationRunner initAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        new File(AppConfig.getTempDirPath()).mkdirs();
    }

}
