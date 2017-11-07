package rongji.cmis.service.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.common.DataDictDao;
import rongji.cmis.model.sys.SysFieldInfo;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.cmis.service.common.DataDictService;
import rongji.cmis.service.common.FieldInfoService;
import rongji.cmis.service.redis.DataDictRepository;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.ParamRequest;

/**
 * 
 * @Title: 对应中组织部颁标准代码 service
 * @version V1.0
 */
@Service("dataDictServiceImpl")
public class DataDictServiceImpl extends BaseServiceImpl<CfgUmsDataDict> implements DataDictService {

	private static final Logger logger = LoggerFactory.getLogger(DataDictServiceImpl.class);

	private static final Integer pageSize = 1000;

	@Resource(name = "dataDictDaoImpl")
	private DataDictDao dataDictDao;

	@Resource(name = "fieldInfoServiceImpl")
	private FieldInfoService fieldInfoService;

	@Resource(name = "unitIndexServiceImpl")
	private UnitIndexService unitIndexService;

	@Autowired
	private DataDictRepository dataDictRepository;

	@Override
	public List<ZtreeDictNote> findZtreeNoteList(String codeTable) {

		List<ZtreeDictNote> dataDictList = null;

		if (StringUtils.isEmpty(codeTable)) {
			return dataDictList;
		}

		// 1. get codeTable from Redis;
		dataDictList = dataDictRepository.getDataDictList(codeTable);

		// 2. get codeTable from DB
		if (dataDictList == null || dataDictList.isEmpty()) {
			dataDictList = convertDictToTreeNote(findListFromDbExcludeHidden(codeTable, true));
		}
		return dataDictList;
	}

	@Override
	public CfgUmsDataDict findDataDictByCode(String codeTable, String code) {
		if (code == null || StringUtils.isEmpty(code.trim())) {
			return null;
		}
		CfgUmsDataDict dataDict = dataDictDao.findDataDictByCode(codeTable, code);
		return dataDict;
	}

	@Override
	public ZtreeDictNote findByFieldInfo(String srcTable, String srcField, String code) {

		if (StringUtils.isEmpty(srcTable) || StringUtils.isEmpty(srcField) || StringUtils.isEmpty(code)) {
			return null;
		}

		SysFieldInfo fieldInfo = fieldInfoService.findFieldInfo(srcTable, srcField);
		if (fieldInfo == null || StringUtils.isEmpty(fieldInfo.getCodeTable())) {
			return null;
		}

		ZtreeDictNote ztreeDictNote = null;
		// 1. get dictNote from redis
		if (!"UNIT_INDEX".equalsIgnoreCase(fieldInfo.getCodeTable())) {
			ztreeDictNote = dataDictRepository.getDataDict(fieldInfo.getCodeTable(), code);
		}

		// 2.if code not in redis cache. query from DB.
		if (ztreeDictNote == null) {
			CfgUmsDataDict cfgUmsDataDict = this.findDataDictByCode(fieldInfo.getCodeTable(), code);
			if (cfgUmsDataDict == null) {
				ztreeDictNote = new ZtreeDictNote();
				ztreeDictNote.setCodeName(code);
				ztreeDictNote.setCodeAbr1(code);
				ztreeDictNote.setCodeAbr2(code);
				ztreeDictNote.setCodeAName(code);
			} else {
				ztreeDictNote = convertDictToTreeNote(cfgUmsDataDict);
			}
		}
		return ztreeDictNote;
	}

	@Override
	public String findCodeNameByFieldInfo(String srcField, String code) {
		ZtreeDictNote note = findByFieldInfo(srcField, code);
		if (note != null && note.getCodeName() != null) {
			return note.getCodeName();
		}
		return "";
	}

	@Override
	public ZtreeDictNote findByFieldInfo(String srcField, String code) {

		if (StringUtils.isEmpty(srcField) || StringUtils.isEmpty(code)) {
			return null;
		}

		SysFieldInfo fieldInfo = fieldInfoService.findFieldInfoBySrcField(srcField);
		if (fieldInfo == null || StringUtils.isEmpty(fieldInfo.getCodeTable())) {
			return null;
		}

		ZtreeDictNote ztreeDictNote = null;
		// 1. get dictNote from redis
		if (!"UNIT_INDEX".equalsIgnoreCase(fieldInfo.getCodeTable())) {
			ztreeDictNote = dataDictRepository.getDataDict(fieldInfo.getCodeTable(), code);
		}

		// 2.if code not in redis cache. query from DB.
		if (ztreeDictNote == null) {
			CfgUmsDataDict cfgUmsDataDict = this.findDataDictByCode(fieldInfo.getCodeTable(), code);
			if (cfgUmsDataDict == null) {
				ztreeDictNote = new ZtreeDictNote();
				ztreeDictNote.setCodeName(code);
				ztreeDictNote.setCodeAbr1(code);
				ztreeDictNote.setCodeAbr2(code);
				ztreeDictNote.setCodeAName(code);
			} else {
				ztreeDictNote = convertDictToTreeNote(cfgUmsDataDict);
			}
		}
		return ztreeDictNote;
	}

	@Override
	public String findCodeNameByFieldInfo(String srcTable, String srcField, String code) {
		ZtreeDictNote note = findByFieldInfo(srcTable, srcField, code);
		if (note != null && note.getCodeName() != null) {
			return note.getCodeName();
		}
		return "";
	}

	@Override
	public String getAbr1ByFieldInfo(String srcTable, String srcField, String code) {
		ZtreeDictNote note = findByFieldInfo(srcTable, srcField, code);
		if (note != null && note.getCodeAbr1() != null) {
			return note.getCodeAbr1();
		}
		return "";
	}

	/**
	 * 将db中的codeTable数据缓存到redis
	 * 
	 * @param codeTable
	 * @param listTranscoder
	 * @return
	 */
	@Override
	public List<CfgUmsDataDict> findListFromDbExcludeHidden(String codeTable, boolean refreshCache) {
		Long count = dataDictDao.findCountByCodeTableName(codeTable, false);
		List<CfgUmsDataDict> dataDictList = new ArrayList<CfgUmsDataDict>();
		if (count == null || count <= 0) {
			return dataDictList;
		}

		// 查询前清除下缓存
		dataDictDao.getSession().clear();

		// 1. 分页查询 codeTable 数据
		for (int i = 0; i < count; i += pageSize) {
			List<CfgUmsDataDict> tempList = dataDictDao.findPageByCodeTableName(codeTable, i, pageSize, false);
			dataDictList.addAll(tempList);
		}

		// 查询后清除下缓存
		dataDictDao.getSession().clear();

		if (refreshCache) {
			// 2. 放入缓存
			dataDictRepository.setDataDictList(codeTable, convertDictToTreeNote(dataDictList));
		}

		return dataDictList;
	}

	/**
	 * 通过 表名 以及 字段名 查询对应的 数据字典
	 */
	@Override
	public List<CfgUmsDataDict> findListByFieldInfo(String srcTable, String srcField) {

		if (StringUtils.isEmpty(srcTable) || StringUtils.isEmpty(srcField)) {
			return null;
		}

		// 1. find field infor
		SysFieldInfo fieldInfo = fieldInfoService.findFieldInfo(srcTable, srcField);
		if (fieldInfo == null || StringUtils.isEmpty(fieldInfo.getCodeTable())) {
			return null;
		}

		// 2. getDataDict by codeTable
		return this.findListFromDbExcludeHidden(fieldInfo.getCodeTable(), false);
	}

	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public String refreshCaches(List<String> codeTables) {

		if (dataDictRepository.canNotUse()) {
			return null;
		}

		List<String> codeTableFails = new ArrayList<String>();
		for (String codeTable : codeTables) {

			if (StringUtils.isEmpty(codeTable)) {
				continue;
			}
			try {

				// 1. 删除 缓存
				dataDictRepository.deleteByCodeTable(codeTable);

				// 2 更新 缓存
				findListFromDbExcludeHidden(codeTable, true);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				codeTableFails.add(codeTable);
			}
		}
		if (codeTableFails.size() == 0) {
			return "更新成功";
		}
		StringBuilder message = new StringBuilder();
		for (String codeTable : codeTableFails) {// 获取
			message.append("'" + codeTable + "',");
		}
		return "更新失败的有：[" + message.substring(0, message.lastIndexOf(",")) + "]";
	}

	/**
	 * 新增
	 */
	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public void addDataDict(String codeTable, CfgUmsDataDict dataDict) {

		// 1. 清空 redis
		dataDictRepository.deleteByCodeTable(codeTable);

		// 2. update db
		dataDictDao.addDataDict(codeTable, dataDict);

		// 3. 动态表 清除hibernate 一级缓存
		dataDictDao.getSession().flush();
		dataDictDao.getSession().clear();

	}

	/**
	 * 修改
	 */
	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public void updateDataDict(String codeTable, CfgUmsDataDict dataDict) {

		// 1. 清空 redis
		dataDictRepository.deleteByCodeTable(codeTable);

		// update db
		dataDictDao.updateDataDict(codeTable, dataDict);

		// 动态表 清除hibernate 一级缓存
		dataDictDao.getSession().flush();
		dataDictDao.getSession().clear();
	}

	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public void deleteDataDict(String codeTable, String id) {

		// 1.根据id删除对应的redis
		dataDictRepository.deleteDataDict(codeTable, id);

		// 2.delete db
		dataDictDao.deleteDataDict(codeTable, id);

	}

	/**
	 * 删除父节点和子节点
	 */
	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public void deleteDataDictAll(String codeTable, String id) {

		// 1.根据id删除对应的redis，删除sup_code和code为id的记录
		// 2.直接删除掉codeTable
		dataDictRepository.deleteByCodeTable(codeTable);

		// 2.delete db 同上
		dataDictDao.deleteDataDictAll(codeTable, id);

	}

	public List<ZtreeDictNote> convertDictToTreeNote(List<CfgUmsDataDict> dictList) {

		List<ZtreeDictNote> dictNoteList = new ArrayList<ZtreeDictNote>();
		for (CfgUmsDataDict dict : dictList) {
			dictNoteList.add(convertDictToTreeNote(dict));
		}

		return dictNoteList;
	}

	private ZtreeDictNote convertDictToTreeNote(CfgUmsDataDict dataDict) {

		ZtreeDictNote note = new ZtreeDictNote();
		if (dataDict == null) {
			return note;
		}
		try {
			note.setCode(dataDict.getCode());
			note.setCodeAbr1(dataDict.getCodeAbr1());
			note.setCodeAbr2(dataDict.getCodeAbr2());
			note.setCodeAName(dataDict.getCodeAName());
			note.setCodeLevel(dataDict.getCodeLevel());
			note.setCodeName(dataDict.getCodeName());
			note.setCodeSpelling(dataDict.getCodeSpelling());
			note.setDmGrp(dataDict.getDmGrp());
			note.setInino(dataDict.getInino());
			note.setSupCode(dataDict.getSupCode());
			note.setUnitCode(dataDict.getUnitCode());
			note.setIsCommon(dataDict.getIsCommon());
			note.setCodeNameSuffix(dataDict.getCodeNameSuffix());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return note;
	}

	@Override
	public boolean isCodeUnique(String codeTable, String newCode, String oldCode) {
		if (newCode == null || newCode.equals(oldCode)) {
			return true;
		}
		CfgUmsDataDict dict = dataDictDao.findDataDictByCode(codeTable, newCode);
		return (dict == null);
	}

	@Override
	public List<CfgUmsDataDict> findListFromDbIncludeHidden(String codeTable) {
		Long count = dataDictDao.findCountByCodeTableName(codeTable, true);
		List<CfgUmsDataDict> dataDictList = new ArrayList<CfgUmsDataDict>();
		if (count == null || count <= 0) {
			return dataDictList;
		}

		// 1. 分页查询 codeTable 数据
		for (int i = 0; i < count; i += pageSize) {
			List<CfgUmsDataDict> tempList = dataDictDao.findPageByCodeTableName(codeTable, i, pageSize, true);
			dataDictList.addAll(tempList);
		}

		// 清除下hibernate 缓存
		dataDictDao.getSession().clear();

		// 2. 放入缓存
		// dataDictRepository.setDataDictList(codeTable,
		// convertDictToTreeNote(dataDictList));

		return dataDictList;
	}

	@Override
	public List<CfgUmsDataDict> findListOfZB02() {
		return dataDictDao.findListOfZB02();
	}

	public List<CfgUmsDataDict> findExtByCodeTableName(String codeTable, boolean findHidden) {
		return dataDictDao.findExtByCodeTableName(codeTable, findHidden);
	}

	@Override
	public CfgUmsDataDict findDataDictByCodeName(String codeTable, String codeName) {
		if (codeName == null || StringUtils.isEmpty(codeName.trim())) {
			return null;
		}
		List<CfgUmsDataDict> dataDictList = dataDictDao.findDataDictByCodeName(codeTable, codeName);
		CfgUmsDataDict dataDict = null;
		if (dataDictList != null && !dataDictList.isEmpty() && dataDictList.size() == 1) {
			dataDict = dataDictList.get(0);
		}
		return dataDict;
	}

	@Override
	public CfgUmsDataDict findDataDictByCodeABR1(String codeTable, String codeABR1) {
		if (codeABR1 == null || StringUtils.isEmpty(codeABR1.trim())) {
			return null;
		}
		List<CfgUmsDataDict> dataDictList = dataDictDao.findDataDictByCodeABR1(codeTable, codeABR1);
		CfgUmsDataDict dataDict = null;
		if (dataDictList != null && !dataDictList.isEmpty()) {
			dataDict = dataDictList.get(0);
		}
		return dataDict;
	}

	@Override
	@CacheEvict(value = "dataDict", allEntries = true)
	public void setCommonCode(String codeTable, String[] codes, String isCommon) {
		dataDictRepository.deleteByCodeTable(codeTable);

		int splitSize = 100;// 分割的块大小
		Object[] subAry = splitAry(codes, splitSize);// 分割后的子块数组
		for (int i = 0; i < subAry.length; i++) {
			String[] splitCodes = (String[]) subAry[i];
			dataDictDao.setCommonCode(codeTable, splitCodes, Short.parseShort(isCommon));
			dataDictDao.getSession().flush();
			dataDictDao.getSession().clear();
		}

		// 动态表 清除hibernate 一级缓存
		dataDictDao.getSession().flush();
		dataDictDao.getSession().clear();
	}

	/**
	 * @description 分割数组
	 * @param ary
	 *            要分割的数组
	 * @param subSize
	 *            分割的块大小
	 */
	private static Object[] splitAry(String[] ary, int subSize) {
		int count = ary.length % subSize == 0 ? ary.length / subSize : ary.length / subSize + 1;
		List<List<String>> subAryList = new ArrayList<List<String>>();
		for (int i = 0; i < count; i++) {
			int index = i * subSize;
			List<String> list = new ArrayList<String>();
			int j = 0;
			while (j < subSize && index < ary.length) {
				list.add(ary[index++]);
				j++;
			}
			subAryList.add(list);
		}
		Object[] subAry = new Object[subAryList.size()];
		for (int i = 0; i < subAryList.size(); i++) {
			List<String> subList = subAryList.get(i);
			String[] subAryItem = new String[subList.size()];
			for (int j = 0; j < subList.size(); j++) {
				subAryItem[j] = subList.get(j);
			}
			subAry[i] = subAryItem;
		}
		return subAry;
	}

	@Override
	public List<CfgUmsDataDict> findListByCodeName(String codeTable, String codeName) {
		if (codeName == null || StringUtils.isEmpty(codeName.trim())) {
			return null;
		}
		List<CfgUmsDataDict> dataDictList = dataDictDao.findDataDictByCodeName(codeTable, codeName);
		return dataDictList;
	}

	@Override
	public Map<String, Object> findUnitsByCode(String code, ParamRequest paramRequest) {
		/*Map<String, Object> retuMap = new HashMap<String, Object>();
		CfgUmsDataDict dict = dataDictDao.findDataDictByCode("UNIT_INDEX", code);
		if (dict != null) {
			retuMap.put("name", dict.getCodeName());
			return retuMap;
		} else {
			Map<String, Object> equFilters = new LinkedHashMap<String, Object>();
			equFilters.put("b00", code);
			paramRequest.setEqualFilters(equFilters);
			List<CsmsB01> b01List = csmsB01ServiceImpl.findAllByParamRequest(paramRequest);
			if (b01List != null && b01List.size() > 0) {
				retuMap.put("name", b01List.get(0).getB0101());
				return retuMap;
			}
		}
		return retuMap;*/
		return null;
	}

	@Override
	public List<CfgUmsDataDict> findByUnitCode(String code) {
		List<CfgUmsDataDict> list = dataDictDao.findDataDictByUnitCode("UNIT_INDEX", code);
		return list;
	}

	@Override
	public List<CfgUmsDataDict> findSubCodesByCode(String codeTable, String code) {
		return dataDictDao.findSubCodesByCode(codeTable, code);
	}

}
