package com.wk.sys.common;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeBuilder {
	
	/**
	 * 将简单的集合数据转换成有层级关系的树形结构集合数据
	 */
	public static List<TreeNode> build(List<TreeNode> treeNodes,Integer topPid){
		List<TreeNode> nodes=new ArrayList<>();
		//第一次循环寻找父节点 遍历传入的节点集合A 找到对应的父节点，放入新的节点集合B
		for (TreeNode n1 : treeNodes) {
			if(n1.getPid().equals(topPid)) {
				nodes.add(n1);		//在新的集合中存放父节点
			}
			//第二次循环找子节点，如果A的ID是B的父ID，B就是A的子节点
			for (TreeNode n2 : treeNodes) {
				if(n1.getId().equals(n2.getPid())) {
					n1.getChildren().add(n2);			//在父节点中放入子节点
				}
			}
		}
		return nodes;
	}
}
