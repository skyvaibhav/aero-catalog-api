package com.hc.db.bean;

import java.util.List;

public class GridParam {

	private List<GridKey> filter;
	
	private List<GridKey> sort;
	
	private int start;
	
	private int limit;

	public List<GridKey> getFilters() {
		return filter;
	}

	public void setFilter(List<GridKey> filter) {
		this.filter = filter;
	}

	public List<GridKey> getSort() {
		return sort;
	}

	public void setSort(List<GridKey> sort) {
		this.sort = sort;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
