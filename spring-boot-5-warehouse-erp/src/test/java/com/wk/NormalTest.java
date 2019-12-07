package com.wk;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

class NormalTest {

    @Test
    void dateTest(){
        String format = DateUtil.format(new Date(), "yyyy-MM-dd");
        System.out.println("format = " + format);
    }
}
