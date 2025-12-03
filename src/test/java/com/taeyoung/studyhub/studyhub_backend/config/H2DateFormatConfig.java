package com.taeyoung.studyhub.studyhub_backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
@Profile("test")
public class H2DateFormatConfig {
    @PostConstruct
    public void registerFunctions() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = conn.createStatement();

        // H2에 DATE_FORMAT 함수를 등록
        stmt.execute(
                "CREATE ALIAS IF NOT EXISTS DATE_FORMAT FOR " +
                        "'com.taeyoung.studyhub.studyhub_backend.config.H2Functions.dateFormat'"
        );

        stmt.close();
        conn.close();
    }
}
