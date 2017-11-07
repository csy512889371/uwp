package rongji.cmis.service.system;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface ExportAndImportDataService {

	List<Map<String, Object>> getCheckImportCadreInfo(File file);

	void executeImport(File file, String[] a0000s, String[] models) throws Exception;

	void putFileCache(MultipartFile importPath,File file) throws Exception;

	void exportCadreData(List<Map<String, Object>> exportDataList,File exportFile) throws Exception;

	void getImportCadreInfo(File file, HttpServletRequest request);

	void exportData(File exportDataList, File exportFile)
			throws Exception;

	void putZipCache(MultipartFile importPath, File file) throws Exception;

	void uploadPhoto(String photoNameOld,String photoNameNew) throws Exception;

	String getCadreInfo(List<Map<String, Object>> exportDataList)
			throws Exception;

}
