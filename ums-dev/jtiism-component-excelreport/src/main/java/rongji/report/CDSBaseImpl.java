package rongji.report;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rongji.framework.util.StringUtil;
import rongji.report.methods.Functions;
import rongji.report.model.BeanRule;
import rongji.report.model.SqlInfo;

import com.othelle.jtuples.Tuple;
import com.othelle.jtuples.Tuple2;
import com.othelle.jtuples.Tuples;

public abstract class CDSBaseImpl implements CDS, CDSSql {

	private ValueHolder holderHead = new ValueHolder(null);

	private ValueHolder holderTail = this.holderHead;

	private SqlInfo sqlInfo = null;

	/**
	 * 
	 * @Title: ChainDataSet.java
	 * @Package rongji.report.utils
	 * @Description: 方法重载的标志位（同个方法名最多实现五次重载）
	 * @author liuzhen
	 * @date 2017年4月21日 下午3:03:03
	 * @version V1.0
	 */
	public enum MethodOverloadType {
		fir, sec, thir, four, five
	}

	public final class ValueHolder {
		private String name;
		private Tuple value;
		ValueHolder next;
		private MethodOverloadType type = MethodOverloadType.fir;

		ValueHolder(String namet) {
			setName(namet);
			setValue(null);
			next = null;
		}

		public ValueHolder clone() {
			ValueHolder valueHolder = new ValueHolder(getName());
			valueHolder.setValue(value);
			valueHolder.next = next;
			valueHolder.setType(type);
			return valueHolder;
		}

		public Tuple getValue() {
			return value;
		}

		public void setValue(Tuple value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public MethodOverloadType getType() {
			return type;
		}

		public void setType(MethodOverloadType type) {
			this.type = type;
		}

	}

	Map<String, Tuple2<Functions, String>> methods;

	private BeanRule beanRule;

	public SqlInfo getSqlInfo() {
		return sqlInfo;
	}

	public void setTarget(BeanRule beanRule) {
		this.beanRule = beanRule;
	}

	public BeanRule getBeanRule() {
		return beanRule;
	}

	public Map<String, Tuple2<Functions, String>> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Tuple2<Functions, String>> methods) {
		this.methods = methods;
	}

	public ValueHolder getHolderHead() {
		return holderHead;
	}

	public void setHolderHead(ValueHolder holderHead) {
		this.holderHead = holderHead;
	}

	public void addValueHolders(ValueHolder holder) {
		while (holder != null) {
			if (StringUtil.isEmpty(holder.getName()) || holder.getName().equals("setObject")) {
				holder = holder.next;
				continue;
			}
			this.holderTail = (this.holderTail.next = holder.clone());
			this.holderTail.next = null;
			holder = holder.next;
		}
	}

	protected ValueHolder addHolder() {
		ValueHolder valueHolder = new ValueHolder(null);
		this.holderTail = (this.holderTail.next = valueHolder);
		return valueHolder;
	}

	public void excute() throws Exception {
		List<ValueHolder> holders = new ArrayList<ValueHolder>();
		ValueHolder valueHolder = this.holderHead.next;
		while (valueHolder != null) {
			if (valueHolder.getName().equals("autoFit") || valueHolder.getName().equals("autoFitEach") || valueHolder.getName().equals("autoResumeFit") || valueHolder.getName().equals("autoHeight")) {
				holders.add(valueHolder);
				valueHolder = valueHolder.next;
				continue;
			}
			Tuple2<Functions, String> pro2 = methods.get(valueHolder.getName());
			Functions method = pro2._1();
			if (method != null) {
				Method func = method.getClass().getDeclaredMethod(pro2._2(), Tuple.class, MethodOverloadType.class);
				func.invoke(method, valueHolder.getValue(), valueHolder.getType());
			}
			valueHolder = valueHolder.next;
		}
		for (ValueHolder hoder : holders) {
			Tuple2<Functions, String> pro2 = methods.get(hoder.getName());
			Functions method = pro2._1();
			if (method != null) {
				Method func = method.getClass().getDeclaredMethod(pro2._2(), Tuple.class, MethodOverloadType.class);
				func.invoke(method, hoder.getValue(), hoder.getType());
			}
		}
	}

	public void resert() {
		this.holderHead = new ValueHolder(null);
		this.holderTail = this.holderHead;
		this.beanRule = null;
	}

	/**
	 * sqlinfo方法
	 */
	private SqlInfo getCurSqlInfo() {
		if (sqlInfo == null) {
			sqlInfo = new SqlInfo();
		}
		return sqlInfo;
	}

	@Override
	public CDSBaseImpl with(String withStr) {
		return this;
	}

	@Override
	public CDSBaseImpl select(String selectStr) {
		getCurSqlInfo().addSelect(selectStr);
		return this;
	}

	@Override
	public CDSBaseImpl from(String fromStr) {
		getCurSqlInfo().addFrom(fromStr);
		return this;
	}

	@Override
	public CDSBaseImpl join(String joinStr) {
		getCurSqlInfo().addJoin(joinStr);
		return this;
	}

	@Override
	public CDSBaseImpl whereAnd(String whereAndStr) {
		getCurSqlInfo().addWhereAnd(whereAndStr);
		return this;
	}

	/**
	 * 赋值方法
	 */

	// Object
	@Override
	public final void setObject() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setObject");
		valueHolder.setValue(Tuples.tuple(beanRule.getTarget()));
	}

	//
	@Override
	public final void fillEach(Integer step, Integer max, String[] names) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("fillEach");
		valueHolder.setValue(Tuples.tuple(beanRule.getTarget(), step, max, names));
	}

	// Map
	@Override
	public void fill(String[] names) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("fill");
		valueHolder.setValue(Tuples.tuple(beanRule.getTarget(), names));
	}

	@Override
	public void setPicture() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setPicture");
		valueHolder.setValue(Tuples.tuple(beanRule.getTarget()));

	}

	/**
	 * end
	 */
	/**
	 * 以下为功能方法
	 */

	@Override
	public CDSBaseImpl data() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("data");
		valueHolder.setValue(null);
		return this;
	}

	@Override
	public CDSBaseImpl getParam(String name) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("getParam");
		valueHolder.setValue(Tuples.tuple(name));
		return this;
	}

	@Override
	public CDSBaseImpl index(Integer index) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("index");
		valueHolder.setValue(Tuples.tuple(index));
		return this;
	}

	@Override
	public CDSBaseImpl getCurDate() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("getCurDate");
		valueHolder.setValue(null);
		return this;
	}

	@Override
	public CDSBaseImpl getDateBeforeYear(Integer beforeYear) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("getDateBeforeYear");
		valueHolder.setValue(Tuples.tuple(beforeYear));
		return this;
	}

	@Override
	public CDSBaseImpl ageToBirthdate(Integer age) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("ageToBirthdate");
		valueHolder.setValue(Tuples.tuple(age));
		return this;
	}

	@Override
	public CDSBaseImpl sql(String sql) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("sql");
		valueHolder.setValue(Tuples.tuple(sql));
		return this;
	}

	@Override
	public CDSBaseImpl get(String name) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("get");
		valueHolder.setValue(Tuples.tuple(name));
		return this;
	}

	@Override
	public CDSBaseImpl separator(String separator) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("separator");
		valueHolder.setValue(Tuples.tuple(separator));
		return this;
	}

	@Override
	public CDSBaseImpl dateFormate(String pattern) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("dateFormate");
		valueHolder.setValue(Tuples.tuple(pattern));
		return this;
	}

	@Override
	public CDSBaseImpl dateFormate(String name, String pattern) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setType(MethodOverloadType.sec);
		valueHolder.setName("dateFormate");
		valueHolder.setValue(Tuples.tuple(name, pattern));
		return this;
	}

	@Override
	public CDSBaseImpl cache(String name) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("cache");
		valueHolder.setValue(Tuples.tuple(name));
		return this;
	}

	public CDSBaseImpl autoHeight(String target) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("autoHeight");
		valueHolder.setValue(Tuples.tuple(target));
		return this;
	}

	@Override
	public CDSBaseImpl autoFit(String target, Integer width, Integer height) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("autoFit");
		valueHolder.setValue(Tuples.tuple(target, width, height));
		return this;
	}

	@Override
	public CDSBaseImpl autoFit(String target, String width, String height) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("autoFit");
		valueHolder.setValue(Tuples.tuple(target, width, height));
		return this;
	}

	@Override
	public CDSBaseImpl autoResumeFit(String target, Integer width, Integer height) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("autoResumeFit");
		valueHolder.setValue(Tuples.tuple(target, width, height));
		return this;
	}

	@Override
	public CDSBaseImpl setSheetName(String objName) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setSheetName");
		valueHolder.setValue(Tuples.tuple(objName));
		return this;
	}

	@Override
	public CDSBaseImpl setFileName(String objName) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setFileName");
		valueHolder.setValue(Tuples.tuple(objName));
		return this;
	}

	@Override
	public CDSBaseImpl copyRowsAndAddShiftLine(Integer startRow, Integer endRow, Integer tarStartRow) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("copyRowsAndAddShiftLine");
		valueHolder.setValue(Tuples.tuple(startRow, endRow, tarStartRow));
		return this;
	}

	@Override
	public CDSBaseImpl copyARowWithStartTempRow(Integer startRow) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("copyARowWithStartTempRow");
		valueHolder.setValue(Tuples.tuple(startRow));
		return this;
	}

	@Override
	public CDSBaseImpl autoFitEach(String target, Integer step, Integer max, Integer width, Integer height) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("autoFitEach");
		valueHolder.setValue(Tuples.tuple(target, step, max, width, height));
		return this;
	}

	@Override
	public CDSBaseImpl setCell(Object val) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setCell");
		valueHolder.setValue(Tuples.tuple(val));
		return this;
	}

	@Override
	public CDSBaseImpl count() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("count");
		valueHolder.setValue(null);
		return this;
	}

	@Override
	public CDSBaseImpl avgAge(String birthKey, String dividendKey) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("avgAge");
		valueHolder.setValue(Tuples.tuple(birthKey, dividendKey));
		return this;
	}

	@Override
	public CDSBaseImpl buildInQuery(String paramName, List<String> inValues) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("buildInQuery");
		valueHolder.setValue(Tuples.tuple(paramName, inValues));
		return this;
	}

	@Override
	public CDSBaseImpl setGlobalParam(String paramName, Object paramVal) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("setGlobalParam");
		valueHolder.setValue(Tuples.tuple(paramName, paramVal));
		return this;
	}

	/**
	 * 
	 * 输出“是/否” 空字符串 不转成“否”
	 */
	@Override
	public CDSBaseImpl yesOrno() {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("yesOrno");
		valueHolder.setValue(Tuples.tuple(true));
		return this;
	}

	@Override
	public CDSBaseImpl yesOrno(boolean ignoreEmpty) {
		ValueHolder valueHolder = addHolder();
		valueHolder.setName("yesOrno");
		valueHolder.setValue(Tuples.tuple(ignoreEmpty));
		valueHolder.setType(MethodOverloadType.sec);
		return this;
	}

	/**
	 * end
	 */

}
