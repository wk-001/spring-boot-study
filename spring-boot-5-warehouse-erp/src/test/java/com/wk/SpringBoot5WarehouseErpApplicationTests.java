package com.wk;

import com.wk.sys.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringBoot5WarehouseErpApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
       /* Page<User> page = new Page<>(0, 3);
        UserVo userVo = new UserVo();
        userVo.setName("王五");
        userService.queryList(page,userVo);
        System.out.println("page = " + page.getRecords());*/
        List<Map<String, Object>> maps = userService.listMaps();
        for (Map<String, Object> map : maps) {
            System.out.println("map = " + map);
        }
    }

}
