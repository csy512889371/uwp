package rongji.report.dao.base.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.report.dao.base.ReportTemplateDao;
import rongji.report.model.ReportTemplate;

@Repository("reportTemplateDaoImpl")
public class ReportTemplateDaoImpl extends GenericDaoImpl<ReportTemplate> implements ReportTemplateDao {

	@Override
	public int getNewInino() {
		String sql = "select max(inino) from REPORT_TEMPLATE ";
		Query query = this.getSession().createSQLQuery(sql);
		Integer inino = (Integer) query.uniqueResult();
		if (inino == null) {
			inino = 0;
		}
		return inino + 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReportTemplIdByInino() {
		String sql = "select REPORT_TEMPLATE_ID from REPORT_TEMPLATE order by inino ";
		Query query = this.getSession().createSQLQuery(sql);
		return query.list();
	}

}
