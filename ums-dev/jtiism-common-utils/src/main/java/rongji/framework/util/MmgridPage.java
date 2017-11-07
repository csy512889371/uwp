package rongji.framework.util;

public class MmgridPage {
	
	private Integer pageSize = 20;
	
	private Integer pageNumber = 1;
	
	private Integer pageCount;
	
	private Integer totalCount = 0;
	
	private String sort;
	
	private String rows;
	
	private String orderBy;

	public Integer getFristResult() {
		return (this.pageNumber - 1) * this.pageSize;
	}

	public String getSort() {
		if (StringUtil.isNotEmpty(this.sort)) {
			String stringarray[] = this.sort.split("\\.");

			for (String stemp : stringarray) {
				if (stemp.equals("asc") || stemp.equals("desc")) {
					return stemp;
				}
				this.rows = stemp;
			}
			return this.sort;
		}
		return this.sort;

	}

	public String getOrderBy() {
		if (StringUtil.isNotEmpty(this.sort)) {
			return this.sort.replace('.', ' ');
		}
		return this.orderBy;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

}
