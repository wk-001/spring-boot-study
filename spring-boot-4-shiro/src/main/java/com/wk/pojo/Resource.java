package com.wk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 资源名称,一般是中文名称(给人看的)
     */
    private String name;

    /**
     * 资源权限字符串,一般是 Shiro 默认的通配符表示(给人看的)
     */
    private String permission;

    /**
     * 资源 url 表示,我们设计的系统让 Shiro 通过这个路径字符串去匹配浏览器中显示的路径
     */
    private String url;


}
