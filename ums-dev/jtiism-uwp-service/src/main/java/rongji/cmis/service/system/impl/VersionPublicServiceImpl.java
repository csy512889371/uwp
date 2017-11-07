package rongji.cmis.service.system.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rongji.cmis.dao.system.ScriptVersionDao;
import rongji.cmis.dao.system.VersionPublicDao;
import rongji.cmis.dao.system.VersionPublicDetDao;
import rongji.cmis.model.sys.ScriptVersion;
import rongji.cmis.model.sys.VersionPublic;
import rongji.cmis.model.sys.VersionPublicDet;
import rongji.cmis.service.system.VersionPublicService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

@Service("versionPublicServiceImpl")
public class VersionPublicServiceImpl extends BaseServiceImpl<VersionPublic> implements VersionPublicService {

	private static final Logger logger = LoggerFactory.getLogger(VersionPublicServiceImpl.class);

	@Resource(name = "versionPublicDaoImpl")
	private VersionPublicDao versionPublicDao;

	@Resource(name = "versionPublicDetDaoImpl")
	private VersionPublicDetDao versionPublicDetDao;

	@Resource(name = "scriptVersionDaoImpl")
	private ScriptVersionDao scriptVersionDao;

	@Override
	public VersionPublic saveVersionPublic(VersionPublic versionPublic, String selectedSeqnos) {

		versionPublic = (VersionPublic) versionPublicDao.merge(versionPublic);
		if (StringUtil.isNotEmpty(selectedSeqnos)) {
			String[] seqnoArr = selectedSeqnos.split(",");
			VersionPublicDet versionPublicDet = null;
			for (String seqno : seqnoArr) {
				if (StringUtil.isNotEmpty(seqno)) {
					versionPublicDet = new VersionPublicDet();
					versionPublicDet.setScriptVersion(scriptVersionDao.find(Integer.parseInt(seqno)));
					versionPublicDet.setVersionPublic(versionPublic);
					versionPublicDetDao.save(versionPublicDet);
				}
			}
		}
		return versionPublic;
	}

	@Override
	public VersionPublic updateVersionPublic(VersionPublic versionPublic, String selectedSeqnos) {

		versionPublic = (VersionPublic) versionPublicDao.merge(versionPublic);
		if (versionPublic != null && null != versionPublic.getPublicId()) {
			versionPublicDetDao.deleteByPublicId(versionPublic.getPublicId());
		}
		if (StringUtil.isNotEmpty(selectedSeqnos)) {
			String[] seqnoArr = selectedSeqnos.split(",");
			VersionPublicDet versionPublicDet = null;
			for (String seqno : seqnoArr) {
				if (StringUtil.isNotEmpty(seqno)) {

					versionPublicDet = new VersionPublicDet();
					versionPublicDet.setScriptVersion(scriptVersionDao.find(Integer.parseInt(seqno)));
					versionPublicDet.setVersionPublic(versionPublic);
					versionPublicDetDao.save(versionPublicDet);
				}
			}
		}
		return versionPublic;
	}

	@Override
	public void delVersionPublic(String publicIds) {
		if (StringUtil.isNotEmpty(publicIds)) {
			String[] publicIdArr = publicIds.split(",");
			for (String publicId : publicIdArr) {
				versionPublicDetDao.deleteByPublicId(Integer.parseInt(publicId));
				versionPublicDao.delete(Integer.parseInt(publicId));
			}
		}
	}

	@Override
	public String expVersionPublic(String publicIds, HttpServletRequest request) {
		BufferedWriter bw = null;
		FileOutputStream fileOut = null;
		String exportFile = "";
		ParamRequest paramRequest = new ParamRequest();
		try {

			String folderName = DocResourceUtils.getDocFolderPath(request);
			String relFolderName = SettingUtils.get().getDynaReportTempDoc();
			if (StringUtils.isNotEmpty(relFolderName)) {
				folderName = DocResourceUtils.getResourceFilePath(relFolderName);
			}

			List<Integer> publicIdList = new ArrayList<Integer>();
			String excelExtendName = "";
			if (StringUtil.isNotEmpty(publicIds)) {
				excelExtendName += "[";
				String[] publicIdArr = publicIds.split(",");
				for (String str : publicIdArr) {
					VersionPublic vp2 = versionPublicDao.find(Integer.parseInt(str));
					if (null != vp2 && StringUtil.isNotEmpty(vp2.getVersionNo())) {
						excelExtendName += vp2.getVersionNo() + "、";
					}
					publicIdList.add(Integer.parseInt(str));
				}
			}
			if (excelExtendName.length() > 2) {
				excelExtendName = excelExtendName.substring(0, excelExtendName.length() - 1) + "]";
			}
			String excelName = "版本号：" + excelExtendName + "执行脚本.txt";
			File excelFile = new File(folderName + "/" + excelName);
			excelFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(excelFile));
			Map<String, Object> inFilters = new LinkedHashMap<String, Object>();
			inFilters.put("publicId", publicIdList);
			paramRequest.setInFilters(inFilters);

			List<VersionPublic> versionPublicList = versionPublicDao.findAllByParamRequest(paramRequest);
			for (VersionPublic vp : versionPublicList) {
				String topStr = "";
				topStr += "------------------------------------以下是   " + vp.getVersionNo() + "  版本所有脚本------------------------------------" + "\r\n";
				topStr += "/*" + "\r\n";
				topStr += "  发布流水:" + vp.getPublicId() + "\r\n";
				topStr += "  发布日期:" + DateUtil.formatDateToStringWithNull(vp.getPubDate(), "yyyy-MM-dd HH:mm:ss") + "\r\n";
				topStr += "  发布说明:" + vp.getPubDesc() + "\r\n";
				topStr += "  备注:" + vp.getMemo() + "\r\n";
				String publicType = "";
				if (0 == vp.getPubType()) {// FZ
					publicType = "FZ";
				} else if (1 == vp.getPubType()) {// FJ
					publicType = "FJ";
				}
				topStr += "  版本类别:" + publicType + "\r\n";
				topStr += "  版本号:" + vp.getVersionNo() + "\r\n";
				topStr += "*/" + "\r\n";
				bw.write(topStr);
				Integer scriptType = vp.getScriptType();// 脚本类型
				Integer startNo = null;//开始脚本序号
				Integer endNo = null;//结束脚本序号
//				List<ScriptVersion> scriptVersionList = scriptVersionDao.getScriptVerionBySelectedPublicId(vp.getPublicId());
				List<ScriptVersion> svStructList = scriptVersionDao.getScriptVerionBySelectPubIdAndChnType(vp.getPublicId(),0);
				List<ScriptVersion> svDataList = scriptVersionDao.getScriptVerionBySelectPubIdAndChnType(vp.getPublicId(),1);
				for (ScriptVersion sv : svStructList) {
					if (null == startNo) {
						startNo = sv.getSeqno();
					} else if (startNo > sv.getSeqno()) {
						startNo = sv.getSeqno();
					}
					if (null == endNo) {
						endNo = sv.getSeqno();
					} else if (endNo < sv.getSeqno()) {
						endNo = sv.getSeqno();
					}
					String scriptVersionStr = "";
					scriptVersionStr += "----------------------------------------------------------" + "\r\n";
					scriptVersionStr += "--  序列号:" + sv.getSeqno() + "  变更说明:" + sv.getChnDesc() + "  变更时间:" + DateUtil.formatDateToStringWithNull(sv.getChnTime(), "yyyy-MM-dd HH:mm:ss") + "\r\n";
					scriptVersionStr += "----------------------------------------------------------" + "\r\n";
					bw.write(scriptVersionStr);
					if (null != scriptType && 1 == scriptType) {// 桌面Access
						if (StringUtil.isNotEmpty(sv.getChnScriptAccess())) {
							bw.write(sv.getChnScriptAccess() + "\r\n");
						}
					} else {// WEB sql server
						if (StringUtil.isNotEmpty(sv.getChnScript())) {
							bw.write(sv.getChnScript() + "\r\n");
						}
					}
				}
				bw.write("/***********************************************************************/\r\n\r\n");
				for (ScriptVersion sv : svDataList) {
					if (null == startNo) {
						startNo = sv.getSeqno();
					} else if (startNo > sv.getSeqno()) {
						startNo = sv.getSeqno();
					}
					if (null == endNo) {
						endNo = sv.getSeqno();
					} else if (endNo < sv.getSeqno()) {
						endNo = sv.getSeqno();
					}
					String scriptVersionStr = "";
					scriptVersionStr += "----------------------------------------------------------" + "\r\n";
					scriptVersionStr += "--  序列号:" + sv.getSeqno() + "  变更说明:" + sv.getChnDesc() + "  变更时间:" + DateUtil.formatDateToStringWithNull(sv.getChnTime(), "yyyy-MM-dd HH:mm:ss") + "\r\n";
					scriptVersionStr += "----------------------------------------------------------" + "\r\n";
					bw.write(scriptVersionStr);
					if (null != scriptType && 1 == scriptType) {// 桌面Access
						if (StringUtil.isNotEmpty(sv.getChnScriptAccess())) {
							bw.write(sv.getChnScriptAccess() + "\r\n");
						}
					} else {// WEB sql server
						if (StringUtil.isNotEmpty(sv.getChnScript())) {
							bw.write(sv.getChnScript() + "\r\n");
						}
					}
				}
				String dbChnHisStr = "";//库版本更新历史脚本信息
				dbChnHisStr += "--  以下库版本更新历史插入数据" + "\r\n";
				if (null != scriptType && 1 == scriptType) {// 桌面Access
					dbChnHisStr += "INSERT INTO DB_CHN_HIS (SEQ_START_NO,SEQ_END_NO,CHN_DESC,CHN_TIME) VALUES(" + startNo + "," + endNo + ",'" + vp.getVersionNo() + "," + vp.getPubDesc() + "',date()&' '& format(time(),'HH:mm:ss'));" + "\r\n";
				} else {// WEB sql server
					dbChnHisStr += "INSERT INTO DB_CHN_HIS (SEQ_START_NO,SEQ_END_NO,CHN_DESC,CHN_TIME) VALUES(" + startNo + "," + endNo + ",'" + vp.getVersionNo() + "," + vp.getPubDesc() + "',getdate()); \r\n GO" + "\r\n";
				}
				dbChnHisStr += "--  以上库版本更新历史插入数据" + "\r\n";
				bw.write(dbChnHisStr);
				bw.write("------------------------------------以上是   " + vp.getVersionNo() + "  版本所有脚本------------------------------------" + "\r\n");
			}
			bw.flush();
			exportFile = excelFile.getName();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (bw != null) {
				IOUtils.closeQuietly(bw);
			}
			if (fileOut != null) {
				IOUtils.closeQuietly(fileOut);
			}
		}
		return exportFile;
	}

	/**
	 *
	 * @Title: getTodayCacheExportFolderName
	 * @Description: 获取当天任免表导出的缓存文件夹名
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	public String getTodayCacheExportFolderName(HttpServletRequest request) {
		Date date = new Date();
		String todFolderStr = DateUtil.formatDateToStringWithNull(date, "yyyy-MM-dd");
		File todFolder = new File(getClassPath(request) + todFolderStr);
		if (!todFolder.exists() && !todFolder.isDirectory()) {
			todFolder.mkdirs();
		}
		return todFolderStr;
	}

	/**
	 * @Description: (获取classes/template的路径)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	private static String getClassPath(HttpServletRequest request) {
		String path = DocResourceUtils.getResourceFilePath("resource/doc") + "/";
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return path;
	}
}
