package rongji.cmis.service.report;

import org.springframework.web.multipart.MultipartFile;

import rongji.framework.base.service.BaseService;
import rongji.report.model.ReportTemplate;

public interface ReportTemplateService  extends BaseService<ReportTemplate>{

	String saveReportTemplate(ReportTemplate reportTemplate, MultipartFile templateFile, String srcPath);

	void updateReportTemplate(ReportTemplate reportTemplate, MultipartFile templateFile, String srcPath);

	void sortReportTemplInino(String[] templIds);

	void deleteReportTempl(String templateId);

}
