package com.showcle.global.util;

import com.showcle.global.service.FfMpegService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class FFMPegServiceTest {

    @Autowired
    private FfMpegService ffMpegHelper;

    @Test
    public void getDurationTest() {
        String[] filePath = new String[] {"D://audio/Alarm01.wav", "D://audio/chello.mp3", "D://audio/Frozen-360p.mp4"};

        for(String path : filePath) {
            log.info("file : {}, duration : {}", path, ffMpegHelper.getDuration(path));
        }
    }
}