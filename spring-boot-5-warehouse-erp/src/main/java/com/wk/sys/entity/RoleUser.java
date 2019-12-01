package com.wk.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wk
 * @since 2019-11-29
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
