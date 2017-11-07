package rongji.report.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

public class WorkBookEngine {

	private HSSFWorkbook book = null;

	// 当前操作sheet
	private HSSFSheet curSheet = null;

	FileInputStream bookFis = null;

	private Integer curSheetIndex = null;

	private Integer copySheetIndex = null;

	/**
	 * 字体字号高宽信息
	 */
	private Map<Integer, Integer[]> fontInfoMap = new LinkedHashMap<Integer, Integer[]>();

	/**
	 * 存单元格需设置的字号
	 */
	private Map<String, Integer> cellFontMap = new HashMap<String, Integer>();

	/**
	 * 样式集 样式过多会报错，尽可能重复利用； key:单元格坐标 value-key:字号 value-value:样式
	 */
	Map<String, Map<Integer, HSSFCellStyle>> cellStyleMaps = new HashMap<String, Map<Integer, HSSFCellStyle>>();

	private Map<String, Integer> sheetNameMap = new HashMap<String, Integer>();

	private WorkBookEngine(String excelTmpPath) throws Exception {
		bookFis = new FileInputStream(excelTmpPath);
		book = new HSSFWorkbook(bookFis);
	}

	public static WorkBookEngine instance(String excelTmpPath) throws Exception {
		return new WorkBookEngine(excelTmpPath);
	}

	@SuppressWarnings("unchecked")
	public void createFontInfos(String path) throws Exception {
		File cmisXmlFile = new ClassPathResource(path).getFile();
		Document document = new SAXReader().read(cmisXmlFile);
		List<Element> fonts = document.getRootElement().element("FONTS").elements();
		for (Element font : fonts) {
			String name = font.attribute("name").getValue();
			if (!"宋体".equals(name)) {
				continue;
			}
			Integer marginTop = Integer.parseInt(font.attribute("marginTop").getValue());
			Integer marginLeft = Integer.parseInt(font.attribute("marginLeft").getValue());
			List<Element> wordSizes = font.elements();
			for (Element wordSize : wordSizes) {
				Integer size = Integer.parseInt(wordSize.attribute("size").getValue());
				Integer width = Integer.parseInt(wordSize.attribute("width").getValue());
				Integer height = Integer.parseInt(wordSize.attribute("height").getValue());
				fontInfoMap.put(size, new Integer[] { width, marginLeft, height, marginTop });
			}
		}
	}

	public void setNextCurSheet(Integer index) {
		cellFontMap = new HashMap<String, Integer>();
		if (index == null) {
			if (copySheetIndex == null) {
				copySheetIndex = 0;
			}
			this.book.cloneSheet(copySheetIndex);
			curSheetIndex = copySheetIndex;
			curSheet = this.book.getSheetAt(curSheetIndex);
			copySheetIndex = this.book.getNumberOfSheets() - 1;
		} else {
			if (copySheetIndex != null) {
				this.book.removeSheetAt(copySheetIndex);
			}
			this.book.cloneSheet(index);
			curSheetIndex = index;
			curSheet = this.book.getSheetAt(curSheetIndex);
			copySheetIndex = this.book.getNumberOfSheets() - 1;
		}
	}

	public void setCurSheet(Integer index) {
		if (index != null) {
			removeCopySheet();
			curSheet = this.book.getSheetAt(index);
			this.curSheetIndex = index;
		}

	}

	public void clear() {
		if (book != null && copySheetIndex != null) {
			book.removeSheetAt(copySheetIndex);
		}
		book = null;
		copySheetIndex = null;
		curSheetIndex = null;
		curSheet = null;
		fontInfoMap = null;
		cellFontMap = null;
		cellStyleMaps = null;
		sheetNameMap = null;
		if (bookFis != null) {
			IOUtils.closeQuietly(bookFis);
		}
	}

	public HSSFWorkbook getBook() {
		return book;
	}

	public HSSFSheet getCurSheet() {
		return curSheet;
	}

	public Map<Integer, Integer[]> getFontInfoMap() {
		return fontInfoMap;
	}

	public Map<String, Integer> getCellFontMap() {
		return cellFontMap;
	}

	public void setFontInfoMap(Map<Integer, Integer[]> fontInfoMap) {
		this.fontInfoMap = fontInfoMap;
	}

	public Map<String, Map<Integer, HSSFCellStyle>> getCellStyleMaps() {
		return cellStyleMaps;
	}

	public Integer getCurSheetIndex() {
		return curSheetIndex;
	}

	public void setCurSheetName(String sheetName) {
		if (StringUtils.isNotEmpty(sheetName) && curSheetIndex != null && curSheetIndex >= 0) {
			if (sheetNameMap.containsKey(sheetName)) {
				Integer index = sheetNameMap.get(sheetName) + 1;
				sheetNameMap.put(sheetName, index);
				sheetName += "(" + index + ")";
			} else {
				sheetNameMap.put(sheetName, 0);
			}
			this.book.setSheetName(curSheetIndex, sheetName);
		}
	}

	public HSSFSheet getCopySheet() {
		if (copySheetIndex == null) {
			return null;
		}
		return book.getSheetAt(copySheetIndex);
	}

	public void removeCopySheet() {
		if (book != null && copySheetIndex != null) {
			book.removeSheetAt(copySheetIndex);
		}
		copySheetIndex = null;
	}

	public Boolean hasCopySheet() {
		if (book != null && copySheetIndex != null) {
			return true;
		}
		return false;
	}

}
