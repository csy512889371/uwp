package rongji.cmis.dao.common.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.common.DataDictDao;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.framework.base.dao.impl.GenericDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @Title: 对应中组织部颁标准代码
 * @version V1.0
 */
@Repository("dataDictDaoImpl")
public class DataDictDaoImpl extends GenericDaoImpl<CfgUmsDataDict> implements DataDictDao {

	/**
	 * 查询 条数
	 */
	public Long findCountByCodeTableName(String codeTable, boolean findHidden) {

		String sql = "select count(1) from " + codeTable;

		if (!findHidden) {
			//sql = sql + " where yesprv = 1";
		}

		SQLQuery query = this.getSession().createSQLQuery(sql);
		String countStr = query.uniqueResult().toString();
		return Long.valueOf(countStr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findPageByCodeTableName(String codeTable, final int offset, final int pageSize, boolean findHidden) {
		List<CfgUmsDataDict> datas = null;
		String sql = "select * from  " + codeTable;
		if (!findHidden) {
			sql = sql + " where yesprv = 1 and INVALID = 1 ";
		}

		sql = sql + " ORDER BY ININO ASC ";

		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addEntity(CfgUmsDataDict.class).setFirstResult(offset).setMaxResults(pageSize);

		datas = query.list();
		return datas;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findExtByCodeTableName(String codeTable, boolean findHidden) {
		List<CfgUmsDataDict> datas = null;
		String sql = "select * from  " + codeTable;
		if (!findHidden) {
			sql = sql + " where yesprv = 1 and INVALID = 1 and IS_STAND = 1";
		} else {
			sql = sql + " where IS_STAND = 1";
		}
		sql = sql + " ORDER BY ININO ASC ";

		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addEntity(CfgUmsDataDict.class);

		datas = query.list();
		return datas;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findListOfZB02() {
		StringBuffer sqlBuff = new StringBuffer("select CODE, CODE_NAME, CODE_ABR1, CODE_ABR2, SUP_CODE, CODE_BSPELLING, ININO, ");
		sqlBuff.append(" CODE_LEVEL , CODE_ANAME , CODE_LEAF, CODE_ASSIST, INVALID, DmLevCod, DmGrp, YesPrv, Attribute, IS_COMMON, IS_STAND");
		sqlBuff.append(", UNIT_CODE").append(" from UNIT_INDEX ");
		Query query = this.getSession().createSQLQuery(sqlBuff.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		List<CfgUmsDataDict> list = new ArrayList<CfgUmsDataDict>();
		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, Object> entityMap : resultList) {
				CfgUmsDataDict dataDict = new CfgUmsDataDict();
				for (Entry<String, Object> entry : entityMap.entrySet()) {
					String keyName = entry.getKey();
					Object value = entry.getValue();
					if (value == null) {
						continue;
					}

					if ("CODE".equals(keyName)) {
						dataDict.setCode((String) value);
					}

					if ("CODE_NAME".equals(keyName)) {
						dataDict.setCodeName((String) value);
					}

					if ("CODE_ABR1".equals(keyName)) {
						dataDict.setCodeAbr1((String) value);
					}

					if ("CODE_ABR2".equals(keyName)) {
						dataDict.setCodeAbr2((String) value);
					}

					if ("SUP_CODE".equals(keyName)) {
						dataDict.setSupCode((String) value);
					}
					
					if("UNIT_CODE".equals(keyName)){
						dataDict.setUnitCode((String) value);
					}

					if ("CODE_BSPELLING".equals(keyName)) {
						dataDict.setCodeSpelling((String) value);
					}

					if ("ININO".equals(keyName)) {
						if (value instanceof Integer) {
							dataDict.setInino((Integer) value);
						} else if (value instanceof Double) {
							Double inino = (Double) value;
							dataDict.setInino(inino.intValue());
						}
					}

					if ("CODE_ANAME".equals(keyName)) {
						dataDict.setCodeAName((String) value);
					}

					if ("CODE_LEVEL".equals(keyName)) {
						if (value instanceof Double) {
							dataDict.setCodeLevel((Double) value);
						}
					}

					if ("CODE_LEAF".equals(keyName)) {
						dataDict.setCodeLeaf((String) value);
					}

					if ("CODE_ASSIST".equals(keyName)) {
						dataDict.setCodeAssist((String) value);
					}

					if ("INVALID".equals(keyName)) {
						dataDict.setInvalid((String) value);
					}

					if ("DmLevCod".equals(keyName)) {
						dataDict.setDmLevCod((String) value);
					}

					if ("DmGrp".equals(keyName)) {
						dataDict.setDmGrp((String) value);
					}

					if ("YesPrv".equals(keyName)) {
						if (value instanceof Byte) {
							Byte yesPrv = (Byte) value;
							dataDict.setYesPrv(Short.valueOf(yesPrv.toString()));
						}
					}

					if ("Attribute".equals(keyName)) {
						dataDict.setAttribute((Integer) value);
					}

					if ("IS_COMMON".equals(keyName)) {
						if (value instanceof Byte) {
							Byte isCommon = (Byte) value;
							dataDict.setIsCommon(Short.valueOf(isCommon.toString()));
						}
					}
					
					if ("IS_STAND".equals(keyName)) {
						if (value instanceof Byte) {
							Byte isStand = (Byte) value;
							dataDict.setIsStand(Short.valueOf(isStand.toString()));
						}
					}
				}
				list.add(dataDict);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CfgUmsDataDict findDataDictByCode(String codeTable, String code) {
		StringBuffer sqlBuff = new StringBuffer("select CODE, CODE_NAME, CODE_ABR1, CODE_ABR2, SUP_CODE, CODE_SPELLING, ININO, ");
		sqlBuff.append(" CODE_LEVEL , CODE_ANAME , CODE_LEAF, CODE_ASSIST, INVALID, DmLevCod, DmGrp, YesPrv, Attribute, IS_COMMON, IS_STAND");
		if("ZB02".equals(codeTable.toUpperCase())) sqlBuff.append(", UNIT_CODE");
		sqlBuff.append(" from  ").append(codeTable).append("  where code ='").append(code).append("'");

		Query query = this.getSession().createSQLQuery(sqlBuff.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<Map<String, Object>> resultList = query.list();
		CfgUmsDataDict dataDict = null;
		if (resultList != null && !resultList.isEmpty()) {
			dataDict = new CfgUmsDataDict();
			Map<String, Object> entityMap = resultList.get(0);
			for (Entry<String, Object> entry : entityMap.entrySet()) {
				String keyName = entry.getKey();
				Object value = entry.getValue();
				if (value == null) {
					continue;
				}

				if ("CODE".equals(keyName)) {
					dataDict.setCode((String) value);
				}

				if ("CODE_NAME".equals(keyName)) {
					dataDict.setCodeName((String) value);
				}

				if ("CODE_ABR1".equals(keyName)) {
					dataDict.setCodeAbr1((String) value);
				}

				if ("CODE_ABR2".equals(keyName)) {
					dataDict.setCodeAbr2((String) value);
				}

				if ("SUP_CODE".equals(keyName)) {
					dataDict.setSupCode((String) value);
				}
				
				if("UNIT_CODE".equals(keyName)){
					dataDict.setUnitCode((String) value);
				}

				if ("CODE_SPELLING".equals(keyName)) {
					dataDict.setCodeSpelling((String) value);
				}

				if ("ININO".equals(keyName)) {
					if (value instanceof Integer) {
						dataDict.setInino((Integer) value);
					} else if (value instanceof Double) {
						Double inino = (Double) value;
						dataDict.setInino(inino.intValue());
					}
				}

				if ("CODE_ANAME".equals(keyName)) {
					dataDict.setCodeAName((String) value);
				}

				if ("CODE_LEVEL".equals(keyName)) {
					if (value instanceof Double) {
						dataDict.setCodeLevel((Double) value);
					}
				}

				if ("CODE_LEAF".equals(keyName)) {
					dataDict.setCodeLeaf((String) value);
				}

				if ("CODE_ASSIST".equals(keyName)) {
					dataDict.setCodeAssist((String) value);
				}

				if ("INVALID".equals(keyName)) {
					dataDict.setInvalid((String) value);
				}

				if ("DmLevCod".equals(keyName)) {
					dataDict.setDmLevCod((String) value);
				}

				if ("DmGrp".equals(keyName)) {
					dataDict.setDmGrp((String) value);
				}

				if ("YesPrv".equals(keyName)) {
					if (value instanceof Byte) {
						Byte yesPrv = (Byte) value;
						dataDict.setYesPrv(Short.valueOf(yesPrv.toString()));
					}
				}

				if ("Attribute".equals(keyName)) {
					dataDict.setAttribute((Integer) value);
				}

				if ("IS_COMMON".equals(keyName)) {
					if (value instanceof Byte) {
						Byte isCommon = (Byte) value;
						dataDict.setIsCommon(Short.valueOf(isCommon.toString()));
					}
				}
				
				if ("IS_STAND".equals(keyName)) {
					if (value instanceof Byte) {
						Byte isStand = (Byte) value;
						dataDict.setIsStand(Short.valueOf(isStand.toString()));
					}
				}
				

			}
		}
		return dataDict;
	}

	/**
	 * 新增
	 */
	@Override
	public void addDataDict(String codeTable, CfgUmsDataDict dataDict) {
		String sql = "insert into " + codeTable + " (DmGrp,YesPrv,CODE, CODE_ABR1,CODE_ABR2, CODE_NAME,CODE_ANAME, CODE_SPELLING, CODE_LEAF,SUP_CODE, CODE_ASSIST, INVALID, START_DATE, STOP_DATE, IS_STAND) "
				+ " values (:dmGrp,:yesPrv,:code,:codeAbr1,:codeAbr2,:codeName,:codeAName,:codeSpelling,:codeLeaf,:supCode,:codeAssist,:invalid,:startDate,:stopDate,:isStand)";

		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setString("dmGrp", dataDict.getDmGrp());
		query.setLong("yesPrv", dataDict.getYesPrv());
		query.setString("code", dataDict.getCode());
		query.setString("codeAbr1", dataDict.getCodeAbr1());
		query.setString("codeAbr2", dataDict.getCodeAbr1());
		query.setString("codeName", dataDict.getCodeName());
		query.setString("codeAName", dataDict.getCodeName());
		query.setString("codeSpelling", dataDict.getCodeSpelling());
		// query.setDouble("codeLevel", dataDict.getCodeLevel());
		query.setString("codeLeaf", dataDict.getCodeLeaf());
		query.setString("supCode", dataDict.getSupCode());
		query.setString("codeAssist", dataDict.getCodeAssist());
		query.setString("invalid", dataDict.getInvalid());
		query.setString("startDate", dataDict.getStartDate());
		query.setString("stopDate", dataDict.getStopDate());
		query.setShort("isStand", dataDict.getIsStand());
		query.executeUpdate();
	}

	/**
	 * 修改
	 */
	@Override
	public void updateDataDict(String codeTable, CfgUmsDataDict dataDict) {
		String sql = " update " + codeTable + " set CODE_NAME = :codeName, YesPrv=:yesPrv,CODE_ABR1=:codeAdr1,CODE_ABR2=:codeAdr1, INVALID=:invalid where CODE = :code";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("codeName", dataDict.getCodeName());
		query.setParameter("code", dataDict.getCode());
		query.setParameter("yesPrv", dataDict.getYesPrv());
		query.setParameter("codeAdr1", dataDict.getCodeAbr1());
		query.setParameter("invalid", dataDict.getInvalid());
		query.executeUpdate();
	}

	@Override
	public void deleteDataDict(String codeTable, String codeId) {
		String sql = "delete from " + codeTable + " where CODE = '" + codeId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	/**
	 * 删除父类和子类
	 */
	@Override
	public void deleteDataDictAll(String codeTable, String codeId) {
		StringBuffer sql = new StringBuffer(" delete ").append(codeTable).append(" where CODE = :codeId ").append("or SUP_CODE = :codeId");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("codeId", codeId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findDataDictByCodeName(String codeTable, String codeName) {
		StringBuffer sqlBuff = new StringBuffer("select CODE, CODE_NAME, CODE_ABR1, CODE_ABR2, SUP_CODE, CODE_BSPELLING, ININO, ");
		sqlBuff.append(" CODE_LEVEL , CODE_ANAME , CODE_LEAF, CODE_ASSIST, INVALID, DmLevCod, DmGrp, YesPrv, Attribute");
		sqlBuff.append(" from  ").append(codeTable).append("  where CODE_Name ='").append(codeName).append("' order by ININO");
		Query query = this.getSession().createSQLQuery(sqlBuff.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		List<CfgUmsDataDict> dataDictList = new ArrayList<CfgUmsDataDict>();
		CfgUmsDataDict dataDict = null;
		if (resultList != null && !resultList.isEmpty()) {
            for (Map<String, Object> aResultList : resultList) {
                dataDict = new CfgUmsDataDict();
                Map<String, Object> entityMap = aResultList;
                for (Entry<String, Object> entry : entityMap.entrySet()) {
                    String keyName = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }

                    if ("CODE".equals(keyName)) {
                        dataDict.setCode((String) value);
                    }

                    if ("CODE_NAME".equals(keyName)) {
                        dataDict.setCodeName((String) value);
                    }

                    if ("CODE_ABR1".equals(keyName)) {
                        dataDict.setCodeAbr1((String) value);
                    }

                    if ("CODE_ABR2".equals(keyName)) {
                        dataDict.setCodeAbr2((String) value);
                    }

                    if ("SUP_CODE".equals(keyName)) {
                        dataDict.setSupCode((String) value);
                    }

                    if ("CODE_BSPELLING".equals(keyName)) {
                        dataDict.setCodeSpelling((String) value);
                    }

                    if ("ININO".equals(keyName)) {
                        if (value instanceof Integer) {
                            dataDict.setInino((Integer) value);
                        } else if (value instanceof Double) {
                            Double inino = (Double) value;
                            dataDict.setInino(inino.intValue());
                        }
                    }

                    if ("CODE_ANAME".equals(keyName)) {
                        dataDict.setCodeAName((String) value);
                    }

                    if ("CODE_LEVEL".equals(keyName)) {
                        if (value instanceof Double) {
                            dataDict.setCodeLevel((Double) value);
                        }
                    }

                    if ("CODE_LEAF".equals(keyName)) {
                        dataDict.setCodeLeaf((String) value);
                    }

                    if ("CODE_ASSIST".equals(keyName)) {
                        dataDict.setCodeAssist((String) value);
                    }

                    if ("INVALID".equals(keyName)) {
                        dataDict.setInvalid((String) value);
                    }

                    if ("DmLevCod".equals(keyName)) {
                        dataDict.setDmLevCod((String) value);
                    }

                    if ("DmGrp".equals(keyName)) {
                        dataDict.setDmGrp((String) value);
                    }

                    if ("YesPrv".equals(keyName)) {
                        if (value instanceof Byte) {
                            Byte yesPrv = (Byte) value;
                            dataDict.setYesPrv(Short.valueOf(yesPrv.toString()));
                        }
                    }

                    if ("Attribute".equals(keyName)) {
                        dataDict.setAttribute((Integer) value);
                    }

                }
                dataDictList.add(dataDict);
            }
		}
		return dataDictList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findDataDictByCodeABR1(String codeTable, String codeABR1) {
		StringBuffer sqlBuff = new StringBuffer("select CODE, CODE_NAME, CODE_ABR1, CODE_ABR2, SUP_CODE, CODE_BSPELLING, ININO, ");
		sqlBuff.append(" CODE_LEVEL , CODE_ANAME , CODE_LEAF, CODE_ASSIST, INVALID, DmLevCod, DmGrp, YesPrv, Attribute");
		sqlBuff.append(" from  ").append(codeTable).append("  where CODE_ABR1 ='").append(codeABR1).append("' order by ININO");
		Query query = this.getSession().createSQLQuery(sqlBuff.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		List<CfgUmsDataDict> dataDictList = new ArrayList<CfgUmsDataDict>();
		CfgUmsDataDict dataDict = null;
		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, Object> aResultList : resultList) {
				dataDict = new CfgUmsDataDict();
				Map<String, Object> entityMap = aResultList;
				for (Entry<String, Object> entry : entityMap.entrySet()) {
					String keyName = entry.getKey();
					Object value = entry.getValue();
					if (value == null) {
						continue;
					}

					if ("CODE".equals(keyName)) {
						dataDict.setCode((String) value);
					}

					if ("CODE_NAME".equals(keyName)) {
						dataDict.setCodeName((String) value);
					}

					if ("CODE_ABR1".equals(keyName)) {
						dataDict.setCodeAbr1((String) value);
					}

					if ("CODE_ABR2".equals(keyName)) {
						dataDict.setCodeAbr2((String) value);
					}

					if ("SUP_CODE".equals(keyName)) {
						dataDict.setSupCode((String) value);
					}

					if ("CODE_BSPELLING".equals(keyName)) {
						dataDict.setCodeSpelling((String) value);
					}

					if ("ININO".equals(keyName)) {
						if (value instanceof Integer) {
							dataDict.setInino((Integer) value);
						} else if (value instanceof Double) {
							Double inino = (Double) value;
							dataDict.setInino(inino.intValue());
						}
					}

					if ("CODE_ANAME".equals(keyName)) {
						dataDict.setCodeAName((String) value);
					}

					if ("CODE_LEVEL".equals(keyName)) {
						if (value instanceof Double) {
							dataDict.setCodeLevel((Double) value);
						}
					}

					if ("CODE_LEAF".equals(keyName)) {
						dataDict.setCodeLeaf((String) value);
					}

					if ("CODE_ASSIST".equals(keyName)) {
						dataDict.setCodeAssist((String) value);
					}

					if ("INVALID".equals(keyName)) {
						dataDict.setInvalid((String) value);
					}

					if ("DmLevCod".equals(keyName)) {
						dataDict.setDmLevCod((String) value);
					}

					if ("DmGrp".equals(keyName)) {
						dataDict.setDmGrp((String) value);
					}

					if ("YesPrv".equals(keyName)) {
						if (value instanceof Byte) {
							Byte yesPrv = (Byte) value;
							dataDict.setYesPrv(Short.valueOf(yesPrv.toString()));
						}
					}

					if ("Attribute".equals(keyName)) {
						dataDict.setAttribute((Integer) value);
					}

				}
				dataDictList.add(dataDict);
			}
		}
		return dataDictList;
	}

	@Override
	public void setCommonCode(String codeTable, String[] codes, short isCommon) {
		String sql = " update " + codeTable + " set IS_COMMON = :isCommon where CODE in (:codes) ";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("isCommon", isCommon);
		query.setParameterList("codes", codes);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findDataDictByUnitCode(String codeTable, String unitCode) {
		List<CfgUmsDataDict> datas = new ArrayList<CfgUmsDataDict>();
		if (unitCode != null && !"".equals(unitCode)) {
			String sql = "select * from  " + codeTable + " where unit_code = '" + unitCode + "'";
			SQLQuery query = this.getSession().createSQLQuery(sql);
			datas = query.list();
		}
		return datas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> findSubCodesByCode(String codeTable, String code) {
		List<CfgUmsDataDict> dataDictList = new ArrayList<CfgUmsDataDict>();
		String sql = "SELECT * FROM " + codeTable + " WHERE SUP_CODE = '" + code + "'";
		if (StringUtils.isEmpty(code)) {
			return dataDictList;
		}
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> resultList = query.list();
		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, Object> entityMap : resultList) {
				CfgUmsDataDict dataDict = new CfgUmsDataDict();
				for (Entry<String, Object> entry : entityMap.entrySet()) {
					String keyName = entry.getKey();
					Object value = entry.getValue();
					if (value == null) {
						continue;
					}

					if ("CODE".equals(keyName)) {
						dataDict.setCode((String) value);
					}

					if ("CODE_NAME".equals(keyName)) {
						dataDict.setCodeName((String) value);
					}

					if ("CODE_ABR1".equals(keyName)) {
						dataDict.setCodeAbr1((String) value);
					}

					if ("CODE_ABR2".equals(keyName)) {
						dataDict.setCodeAbr2((String) value);
					}

					if ("SUP_CODE".equals(keyName)) {
						dataDict.setSupCode((String) value);
					}
					
					if("UNIT_CODE".equals(keyName)){
						dataDict.setUnitCode((String) value);
					}

					if ("CODE_SPELLING".equals(keyName)) {
						dataDict.setCodeSpelling((String) value);
					}

					if ("ININO".equals(keyName)) {
						if (value instanceof Integer) {
							dataDict.setInino((Integer) value);
						} else if (value instanceof Double) {
							Double inino = (Double) value;
							dataDict.setInino(inino.intValue());
						}
					}

					if ("CODE_ANAME".equals(keyName)) {
						dataDict.setCodeAName((String) value);
					}

					if ("CODE_LEVEL".equals(keyName)) {
						if (value instanceof Double) {
							dataDict.setCodeLevel((Double) value);
						}
					}

					if ("CODE_LEAF".equals(keyName)) {
						dataDict.setCodeLeaf((String) value);
					}

					if ("CODE_ASSIST".equals(keyName)) {
						dataDict.setCodeAssist((String) value);
					}

					if ("INVALID".equals(keyName)) {
						dataDict.setInvalid((String) value);
					}

					if ("DmLevCod".equals(keyName)) {
						dataDict.setDmLevCod((String) value);
					}

					if ("DmGrp".equals(keyName)) {
						dataDict.setDmGrp((String) value);
					}

					if ("YesPrv".equals(keyName)) {
						if (value instanceof Byte) {
							Byte yesPrv = (Byte) value;
							dataDict.setYesPrv(Short.valueOf(yesPrv.toString()));
						}
					}

					if ("Attribute".equals(keyName)) {
						dataDict.setAttribute((Integer) value);
					}

					if ("IS_COMMON".equals(keyName)) {
						if (value instanceof Byte) {
							Byte isCommon = (Byte) value;
							dataDict.setIsCommon(Short.valueOf(isCommon.toString()));
						}
					}

					if ("IS_STAND".equals(keyName)) {
						if (value instanceof Byte) {
							Byte isStand = (Byte) value;
							dataDict.setIsStand(Short.valueOf(isStand.toString()));
						}
					}
				}
				dataDictList.add(dataDict);
			}
		}
		return dataDictList;
	}

}
