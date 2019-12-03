package com.wk.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wk
 * @since 2019-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_user")
public class RoleUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "rid", type = IdType.INPUT)
    private Integer rid;

    @TableId(value = "uid", type = IdType.INPUT)
    private Integer uid;


}
