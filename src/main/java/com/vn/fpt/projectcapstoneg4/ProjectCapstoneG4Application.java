package com.vn.fpt.projectcapstoneg4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class , HibernateJpaAutoConfiguration.class})
public class ProjectCapstoneG4Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectCapstoneG4Application.class, args);
    }

}
