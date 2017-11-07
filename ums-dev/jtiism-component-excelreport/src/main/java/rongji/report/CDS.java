package rongji.report;

import java.util.List;

public interface CDS {
	void setObject();

	void fillEach(Integer step, Integer max, String[] names);

	void fill(String[] args);

	void setPicture();

	CDSBaseImpl autoFit(String target, String width, String height);

	CDSBaseImpl data();

	CDSBaseImpl getParam(String name);

	CDSBaseImpl index(Integer index);

	CDSBaseImpl sql(String sql);

	CDSBaseImpl get(String name);

	CDSBaseImpl separator(String separator);

	CDSBaseImpl dateFormate(String pattern);

	CDSBaseImpl dateFormate(String name, String pattern);

	CDSBaseImpl cache(String name);

	CDSBaseImpl setCell(Object val);

	CDSBaseImpl autoHeight(String target);

	CDSBaseImpl autoFit(String target, Integer width, Integer height);

	CDSBaseImpl autoResumeFit(String target, Integer width, Integer height);

	CDSBaseImpl autoFitEach(String target, Integer step, Integer max, Integer width, Integer height);

	CDSBaseImpl setSheetName(String objName);

	CDSBaseImpl setFileName(String objName);

	CDSBaseImpl copyRowsAndAddShiftLine(Integer startRow, Integer endRow, Integer tarStartRow);

	CDSBaseImpl copyARowWithStartTempRow(Integer startRow);

	CDSBaseImpl getCurDate();
	
	CDSBaseImpl getDateBeforeYear(Integer beforeYear);

	CDSBaseImpl count();

	CDSBaseImpl avgAge(String birthKey, String dividendKey);

	CDSBaseImpl buildInQuery(String paramName, List<String> inValues);

	CDSBaseImpl ageToBirthdate(Integer age);
	
	CDSBaseImpl setGlobalParam(String paramName, Object paramVal);
	
	CDSBaseImpl yesOrno(boolean ignoreEmpty);
	
	CDSBaseImpl yesOrno();
}
