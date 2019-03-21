package com.x.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelRead {

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd");

	private static final Logger logger = LoggerFactory.getLogger(ExcelRead.class);

	public int totalRows; // sheet中总行数
	public static int totalCells; // 每一行总单元格数

	/**
	 * read the Excel .xlsx,.xls
	 * 
	 * @param file
	 *            jsp中的上传文件
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readExcel(MultipartFile file) {
		if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
			return null;
		} else {
			String postfix = getPostfix(file.getOriginalFilename());
			if (!EMPTY.equals(postfix)) {
				if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file);
				} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 创建一个多余的用来返回读空单元格返回空的字符串
	 * 
	 * @param file
	 * @return
	 */
	public List<ArrayList<String>> readExcels(MultipartFile file) {
		if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
			return null;
		} else {
			String postfix = getPostfix(file.getOriginalFilename());
			if (!EMPTY.equals(postfix)) {
				if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXlsString(file);
				} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsxString(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 创建一个多余的用来返回读空单元格返回空的字符串
	 *
	 * @param file
	 * @return
	 */
	public List<ArrayList<String>> readExcelsByStartIndex(MultipartFile file, Integer index) {
		if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
			return null;
		} else {
			String postfix = getPostfix(file.getOriginalFilename());
			if (!EMPTY.equals(postfix)) {
				if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXlsStringByStartIndex(file, index);
				} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsxStringByStartIndex(file, index);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readXlsx(MultipartFile file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				totalRows = xssfSheet.getLastRowNum();
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						rowList = new ArrayList<String>();
						totalCells = xssfRow.getLastCellNum();
						// 读取列，从第一列开始
						for (int c = 0; c < totalCells; c++) {
							XSSFCell cell = xssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString()))
								// rowList.add(EMPTY);
								continue;
							else
								rowList.add(getXValue(cell).trim());
						}
						if (rowList.size() > 0)// 数组有值再添加
							list.add(rowList);
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 *            特地用来返回空字符串的
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readXlsxString(MultipartFile file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// 获取excel真实行数
				totalRows = getTrueRow(xssfSheet);
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows + 1; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						rowList = new ArrayList<String>();
						// 用标题的列数作为标板
						totalCells = xssfSheet.getRow(0).getLastCellNum();
						// 读取列，从第一列开始
						int Num = 0;
						for (int c = 0; c < totalCells; c++) {
							XSSFCell cell = xssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString())) {
								Num++;
								if (Num == totalCells - 1) {// 如果整行都为空则删除改集合
									rowList.clear();
									break;
								}
								rowList.add(EMPTY);
								continue;
							} else {
								rowList.add(getXValue(cell).trim());
							}
						}
						if (rowList.size() > 0)// 数组有值再添加
							list.add(rowList);
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	/**
	 * read the Excel 2010 .xlsx
	 *
	 * @param file leo
	 *            特地用来返回空字符串的,从指定行数开始读取excel
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readXlsxStringByStartIndex(MultipartFile file, Integer startIndex) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;

        int errorRowIndex = 1;
        int errorCellIndex = 0;
        if(startIndex != null && startIndex.intValue() == 0)
            errorRowIndex = startIndex;

        try {
			input = file.getInputStream();
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// 获取excel真实行数
				totalRows = getTrueRow(xssfSheet);
//				totalRows = xssfSheet.getLastRowNum() + 1;

                if(startIndex == null)
                    startIndex = 0;//默认从第一行开始读取
				for (int rowNum = startIndex; rowNum <= totalRows + 1; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						rowList = new ArrayList<String>();
                        int getCellFromRowIndex = (int)Math.floor(totalRows/2);
                        totalCells = xssfSheet.getRow(getCellFromRowIndex).getLastCellNum();
                        // 读取列，从第一列开始
						int Num = 0;
						for (int c = 0; c < totalCells; c++) {
							if(xssfRow.getCell(c)!=null)
								xssfRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
							XSSFCell cell = xssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString())) {
								Num++;
								if (Num == totalCells - 1) {// 如果整行都为空则删除改集合
									rowList.clear();
									break;
								}
								rowList.add(EMPTY);
								continue;
							} else {
								rowList.add(getXValue(cell).trim());
							}
							errorCellIndex++;
						}
						if (rowList.size() > 0)// 数组有值再添加
							list.add(rowList);
					}
					errorRowIndex++;
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
            logger.error(e.getMessage());
            logger.error("读取excel表格错误: [" + errorRowIndex + "," + errorCellIndex + "]");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public List<ArrayList<String>> readXls(MultipartFile file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				totalRows = hssfSheet.getLastRowNum();
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						rowList = new ArrayList<String>();
						totalCells = hssfRow.getLastCellNum();
						// 读取列，从第一列开始
						for (short c = 0; c < totalCells; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString()))
								// rowList.add(EMPTY);
								continue;
							rowList.add(getHValue(cell).trim());
						}
						list.add(rowList);
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 *            特地用来返回空字符串的
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public List<ArrayList<String>> readXlsString(MultipartFile file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取excel真实行数
				totalRows = getTrueRows(hssfSheet);
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows + 1; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						rowList = new ArrayList<String>();
						totalCells = hssfSheet.getRow(0).getLastCellNum();
						// 读取列，从第一列开始
						int Num = 0;
						for (short c = 0; c < totalCells; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString())) {
								Num++;
								if (Num == totalCells - 1) {// 如果整行都为空则删除改集合
									rowList.clear();
									break;
								}
								rowList.add(EMPTY);
								continue;
							} else{
								rowList.add(getHValue(cell).trim());
							}
						}
						if (rowList.size() > 0)// 数组有值再添加
							list.add(rowList);
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * read the Excel 2003-2007 .xls
	 *
	 * @param file
	 *            特地用来返回空字符串的
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readXlsStringByStartIndex(MultipartFile file, Integer startIndex) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		int errorRowIndex = 1;
		int errorCellIndex = 0;
		if(startIndex != null && startIndex.intValue() == 0)
			errorRowIndex = startIndex;

		try {
			input = file.getInputStream();
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取excel真实行数
				totalRows = getTrueRows(hssfSheet);
				// 读取Row,从第二行开始
                if(startIndex == null||startIndex.intValue() == 0)
                    startIndex = 1;
				for (int rowNum = startIndex; rowNum <= totalRows + 1; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						rowList = new ArrayList<String>();
                        int getCellFromRowIndex = (int)Math.floor(totalRows/2);
						totalCells = hssfSheet.getRow(getCellFromRowIndex).getLastCellNum();
						// 读取列，从第一列开始
						int Num = 0;
						for (short c = 0; c < totalCells; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							if (null == cell || "".equals(cell.toString())) {
								Num++;
								if (Num == totalCells - 1) {// 如果整行都为空则删除改集合
									rowList.clear();
									break;
								}
								rowList.add(EMPTY);
								continue;
							} else{
								rowList.add(getHValue(cell).trim());
							}
							errorCellIndex++;
						}
						if (rowList.size() > 0)// 数组有值再添加
							list.add(rowList);
					}
					errorRowIndex++;
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
            logger.error(e.getMessage());
            logger.error("读取excel表格错误: [" + errorRowIndex + "," + errorCellIndex + "]");
			System.err.println(e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 获取excel真实行数
	 * 
	 * @param
	 * @return
	 */
	public int getTrueRow(XSSFSheet sheet) {
		CellReference cellReference = new CellReference("A4");
		boolean flag = false;
		System.out.println("总行数：" + (sheet.getLastRowNum() + 1));
		for (int i = cellReference.getRow(); i <= sheet.getLastRowNum();) {
			Row r = sheet.getRow(i);
			if (r == null||"".equals(r)) {
				// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
				continue;
			}
			flag = false;
			for (Cell c : r) {
				if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
					flag = true;
					break;
				}
			}
			if (flag) {
				i++;
				continue;
			} else {// 如果是空白行（即可能没有数据，但是有一定格式）
				if (i == sheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
					sheet.removeRow(r);
				else// 如果还没到最后一行，则数据往上移一行
					sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}
		}
		System.out.println("真实总行数：" + (sheet.getLastRowNum() - 1));
		return sheet.getLastRowNum() - 1;
	}

	/**
	 * 获取excel真实行数
	 * 
	 * @param
	 * @return
	 */
	public int getTrueRows(HSSFSheet sheet) {
		CellReference cellReference = new CellReference("A4");
		boolean flag = false;
		System.out.println("虚假总行数：" + (sheet.getLastRowNum() + 1));
		for (int i = cellReference.getRow(); i <= sheet.getLastRowNum();) {
			Row r = sheet.getRow(i);
			if (r == null||"".equals(r)) {
				// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
				continue;
			}
			flag = false;
			for (Cell c : r) {
				if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
					flag = true;
					break;
				}
			}
			if (flag) {
				i++;
				continue;
			} else {// 如果是空白行（即可能没有数据，但是有一定格式）
				if (i == sheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
					sheet.removeRow(r);
				else// 如果还没到最后一行，则数据往上移一行
					sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}
		}
		System.out.println("真实总行数：" + (sheet.getLastRowNum() - 1));
		return sheet.getLastRowNum() - 1;
	}



	/**
	 * 解析Excel表
	 */
	public List<List<List<String>>> parseExcel(MultipartFile file) {
		if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
			return null;
		} else {
			try {
				Workbook workbook = initWorkBook(file);
				if (workbook != null) {

					List<List<List<String>>> results = new ArrayList<>();
					int numOfSheet = workbook.getNumberOfSheets();
					// 解析sheet
					for (int i = 0; i < numOfSheet; i++) {
						Sheet sheet = workbook.getSheetAt(i);
                        List<List<String>> parseSheet = parseExcelSheet(sheet);
						results.add(parseSheet);
					}
					return results;
				}else {
					return null;
				}
			}catch (IOException e) {
				return null;
			}
		}
	}

	Workbook initWorkBook(MultipartFile file) throws IOException {
		String postfix = getPostfix(file.getOriginalFilename());
		if (!EMPTY.equals(postfix)) {
			InputStream input = file.getInputStream();
			Workbook workbook = null;
			if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
				workbook = new HSSFWorkbook(input);
			} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
				workbook = new XSSFWorkbook(input);
			}
			return workbook;
		}else {
			return null;
		}
	}

	// 获取Excel表的真实行数
	int getExcelRealRow(Sheet sheet) {

		CellReference cellReference = new CellReference("A4");
		boolean flag = false;
		System.out.println("总行数："+(sheet.getLastRowNum()+1));
		for (int i = cellReference.getRow(); i <= sheet.getLastRowNum();) {
			Row r = sheet.getRow(i);
			if(r == null) {
				// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
				sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);
				continue;
			}
			flag = false;
			for(Cell c:r) {
				if(c.getCellType() != Cell.CELL_TYPE_BLANK){
					flag = true;
					break;
				}
			}
			if(flag) {
				i++;
				continue;
			}else {
			    // 如果是空白行（即可能没有数据，但是有一定格式）
				if(i == sheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
					sheet.removeRow(r);
				else//如果还没到最后一行，则数据往上移一行
					sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);
			}
		}
		System.out.println("总行数："+(sheet.getLastRowNum()+1));
		return sheet.getLastRowNum()+1;
	}



	/**
	 * 解析每一个sheet数据
	 * */
	private List<List<String>> parseExcelSheet(Sheet sheet) {
		Row row;
		int count = 0;

		List<List<String>> results = new ArrayList<>();

		int realRow = getExcelRealRow(sheet);
		short cellNum = 0;
		for (int i = 0; i < realRow; i++) {
			row = sheet.getRow(i);
			if (i == 0) {
				cellNum = row.getLastCellNum();
			}else {
				System.out.println("行数："+ cellNum);
				List<String> parseRow = parseExcelRow(row, cellNum);
				results.add(parseRow);
			}
		}

		return results;

	}

	/**
	 * 获得path的后缀名
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path){
		if(path==null || EMPTY.equals(path.trim())){
			return EMPTY;
		}
		if(path.contains(POINT)){
			return path.substring(path.lastIndexOf(POINT)+1,path.length());
		}
		return EMPTY;
	}
	/**
	 * 单元格格式
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getHValue(HSSFCell hssfCell){
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			String cellValue = "";
			if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
				Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
				cellValue = sdf.format(date);
			}else{
				DecimalFormat df = new DecimalFormat("#.##");
				cellValue = df.format(hssfCell.getNumericCellValue());
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());
				if(strArr.equals("00")){
					cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
				}
			}
			return cellValue;
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	/**
	 * 单元格格式
	 * @param xssfCell
	 * @return
	 */
	public static String getXValue(XSSFCell xssfCell){
		if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			String cellValue = "";
			if(DateUtil.isCellDateFormatted(xssfCell)){
				Date date = DateUtil.getJavaDate(xssfCell.getNumericCellValue());
				cellValue = sdf.format(date);
			}else{
				DecimalFormat df = new DecimalFormat("#.##");
				cellValue = df.format(xssfCell.getNumericCellValue());
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());
				if(strArr.equals("00")){
					cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
				}
			}
			return cellValue;
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}

    /**
     * 解析每一个row数据
     * */
    private List<String> parseExcelRow(Row row, int count) {

        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                results.add("");
            }else {
                // 定义每一个cell的数据类型
                cell.setCellType(Cell.CELL_TYPE_STRING);
                // 取出cell中的value
                results.add(cell.getStringCellValue());
            }
        }
        return results;
    }









}
