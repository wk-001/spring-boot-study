package com.wk.sys.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * json数据实体
 * @author LJH
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView implements Serializable {
	
	private Integer code=0;
	private String msg="";
	private Long count=0L;
	private Object data;
	public DataGridView(Long count, Object data) {
		super();
		this.count = count;
		this.data = data;
	}
	public DataGridView(Object data) {
		super();
		this.data = data;
	}
}
