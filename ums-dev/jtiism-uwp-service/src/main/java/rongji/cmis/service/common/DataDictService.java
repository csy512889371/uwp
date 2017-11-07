package rongji.cmis.service.common;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.base.service.BaseService;
import rongji.framework.util.ParamRequest;

/**
 * service
 * 
 * @Title: 对应中组织部颁标准代码 service
 * @version V1.0
 */
public interface DataDictService extends BaseService<CfgUmsDataDict> {

	List<ZtreeDictNote> findZtreeNoteList(String codeTable);

	List<CfgUmsDataDict> findListFromDbExcludeHidden(String codeTable, boolean refreshCache);

	List<CfgUmsDataDict> findListFromDbIncludeHidden(String codeTable);
	
	List<CfgUmsDataDict> findListOfZB02();
	
	/**
	* @Title: findExtByCodeTableName
	* @Description: (查询扩展指标)
	* @param codeTable
	* @param findHidden
	* @return
	* @return List<CfgUmsDataDict>    返回类型
	* @throws
	* @author wqq 
	*/ 
	public List<CfgUmsDataDict> findExtByCodeTableName(String codeTable, boolean findHidden);

	/**
	 * 通过 表名 以及 字段名 查询对应的 数据字典
	 */
	List<CfgUmsDataDict> findListByFieldInfo(String tabName, String colName);

	ZtreeDictNote findByFieldInfo(String srcTable, String srcField, String code);

	String findCodeNameByFieldInfo(String srcTable, String srcField, String code);

	CfgUmsDataDict findDataDictByCode(String codeTable, String code);

	String refreshCaches(List<String> codeTables);

	void updateDataDict(String codeTable, CfgUmsDataDict dataDict);

	void addDataDict(String codeTable, CfgUmsDataDict dataDict);

	void deleteDataDict(String codeTable, String id);

	void deleteDataDictAll(String codeTable, String id);

	public List<ZtreeDictNote> convertDictToTreeNote(List<CfgUmsDataDict> dictList);

	boolean isCodeUnique(String codeTable, String newCode, String oldCode);

	CfgUmsDataDict findDataDictByCodeName(String codeTable, String codeName);

	CfgUmsDataDict findDataDictByCodeABR1(String codeTable, String codeABR1);

	void setCommonCode(String codeTable, String[] codes, String isCommon);

	String getAbr1ByFieldInfo(String string, String string2, String a0801b);
	
	List<CfgUmsDataDict> findListByCodeName(String codeTable, String codeName);

	Map<String, Object> findUnitsByCode(String code, ParamRequest paramRequest);

	List<CfgUmsDataDict> findByUnitCode(String code);

	ZtreeDictNote findByFieldInfo(String srcField, String code);

	String findCodeNameByFieldInfo(String srcField, String curValue);
	
	/**
	 * @description 获取下级指标
	 * @param codeTable
	 * @param code
	 * @return List<CfgUmsDataDict>
	 */
	public List<CfgUmsDataDict> findSubCodesByCode(String codeTable, String code);
}
