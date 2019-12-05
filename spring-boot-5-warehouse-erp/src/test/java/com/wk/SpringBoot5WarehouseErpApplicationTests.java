package com.wk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wk.sys.entity.Role;
import com.wk.sys.service.RoleService;
import com.wk.sys.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBoot5WarehouseErpApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    void contextLoads() {
       /* Page<User> page = new Page<>(0, 3);
        UserVo userVo = new UserVo();
        userVo.setName("王五");
        userService.queryList(page,userVo);
        System.out.println("page = " + page.getRecords());

        List<Map<String, Object>> maps = userService.listMaps();
        for (Map<String, Object> map : maps) {
            System.out.println("map = " + map);
        }*/
        QueryWrapper<Role> wrapper = new QueryWrapper<Role>()
                .eq("rolecode","admin");
        Role role = roleService.getOne(wrapper);
        System.out.println("role = " + role);
    }

}
