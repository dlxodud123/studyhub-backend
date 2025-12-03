package com.taeyoung.studyhub.studyhub_backend.config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class H2Functions {
    public static String dateFormat(Timestamp timestamp, String pattern) {
        if (timestamp == null) return null;

        // MySQL 패턴 → Java 패턴 변환
        String javaPattern = pattern
                .replace("%Y", "yyyy")
                .replace("%m", "MM")
                .replace("%d", "dd")
                .replace("%H", "HH")
                .replace("%i", "mm")
                .replace("%s", "ss");

        return new SimpleDateFormat(javaPattern).format(timestamp);
    }
}
