package com.x.utils.excel;

import com.x.utils.excel.annotation.ExcelField;
import com.x.utils.lang.ObjectUtils;
import com.x.utils.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 */
public class ExcelImport {

	private static Logger log = LoggerFactory.getLogger(ExcelImport.class);

	/**
	 * 工作薄对象
	 */
	private Workbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 标题行数
	 */
	private int headerNum;

	/**
	 * 构造函数
	 *
	 * @param file 导入文件对象，读取第一个工作表
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(File file) throws InvalidFormatException, IOException {
		this(file, 0, 0);
	}

	/**
	 * 构造函数
	 *
	 * @param file      导入文件对象，读取第一个工作表
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(File file, int headerNum)
			throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 *
	 * @param file             导入文件对象
	 * @param headerNum        标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称，从0开始
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(File file, int headerNum, Object sheetIndexOrName)
			throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndexOrName);
	}

	/**
	 * 构造函数
	 *
	 * @param multipartFile    导入文件对象
	 * @param headerNum        标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称，从0开始
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(MultipartFile multipartFile, int headerNum, Object sheetIndexOrName)
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndexOrName);
	}

	/**
	 * 构造函数
	 *
	 * @param headerNum        标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(String fileName, InputStream is, int headerNum, Object sheetIndexOrName)
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)) {
			throw new ExcelException("导入文档为空!");
		} else if (fileName.toLowerCase().endsWith("xls")) {
			this.wb = new HSSFWorkbook(is);
		} else if (fileName.toLowerCase().endsWith("xlsx")) {
			this.wb = new XSSFWorkbook(is);
		} else {
			throw new ExcelException("文档格式不正确!");
		}
		this.setSheet(sheetIndexOrName, headerNum);
		log.debug("Initialize success.");
	}

	/**
	 * 添加到 annotationList
	 */
	private void addAnnotation(List<Object[]> annotationList, ExcelField ef, Object fOrM, ExcelField.Type type, String... groups) {
//		if (ef != null && (ef.type()==0 || ef.type()==type)){
		if (ef != null && (ef.type() == ExcelField.Type.ALL || ef.type() == type)) {
			if (groups != null && groups.length > 0) {
				boolean inGroup = false;
				for (String g : groups) {
					if (inGroup) {
						break;
					}
					for (String efg : ef.groups()) {
						if (StringUtils.equals(g, efg)) {
							inGroup = true;
							annotationList.add(new Object[]{ef, fOrM});
							break;
						}
					}
				}
			} else {
				annotationList.add(new Object[]{ef, fOrM});
			}
		}
	}

	/**
	 * 获取当前工作薄
	 *
	 * @author ThinkGem
	 */
	public Workbook getWorkbook() {
		return wb;
	}

	/**
	 * 设置当前工作表和标题行数
	 *
	 * @author ThinkGem
	 */
	public void setSheet(Object sheetIndexOrName, int headerNum) {
		if (sheetIndexOrName instanceof Integer || sheetIndexOrName instanceof Long) {
			this.sheet = this.wb.getSheetAt(ObjectUtils.toInteger(sheetIndexOrName));
		} else {
			this.sheet = this.wb.getSheet(ObjectUtils.toString(sheetIndexOrName));
		}
		if (this.sheet == null) {
			throw new ExcelException("没有找到‘" + sheetIndexOrName + "’工作表!");
		}
		this.headerNum = headerNum;
	}

	/**
	 * 获取行对象
	 *
	 * @param rownum
	 * @return 返回Row对象，如果空行返回null
	 */
	public Row getRow(int rownum) {
		Row row = this.sheet.getRow(rownum);
		if (row == null) {
			return null;
		}
		// 验证是否是空行，如果空行返回null
		short cellNum = 0;
		short emptyNum = 0;
		Iterator<Cell> it = row.cellIterator();
		while (it.hasNext()) {
			cellNum++;
			Cell cell = it.next();
			if (StringUtils.isBlank(cell.toString())) {
				emptyNum++;
			}
		}
		if (cellNum == emptyNum) {
			return null;
		}
		return row;
	}

	/**
	 * 获取数据行号
	 *
	 * @return
	 */
	public int getDataRowNum() {
		return headerNum;
	}

	/**
	 * 获取最后一个数据行号
	 *
	 * @return
	 */
	public int getLastDataRowNum() {
		//return this.sheet.getLastRowNum() + headerNum;
		return this.sheet.getLastRowNum() + 1;
	}

	/**
	 * 获取最后一个列号
	 *
	 * @return
	 */
	public int getLastCellNum() {
		Row row = this.getRow(headerNum);
		return row == null ? 0 : row.getLastCellNum();
	}

	/**
	 * 获取单元格值
	 *
	 * @param row    获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column) {
		if (row == null) {
			return row;
		}
		Object val = "";
		try {
			Cell cell = row.getCell(column);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					val = cell.getNumericCellValue();
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
					} else {
						if ((Double) val % 1 > 0) {
							val = new DecimalFormat("0.00").format(val);
						} else {
							val = new DecimalFormat("0").format(val);
						}
					}
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					val = cell.getStringCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					try {
						val = cell.getStringCellValue();
					} catch (Exception e) {
						FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
								.getCreationHelper().createFormulaEvaluator();
						evaluator.evaluateFormulaCell(cell);
						CellValue cellValue = evaluator.evaluate(cell);
						switch (cellValue.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								val = cellValue.getNumberValue();
								break;
							case Cell.CELL_TYPE_STRING:
								val = cellValue.getStringValue();
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								val = cellValue.getBooleanValue();
								break;
							case Cell.CELL_TYPE_ERROR:
								val = ErrorEval.getText(cellValue.getErrorValue());
								break;
							default:
								val = cell.getCellFormula();
						}
					}
				} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
					val = cell.getErrorCellValue();
				}
			}
		} catch (Exception e) {
			return val;
		}
		return val;
	}

//	/**
//	 * 导入测试
//	 */
//	public static void main(String[] args) throws Throwable {
//
//		ExcelImport ei = new ExcelImport(new File("target/export.xlsx"), 1);
//
//		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
//			Row row = ei.getRow(i);
//			if (row == null){
//				continue;
//			}
//			for (int j = 0; j < ei.getLastCellNum(); j++) {
//				Object val = ei.getCellValue(row, j);
//				System.out.print(val+", ");
//			}
//			System.out.println();
//		}
//
//	}

}
