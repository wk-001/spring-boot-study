package com.wk;

import com.wk.sys.entity.User;
import com.wk.sys.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringBoot5WarehouseErpApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        List<User> list = userService.list();
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }

}
