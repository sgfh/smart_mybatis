package com.smart.mybatis.page;

import java.io.Serializable;
public class Pageable implements Serializable {

	/**
	 * 此类用来接收客户端传递的分页参数
	 */
	private static final long serialVersionUID = -8614920660740236996L;
	private Integer pageNumber;
	private Integer pageSize;



	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}