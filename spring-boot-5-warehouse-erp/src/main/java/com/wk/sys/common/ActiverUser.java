package com.wk.sys.common;

import com.wk.sys.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 
 * 用户的角色和权限
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

	private User user;
	
	private List<String> roles;
	
	private List<String> permissions;
}
