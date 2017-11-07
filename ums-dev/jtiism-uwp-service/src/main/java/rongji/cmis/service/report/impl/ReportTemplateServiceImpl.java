package rongji.cmis.service.report.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import rongji.cmis.service.report.ReportTemplateService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.StringUtil;
import rongji.report.dao.base.ReportTemplateDao;
import rongji.report.model.ReportTemplate;

@Service("reportTemplateServiceImpl")
public class ReportTemplateServiceImpl extends BaseServiceImpl<ReportTemplate> implements ReportTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(ReportTemplateServiceImpl.class);

	@Resource(name = "reportTemplateDaoImpl")
	private ReportTemplateDao reportTemplateDao;

	@Override
	public String saveReportTemplate(ReportTemplate reportTemplate, MultipartFile templateFile, String srcPath) {
		InputStream readStream = null;
		FileOutputStream fileOut = null;
		String path = "";
		try {
			readStream = templateFile.getInputStream();
			String templDirPath = srcPath + "/" + UUID.randomUUID().toString();
			File tempDir = new File(templDirPath);
			tempDir.mkdirs();
			path = templDirPath + "/" + templateFile.getOriginalFilename();
			File file = new File(path);
			fileOut = new FileOutputStream(file);
			byte[] data = new byte[1024];
			Integer length = -1;
			while ((length = readStream.read(data)) != -1) {
				fileOut.write(data, 0, length);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (fileOut != null) {
				IOUtils.closeQuietly(fileOut);
			}
			if (readStream != null) {
				IOUtils.closeQuietly(readStream);
			}
		}
		reportTemplate.setRules(StringUtil.formatWrap(reportTemplate.getRules()));
		reportTemplate.setInino(reportTemplateDao.getNewInino());
		reportTemplate.setFilePath(path);
		reportTemplateDao.save(reportTemplate);
		return reportTemplate.getId();
	}

	@Override
	public void updateReportTemplate(ReportTemplate reportTemplate, MultipartFile templateFile, String srcPath) {
		InputStream readStream = null;
		FileOutputStream fileOut = null;
		String path = "";
		ReportTemplate oldReportTemplate = reportTemplateDao.load(reportTemplate.getId());
		oldReportTemplate.setTemplateName(reportTemplate.getTemplateName());
		oldReportTemplate.setIsvalid(reportTemplate.getIsvalid());
		oldReportTemplate.setRules(StringUtil.formatWrap(reportTemplate.getRules()));
		oldReportTemplate.setTemplateType(reportTemplate.getTemplateType());
		if (templateFile != null && templateFile.getSize() > 0) {
			String tempPath = oldReportTemplate.getFilePath();
			try {
				readStream = templateFile.getInputStream();
				String templDirPath = srcPath + "/" + UUID.randomUUID().toString();
				File tempDir = new File(templDirPath);
				tempDir.mkdirs();
				path = templDirPath + "/" + templateFile.getOriginalFilename();
				File file = new File(path);
				fileOut = new FileOutputStream(file);
				byte[] data = new byte[1024];
				Integer length = -1;
				while ((length = readStream.read(data)) != -1) {
					fileOut.write(data, 0, length);
				}
				File oldTemp = new File(tempPath);
				File oldDir = oldTemp.getParentFile();
				oldTemp.delete();
				if (oldDir.isDirectory() && oldDir.listFiles().length == 0) {
					oldDir.delete();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (fileOut != null) {
					IOUtils.closeQuietly(fileOut);
				}
				if (readStream != null) {
					IOUtils.closeQuietly(readStream);
				}
			}
			oldReportTemplate.setFilePath(path);
		}
		reportTemplateDao.merge(oldReportTemplate);
	}

	@Override
	public void sortReportTemplInino(String[] templIds) {
		Integer inino = 1;
		for (String templId : templIds) {
			ReportTemplate templ = reportTemplateDao.load(templId);
			templ.setInino(inino);
			inino++;
		}

	}

	@Override
	public void deleteReportTempl(String templateId) {
		ReportTemplate template = reportTemplateDao.load(templateId);
		File file = new File(template.getFilePath());
		File dir = file.getParentFile();
		file.delete();
		if (dir.isDirectory() && dir.listFiles().length == 0) {
			dir.delete();
		}
		reportTemplateDao.delete(template);
		List<String> ids = reportTemplateDao.getReportTemplIdByInino();
		ids.remove(templateId);
		if (ids != null && !ids.isEmpty()) {
			sortReportTemplInino(ids.toArray(new String[] {}));
		}

	}
}
