package com.wk.sys.vo;

import com.wk.sys.entity.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeptVo extends Dept {

    private Integer page = 1;               //当前是第几页

    private Integer limit = 10;             //每页多少条数据

    private Integer[] ids;                  //接受多个ID 用于批量删除

}
