package net.suncaper.tag_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TagBackendApplication {

    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "C:\\Users\\Administrator\\AppData\\Local\\hadoop-2.9.2");
        SpringApplication.run(TagBackendApplication.class, args);
    }

}
