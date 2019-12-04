package com.wk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.entity.User;
import com.wk.sys.service.UserService;
import com.wk.sys.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot5WarehouseErpApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        Page<User> page = new Page<>(0, 3);
        UserVo userVo = new UserVo();
        userVo.setName("王五");
        userService.queryList(page,userVo);
        System.out.println("page = " + page.getRecords());
    }

}
