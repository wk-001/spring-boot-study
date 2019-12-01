package com.wk.sys.vo;

import com.wk.sys.entity.Loginfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogInfoVo extends Loginfo {

    private Integer page = 1;               //当前是第几页

    private Integer limit = 10;             //每页多少条数据

    private Integer[] ids;                  //接受多个ID 用于批量删除

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")         //对前台传递到后台的时间数据进行格式化
    private Date startTime;         //查询起始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")         //对前台传递到后台的时间数据进行格式化
    private Date endTime;           //查询结束时间
}
