package com.ll.traveler.global.app;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Getter
    private static String siteName;

    @Value("${custom.site.name}")
    public void setSiteName(String siteName) { this.siteName = siteName; }

    @Getter
    private static EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Getter
    private static String tempDirPath;

    @Value("${custom.temp.dirPath}")
    public void setTempDirPath(String tempDirPath) { this.tempDirPath = tempDirPath; }

    @Getter
    private static String genFileDirPath;

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath) { this.genFileDirPath = genFileDirPath; }
}