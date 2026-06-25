package com.showcle.global.service;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
@Slf4j
public class FfMpegService {

    @Value("${ffmpeg.path}")
    private String ffmpeg;

    public String getDuration(String filePath) {
        if(StringUtils.isEmpty(filePath)) return null;

        try {
            String[] commands = { ffmpeg + File.separator + "ffprobe", "-v", "error", "-show_entries",
                    "format=duration", "-of", "default=noprint_wrappers=1:nokey=1", filePath };

            Process processor = Runtime.getRuntime().exec(commands);

            BufferedReader reader = new BufferedReader(new InputStreamReader(processor.getInputStream()));

            // 첫 라인만 저장
            String duration = reader.readLine();

            processor.waitFor();

            int exitValue = processor.exitValue();

            if (exitValue != 0) {
                throw new RuntimeException("exit code is not - [" + exitValue + "]");
            }
            BigDecimal bd = new BigDecimal(duration);

            // 반올림
            BigDecimal result = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(result.doubleValue());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
