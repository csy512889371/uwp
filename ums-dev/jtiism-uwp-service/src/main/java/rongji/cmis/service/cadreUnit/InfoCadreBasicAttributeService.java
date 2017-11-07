package rongji.cmis.service.cadreUnit;

import org.springframework.web.multipart.MultipartFile;
import rongji.cmis.model.sys.SysColumnShow;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Service - 干部管理基本信息
 * 
 * @version 1.0
 */
public interface InfoCadreBasicAttributeService extends BaseService<SysColumnShow> {

	void saveExtraAttr(SysColumnShow attr);

	void updateAPartOfExtra(SysColumnShow attr);

	List<SysColumnShow> findDynEnabledAttrListByInfoSet(String infoSet);

	List<SysColumnShow> findSysColumnShowListByInfoSet(String attrCode);

	List<SysColumnShow> findFiexdAttrListByInfoSet(String infoSet);

	List<SysColumnShow> findInfoSetAttrListByInfoSet(String infoSet);

	void deleteDynaColumnByInfoSet(String attrCode);

	List<SysColumnShow> findExtraEnabledAttrListByInfoSet(String infoSet);

	List<SysColumnShow> findExtraEnabledAttrListByInfoSetWithColumn(String infoSet,Map map,String id);

	List<SysColumnShow> findColumnInfo(String infoSet);

	List<SysColumnShow> getColumnPrivs(List<SysColumnShow> attrs, String fromType);

	ResultModel uploadInfoImg(String colId, String colName,String infoSet, MultipartFile imgFile);
}