package com.wk.sys.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组装属性结构的节点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {

	private Integer id;

	@JsonProperty("parentId")
	private Integer pid;									//父节点ID

	private String title;									//标题

	private String icon;									//图标

	private String href;									//跳转路径

	private Boolean spread;									//是否展开

	private List<TreeNode> children = new ArrayList<>();	//存放子节点
	
	private String checkArr="0";							//0代表不选中  1代表选中
	
	/**
	 * 首页左侧导航树的构造器
	 */
	public TreeNode(Integer id, Integer pid, String title, String icon, String href, Boolean spread) {
		super();
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.icon = icon;
		this.href = href;
		this.spread = spread;
	}

	/**
	 * dtree的数据格式
	 */
	public TreeNode(Integer id, Integer pid, String title, Boolean spread) {
		super();
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.spread = spread;
	}

	/**
	 * dTree复选树的构造器
	 */
	public TreeNode(Integer id, Integer pid, String title, Boolean spread, String checkArr) {
		super();
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.spread = spread;
		this.checkArr = checkArr;
	}

	
}
