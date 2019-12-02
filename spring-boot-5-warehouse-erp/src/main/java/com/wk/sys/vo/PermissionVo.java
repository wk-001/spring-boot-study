package com.wk.sys.vo;

import com.wk.sys.entity.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PermissionVo extends Permission {

	private Integer page = 1;               //当前是第几页

	private Integer limit = 10;             //每页多少条数据
}
