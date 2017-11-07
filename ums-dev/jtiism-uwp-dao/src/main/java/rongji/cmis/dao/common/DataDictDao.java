package rongji.cmis.dao.common;

import java.util.List;

import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.framework.base.dao.GenericDao;

/**
 * 
 * @Title: 对应中组织部颁标准代码
 * @version V1.0
 */
public interface DataDictDao extends GenericDao<CfgUmsDataDict> {

	Long findCountByCodeTableName(String codeTable, boolean findHidden);

	List<CfgUmsDataDict> findPageByCodeTableName(String codeTable, final int offset, final int pageSize, boolean findHidden);
	
	List<CfgUmsDataDict> findListOfZB02();
	
	List<CfgUmsDataDict> findExtByCodeTableName(String codeTable, boolean findHidden);

	CfgUmsDataDict findDataDictByCode(String codeTable, String code);

	void updateDataDict(String codeTable, CfgUmsDataDict dataDict);

	void addDataDict(String codeTable, CfgUmsDataDict dataDict);

	void deleteDataDict(String codeTable, String id);

	void deleteDataDictAll(String codeTable, String id);

	List<CfgUmsDataDict> findDataDictByCodeName(String codeTable, String codeName);

	List<CfgUmsDataDict> findDataDictByCodeABR1(String codeTable, String codeABR1);

	void setCommonCode(String codeTable, String[] codes, short isCommon);
	
	List<CfgUmsDataDict> findDataDictByUnitCode(String codeTable, String unitCode);
	
	List<CfgUmsDataDict> findSubCodesByCode(String codeTable, String code);
}
