package rongji.report.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import rongji.report.methods.Functions;

import com.othelle.jtuples.Tuple2;
import com.othelle.jtuples.Tuples;

/**
 * 
 * @Title: ReportRule.java
 * @Package rongji.report.model
 * @Description: 报表规则解析器
 * @author LFG
 * @date 2017年5月11日 上午8:53:09
 * @version V1.0
 */
public class ReportRule {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportRule.class);

	/**
	 * .输入定义
	 */
	private List<BeanRule> inputRules = new ArrayList<BeanRule>();
	/**
	 * .单元格定义
	 */
	Map<Integer, List<BeanRule>> cellRules = new HashMap<Integer, List<BeanRule>>();

	/**
	 * .全局条件
	 */
	private List<BeanRule> globalRules = new ArrayList<BeanRule>();
	/**
	 * .行条件
	 */
	private List<BeanRule> rowRules = new ArrayList<BeanRule>();
	/**
	 * .列条件
	 */
	private List<BeanRule> columnRules = new ArrayList<BeanRule>();

	Map<String, Tuple2<Functions, String>> methods = null;

	public ReportRule() {
	}

	public void createBeanRules(InputStream commondStream) throws Exception {
		BufferedReader designFis = null;
		try {
			designFis = new BufferedReader(new InputStreamReader(commondStream, "gbk"));
			String commond;
			// 处理块语句
			StringBuffer rules = new StringBuffer();
			while ((commond = designFis.readLine()) != null) {
				rules.append(commond).append("\r\n");
			}
			createBeanRules(rules.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			if (designFis != null) {
				try {
					designFis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void createStatisticsRules(String rules) throws Exception {
		try {
			List<String> commonds = Arrays.asList(rules.split("\r\n"));
			List<BeanRule> currRules = null;
			// 处理块语句
			Queue<String> ruleQueue = new LinkedList<String>();
			for (String commond : commonds) {
				if (".全局条件".equals(commond.trim())) {
					if (ruleQueue.size() > 0) {
						setBeanRule(ruleQueue, currRules);
					}
					currRules = globalRules;
					continue;
				}
				if (".行条件".equals(commond.trim())) {
					if (ruleQueue.size() > 0) {
						setBeanRule(ruleQueue, currRules);
					}
					currRules = rowRules;
					continue;
				}
				if (".列条件".equals(commond.trim())) {
					if (ruleQueue.size() > 0) {
						setBeanRule(ruleQueue, currRules);
					}
					currRules = columnRules;
					continue;
				}
				if (StringUtils.isEmpty(commond.trim()) || commond.trim().startsWith("#")) {
					// 过滤空行或注释
					continue;
				}
				if (ruleQueue.size() > 0 && commond.indexOf(">") >= 0) {
					setBeanRule(ruleQueue, currRules);
				}
				ruleQueue.offer(commond);
			}
			if (ruleQueue.size() > 0) {
				setBeanRule(ruleQueue, currRules);
			}
		} catch (Exception e) {
			logger.error("解析rule 字符串错误", e);
			throw e;
		}
	}

	public void createBeanRules(String rules) throws Exception {
		try {
			List<String> commonds = Arrays.asList(rules.split("\r\n"));
			List<BeanRule> currRules = null;
			// 处理块语句
			Queue<String> ruleQueue = new LinkedList<String>();
			for (String commond : commonds) {
				commond = commond.trim();
				if (".输入定义".equals(commond)) {
					if (ruleQueue.size() > 0) {
						setBeanRule(ruleQueue, currRules);
					}
					currRules = inputRules;
					continue;
				}
				if (".单元格定义".equals(commond)) {
					if (ruleQueue.size() > 0) {
						setBeanRule(ruleQueue, currRules);
					}
					Integer level = cellRules.size() + 1;
					cellRules.put(level, new ArrayList<BeanRule>());
					currRules = cellRules.get(level);
					continue;
				}
				if (StringUtils.isEmpty(commond) || commond.startsWith("#")) {
					// 过滤空行或注释
					continue;
				}
				if (ruleQueue.size() > 0 && commond.indexOf(">") >= 0) {
					setBeanRule(ruleQueue, currRules);
				}
				ruleQueue.offer(commond);
			}
			if (ruleQueue.size() > 0) {
				setBeanRule(ruleQueue, currRules);
			}
		} catch (Exception e) {
			logger.error("创建 Bean Rules 异常", e);
			throw e;
		}
	}

	private void setBeanRule(Queue<String> ruleQueue, List<BeanRule> currRules) throws Exception {
		String line = "";
		String ruleStr = "";
		while ((line = ruleQueue.poll()) != null) {
			ruleStr += line + "\r\n";
		}
		ruleStr = ruleStr.trim();
		BeanRule beanRule = new BeanRule(ruleStr);
		currRules.add(beanRule);
	}

	public List<BeanRule> getInputRules() {
		return inputRules;
	}

	public void setInputRules(List<BeanRule> inputRules) {
		this.inputRules = inputRules;
	}

	public List<BeanRule> getCellRules(Integer level) {
		return cellRules.get(level);
	}

	public Map<String, Tuple2<Functions, String>> getMethods() {
		return methods;
	}

	public List<BeanRule> getGlobalRules() {
		return globalRules;
	}

	public void setGlobalRules(List<BeanRule> globalRules) {
		this.globalRules = globalRules;
	}

	public List<BeanRule> getRowRules() {
		return rowRules;
	}

	public void setRowRules(List<BeanRule> rowRules) {
		this.rowRules = rowRules;
	}

	public List<BeanRule> getColumnRules() {
		return columnRules;
	}

	public void setColumnRules(List<BeanRule> columnRules) {
		this.columnRules = columnRules;
	}

	@SuppressWarnings("unchecked")
	public void createMethods(String methodPath) throws Exception {
		this.methods = new HashMap<String, Tuple2<Functions, String>>();

		File cmisXmlFile = new ClassPathResource(methodPath).getFile();
		Document document = new SAXReader().read(cmisXmlFile);
		List<Element> meths = document.getRootElement().element("methods").elements();
		List<Element> pars = document.getRootElement().element("params").elements();
		String packagePath = "";
		for (Element ele : pars) {
			String name = ele.attribute("name").getValue();
			if ("packagePath".equals(name)) {
				packagePath = ele.attribute("value").getValue();
			}
		}

		Map<String, Functions> entitys = new HashMap<String, Functions>();
		try {
			for (Element ele : meths) {
				String name = ele.attribute("name").getValue();
				String entityName = ele.attribute("entity").getValue();
				String funcName = ele.attribute("func").getValue();
				
				Attribute packageAttr = ele.attribute("package");
				String classPath = packagePath + entityName;
				if (packageAttr != null) {
					String packPath = packageAttr.getValue();
					classPath = packPath + entityName;
				}
				
				Functions entity = null;
				if (entitys.containsKey(classPath)) {
					entity = entitys.get(classPath);
				} else {
					entity = (Functions) Class.forName(classPath).newInstance();
					entitys.put(classPath, entity);
				}
				this.methods.put(name, Tuples.tuple(entity, funcName));
			}
		} catch (Exception e) {
			logger.error("创建 methods 异常.", e);
		}
	}
}
