package rongji.report.model;

import java.util.ArrayList;
import java.util.List;

import rongji.framework.base.exception.ApplicationException;
import rongji.framework.util.StringUtil;

public class SqlInfo {
	List<String> withInfo = new ArrayList<String>();
	List<String> selectInfo = new ArrayList<String>();
	List<String> fromInfo = new ArrayList<String>();
	List<String> joinInfo = new ArrayList<String>();
	List<String> whereInfo = new ArrayList<String>();

	public String createASql() {
		if (selectInfo.size() != 1) {
			throw new ApplicationException("select的条件必须有且只有一个");
		}
		if (fromInfo.size() != 1) {
			throw new ApplicationException("select的条件必须有且只有一个");
		}
		StringBuilder builder = new StringBuilder();
		// WITH
		if (!withInfo.isEmpty()) {
			builder.append(" WITH \r\n ");
			for (int i = 0; i < withInfo.size() - 1; i++) {
				builder.append(withInfo.get(i)).append("\r\n , ");
			}
			builder.append(withInfo.get(withInfo.size() - 1)).append("  ");
		}
		// SELECT
		builder.append(selectInfo.get(0)).append(" ");
		// FROM
		builder.append(fromInfo.get(0)).append(" ");
		// JOIN
		for (String joinStr : joinInfo) {
			builder.append(joinStr).append(" ");
		}
		//WHERE
		if (!whereInfo.isEmpty()) {
			builder.append(" where 1=1 ");
			for (String whereStr : whereInfo) {
				if (StringUtil.isEmpty(whereStr)) {
					continue;
				}
				builder.append(" and ( ").append(whereStr).append(") ");
			}
		}
		return builder.toString();
	}

	public SqlInfo addNewSqlInfo(SqlInfo sqlInfo) {
		SqlInfo newSqlInfo = new SqlInfo();
		newSqlInfo.withInfo.addAll(withInfo);
		newSqlInfo.withInfo.addAll(sqlInfo.withInfo);
		newSqlInfo.selectInfo.addAll(selectInfo);
		newSqlInfo.selectInfo.addAll(sqlInfo.selectInfo);
		newSqlInfo.fromInfo.addAll(fromInfo);
		newSqlInfo.fromInfo.addAll(sqlInfo.fromInfo);
		newSqlInfo.joinInfo.addAll(joinInfo);
		newSqlInfo.joinInfo.addAll(sqlInfo.joinInfo);
		newSqlInfo.whereInfo.addAll(whereInfo);
		newSqlInfo.whereInfo.addAll(sqlInfo.whereInfo);
		return newSqlInfo;
	}

	public void addWith(String withStr) {
		this.withInfo.add(withStr);
	}

	public void addSelect(String selectStr) {
		this.selectInfo.add(selectStr);
	}

	public void addFrom(String fromStr) {
		this.fromInfo.add(fromStr);
	}

	public void addJoin(String joinStr) {
		this.joinInfo.add(joinStr);
	}

	public void addWhereAnd(String whereAndStr) {
		this.whereInfo.add(whereAndStr);
	}

}
