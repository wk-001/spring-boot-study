package com.wk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色资源关联表
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色资源关联 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色 id
     */
    private Integer roleId;

    /**
     * 资源 id
     */
    private Integer resourceId;


}
