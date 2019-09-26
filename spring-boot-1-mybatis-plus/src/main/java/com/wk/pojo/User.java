package com.wk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wk
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 1：男，0：女
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 乐观锁版本号，必须加上@Version注解更新数据前对比数据版本号，如果相同更新数据，不相同则不进行操作，
     * 每次更新成功版本号自增1
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除标识，1：存在；0：已删除
     * 逻辑删除，必须加上@TableLogic注解
     */
    @TableLogic
    private Integer deleted;

    public User() {
    }

    public User(String userName, Integer age, Integer gender, String email, Date birthday) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.birthday = birthday;
    }

    public User(Integer id, String userName, Integer age, Integer version) {
        this.id = id;
        this.userName = userName;
        this.age = age;
        this.version = version;
    }
}
