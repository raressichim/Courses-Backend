package com.ing.hubs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // generateSecret();
        log.info("App started");
    }

    private void generateSecret() {
        var key = Jwts.SIG.HS256.key().build();
        var keyEncoded = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("Secret: " + keyEncoded);
    }
}
