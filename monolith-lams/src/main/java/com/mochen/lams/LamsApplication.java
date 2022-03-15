package com.mochen.lams;

import com.mochen.lams.room.netty.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LamsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LamsApplication.class, args);
        try {
            new NettyServer(12345).start();
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }
}
