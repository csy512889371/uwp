package rongji.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Page<T> {

	public Page(ParamRequest pageRequest, Integer totalCount) {
		this.pageRequest = pageRequest;
		this.firstResult = (pageRequest.getPageNumber() - 1) * pageRequest.getPageSize();
		this.pageSize = pageRequest.getPageSize();
		this.totalCount = totalCount;
	}

	public Page() {
	}

	private ParamRequest pageRequest;

	private List<T> result;

	private List<Map<String, Object>> resultMapList;

	private Integer totalCount = 0;

	private Integer firstResult;

	private Integer pageSize;

	@JsonIgnore
	public ParamRequest getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest(ParamRequest pageRequest) {
		this.pageRequest = pageRequest;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public List<Map<String, Object>> getResultMapList() {
		if (resultMapList == null) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}

	public void setResultMapList(List<Map<String, Object>> resultMapList) {
		this.resultMapList = resultMapList;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}



	


	

}
