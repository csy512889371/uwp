package rongji.report.dao.base;

import java.util.List;

import rongji.framework.base.dao.GenericDao;
import rongji.report.model.ReportTemplate;

public interface ReportTemplateDao extends GenericDao<ReportTemplate> {

	int getNewInino();

	List<String> getReportTemplIdByInino();

}
