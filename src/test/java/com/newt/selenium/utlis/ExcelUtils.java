package com.newt.selenium.utlis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.newt.selenium.constants.ApplicationConstants;

public class ExcelUtils implements ApplicationConstants {

	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static XSSFRow Row;
	static int reqcellrowno;
	static int reqcellcolno;
	
	PropertyUtility putility = new PropertyUtility(OBJECT_REPO_FILEPATH);
	
	public static void setExcelFile(String Path, String SheetName) throws Exception {
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(Path);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			System.out.println("Error while intializing the excel file " + e);
		}
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	public void setCellData(String sheetName, String cellValue, int RowNum, int ColNum) throws Exception {
		try {
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet(sheetName);

			Row row = sheet.getRow(RowNum);
			Cell cellWrite = row.getCell(ColNum);
			if (cellWrite == null) {
				cellWrite = row.createCell(ColNum);
				cellWrite.setCellValue(cellValue);
			} else {
				cellWrite.setCellValue(cellValue);
			}
			FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Error while setting the cell data by setCellData(Str1, Str2, int1, int2) for column : "+ColNum+" and row : "+RowNum+" Exception : " + e.getMessage());
		}
	}

	public void setCellData(Sheet mySheet, String cellValue, int RowNum, int ColNum) throws Exception {
		try {
			Row row = mySheet.getRow(RowNum);
			Cell cellWrite = row.getCell(ColNum);
			if (cellWrite == null) {
				cellWrite = row.createCell(ColNum);
				cellWrite.setCellValue(cellValue);
			} else {
				cellWrite.setCellValue(cellValue);
			}
		} catch (Exception e) {
			System.out.println("Error while setting the cell data by setCellData(Sheet, Str1, int1, int2) for column : "+ColNum+" and row : "+RowNum+" Exception : " + e.getMessage());
		}
	}

	public void setCellData(Sheet sheet, String cellValue, String rowName, String colName) throws Exception {
		try {
			int rowNum = 0;
			int colNum = 0;
			Row row = sheet.getRow(0);

			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				Cell c = rowLoop.getCell(0);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcaserowname = c.getStringCellValue();
				if (rowName.equalsIgnoreCase(testcaserowname)) {
					rowNum = i;
					break;
				}
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell c = row.getCell(j);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcasecellname = c.getStringCellValue();
				if (colName.equalsIgnoreCase(testcasecellname)) {
					colNum = j;
					break;
				}
			}
			Cell cellWrite = sheet.getRow(rowNum).getCell(colNum);
			if (cellWrite == null) {
				cellWrite = sheet.getRow(rowNum).createCell(colNum);
				cellWrite.setCellValue(cellValue);
			} else {
				cellWrite.setCellValue(cellValue);
			}
		} catch (Exception e) {
			System.out.println("Error while setting the cell data by setCellData(Sheet, Str1, Str2, Str3) for column : "+colName+" and row : "+rowName+" Exception : " + e.getMessage());
		}
	}

	public void setCellData(String sheetName, String cellValue, String rowName, String colName) throws Exception {
		try {
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet(sheetName);
			int rowNum = 0;
			int colNum = 0;
			Row row = sheet.getRow(0);

			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				Cell c = rowLoop.getCell(0);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcaserowname = c.getStringCellValue();
				if (rowName.equalsIgnoreCase(testcaserowname)) {
					rowNum = i;
					break;
				}
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell c = row.getCell(j);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcasecellname = c.getStringCellValue();
				if (colName.equalsIgnoreCase(testcasecellname)) {
					colNum = j;
					break;
				}
			}
			Cell cellWrite = sheet.getRow(rowNum).getCell(colNum);
			if (cellWrite == null) {
				cellWrite = sheet.getRow(rowNum).createCell(colNum);
				cellWrite.setCellValue(cellValue);
			} else {
				cellWrite.setCellValue(cellValue);
			}
			FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Error while setting the cell data by setCellData(Str1, Str2, Str3, Str4) for column : "+colName+" and row : "+rowName+" Exception : " + e.getMessage());
		}
	}

	public static String getExcelData(String columnname, String rowname) throws Exception {
		ArrayList<String> testcasenamedata = new ArrayList<String>();
		ArrayList<String> colnamedata = new ArrayList<String>();
		int writtenrowno = ExcelWSheet.getPhysicalNumberOfRows();
		boolean colFound = false;
		boolean rowFound = false;	
		String CellData = "";
		try{
			Row = ExcelWSheet.getRow(0);
			int writtencolno = Row.getLastCellNum();
			for (int i = 0; i < writtenrowno; i++) {
				Row = ExcelWSheet.getRow(i);
				Cell = Row.getCell(0);
				if (Cell != null) {
					testcasenamedata.add(Cell.getStringCellValue());
					if (rowname.equalsIgnoreCase(testcasenamedata.get(i))) {
						reqcellrowno = i;
						rowFound = true;
						break;
					}
				}
			}
			if (rowFound) {
				for (int j = 0; j < writtencolno; j++) {
					Row = ExcelWSheet.getRow(0);
					Cell = Row.getCell(j);
					if (Cell != null) {
						colnamedata.add(Cell.getStringCellValue());
						if (columnname.equalsIgnoreCase(colnamedata.get(j))) {
							reqcellcolno = j;
							colFound = true;
							break;
						}
					}
				}
			}
			if (colFound) {
				Cell = ExcelWSheet.getRow(reqcellrowno).getCell(reqcellcolno);
				Cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				CellData = Cell.getStringCellValue();
			}
			return CellData;
		}
		catch (Exception e) {
			System.out.println("Error while getting the cell data by getExcelData(Str1,Str2) for column : "+columnname+" and row : "+rowname+" Exception : " + e.getMessage());
			return CellData;
		}		
	}

	public String getExcelData(String sheetName, String columnname, String rowname) throws Exception {
		InputStream inp = new FileInputStream(DATA_FILEPATH);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheet(sheetName);
		String CellData = "";
		int rowNum = 0;
		int colNum = 0;
		boolean colFound = false;
		boolean rowFound = false;
		try{
			Row row = sheet.getRow(0);
	
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				if (rowLoop != null){
					Cell c = rowLoop.getCell(0);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcaserowname = c.getStringCellValue();
						if (rowname.equalsIgnoreCase(testcaserowname)) {
							rowNum = i;
							rowFound = true;
							break;
						}
					}
				}
			}
			if (rowFound) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell c = row.getCell(j);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcasecellname = c.getStringCellValue();
						if (columnname.equalsIgnoreCase(testcasecellname)) {
							colNum = j;
							colFound = true;
							break;
						}
					}
				}
			}
			if (colFound) {
				Cell cell = sheet.getRow(rowNum).getCell(colNum);
				cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				CellData = cell.getStringCellValue();
			}
			return CellData;
		}
		catch (Exception e) {
			System.out.println("Error while getting the cell data by getExcelData(Str1,Str2,Str3) for column : "+columnname+" and row : "+rowname+" Exception : " + e.getMessage());
			return CellData;
		}		
	}

	public String getExcelData(Sheet sheet, String rowName, String colName) throws Exception {
		String CellData = "";
		try {
			int rowNum = 0;
			int colNum = 0;
			boolean colFound = false;
			boolean rowFound = false;
			Row row = sheet.getRow(0);
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				if (rowLoop != null){
					Cell c = rowLoop.getCell(0);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcaserowname = c.getStringCellValue();
						if (rowName.equalsIgnoreCase(testcaserowname)) {
							rowNum = i;
							rowFound = true;
							break;
						}
					}
				}
			}
			if (rowFound) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell c = row.getCell(j);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcasecellname = c.getStringCellValue();
						if (colName.equalsIgnoreCase(testcasecellname)) {
							colNum = j;
							colFound = true;
							break;
						}
					}
				}
			}
			if (colFound) {
				Cell cell = sheet.getRow(rowNum).getCell(colNum);
				cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				CellData = cell.getStringCellValue();
			}
			return CellData;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while getting the cell data by getExcelData(Sheet,Str1,Str2) for sheet : "+sheet.getSheetName() +" for column : "+colName+" and row : "+rowName+" Exception : " + e.getMessage());
			return CellData;
		}
	}

	public ArrayList<String> getCellCount(Sheet mySheet) throws Exception {
		ArrayList<String> testCases = new ArrayList<String>();
		try {
			int i = 1;
			boolean loop = true;
			while (loop) {
				Row row = mySheet.getRow(i);
				if (row == null)
					loop = false;
				else {
					Cell cellWrite = row.getCell(0);
					if (cellWrite == null)
						loop = false;
					else {
						cellWrite.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						testCases.add(cellWrite.getStringCellValue());
					}
				}
				i++;
			}
			return testCases;
		} catch (Exception e) {
			System.out.println("Error while getting the cell data by getCellCount(Sheet) Exception : " + e.getMessage());
			return testCases;
		}
	}
	
	public void clearResultData(String sheetName, String resultStatus, String resultError, String resultJO) throws Exception {
		InputStream inp = new FileInputStream(DATA_FILEPATH);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheet(sheetName);
		ArrayList<String> testCases = getCellCount(sheet);
		int colResultStatus = 0 ;
		int colResultError = 0;
		int colResultJO = 0;
		Row row = sheet.getRow(0);
		
		if(resultJO !=null && resultJO.trim().length() > 0){
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell c = row.getCell(j);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcasecellname = c.getStringCellValue();
				if (resultJO.equalsIgnoreCase(testcasecellname)) {
					colResultJO = j;
					break;
				}
			}
		}
		
		if(resultStatus !=null && resultStatus.trim().length() > 0){
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell c = row.getCell(i);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcasecellname = c.getStringCellValue();
				if (resultStatus.equalsIgnoreCase(testcasecellname)) {
					colResultStatus = i;
					break;
				}
			}
		}
		
		if(resultError !=null && resultError.trim().length() > 0){
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell c = row.getCell(j);
				c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				String testcasecellname = c.getStringCellValue();
				if (resultError.equalsIgnoreCase(testcasecellname)) {
					colResultError = j;
					break;
				}
			}
		}
		if (colResultStatus > 0){
			for (int k = 1; k <= testCases.size(); k++) {
				Cell cellResultStatus = sheet.getRow(k).getCell(colResultStatus); 
				if (cellResultStatus != null)
					cellResultStatus.setCellValue("");				
			}				
		}			
		if (colResultError > 0){
			for (int k = 1; k <= testCases.size(); k++) {
				Cell cellResultError = sheet.getRow(k).getCell(colResultError);
				if (cellResultError != null)
					cellResultError.setCellValue("");					
			}
			
		}
		if (colResultJO > 0){
			for (int k = 1; k <= testCases.size(); k++) {
				Cell cellResultJO = sheet.getRow(k).getCell(colResultJO); 
				if (cellResultJO != null)
					cellResultJO.setCellValue("");				
			}				
		}
		
		FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
		wb.write(fileOut);
		fileOut.close();
	}
	
	
	public static String getExcelDate(String columnname,String rowname) throws Exception
    {
    	ArrayList<String> testcasenamedata = new ArrayList<String>();
    	ArrayList<String> colnamedata = new ArrayList<String>();
    	int writtenrowno= ExcelWSheet.getPhysicalNumberOfRows();
    	Row =  ExcelWSheet.getRow(0);
    	int writtencolno= Row.getLastCellNum();
    	Date  CellData = null;
    	String reportDate;
    	for(int i=0;i<writtenrowno;i++)
     	{
    		Row =  ExcelWSheet.getRow(i);
     		Cell = Row.getCell(0);
     		testcasenamedata.add(Cell.getStringCellValue());
     		if(rowname.equalsIgnoreCase(testcasenamedata.get(i)))
     		{
     			reqcellrowno=i;
     			break;
     		}
     	}
     	for(int j=0;j<writtencolno;j++)
     	{
     		Row =  ExcelWSheet.getRow(0);
     		Cell= Row.getCell(j);
     		colnamedata.add(Cell.getStringCellValue());
     		if(columnname.equalsIgnoreCase(colnamedata.get(j)))
     		{
     			reqcellcolno=j;
     			break;
     		}
     	}

     	Cell = ExcelWSheet.getRow(reqcellrowno).getCell(reqcellcolno);
     	if (DateUtil.isCellDateFormatted(Cell)) {
           CellData = Cell.getDateCellValue();             
           } 

     	else 
     	{
                //System.out.println(Cell.getNumericCellValue());
        }


     	if(CellData==null)
     	{
     		reportDate = null;	
     	}
     	else
     	{
     		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		reportDate = df.format(CellData);
     	}	
     	return reportDate;
    }
	
	public String getQuestionAnswer(String question) throws Exception {
		InputStream inp = new FileInputStream(DATA_FILEPATH);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheet(putility.getProperty("QUESTION_ANSWER_SHEET").trim());
		String answer = "NULL";
		int rowNum = 0;
		boolean rowFound = false;
		try{
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				if (rowLoop != null){
					Cell c = rowLoop.getCell(new Integer(putility.getProperty("COLUMN_NUMBER_QUESTION").trim()).intValue());
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String rowQuestion = c.getStringCellValue().trim();
						if (question.trim().equalsIgnoreCase(rowQuestion)) {
							rowNum = i;
							rowFound = true;
							break;
						}
					}
				}
			}
			if (rowFound) {
				Cell cell = sheet.getRow(rowNum).getCell(new Integer(putility.getProperty("COLUMN_NUMBER_ANSWER").trim()).intValue());
				cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				answer = cell.getStringCellValue();
			}
			return answer;
		}
		catch (Exception e) {
			System.out.println("Error while getting the answer by getQuestionAnswer(Str1) for question : "+question+" Exception : " + e.getMessage());
			return answer;
		}		
	} 
	
	
	
	public ArrayList<String> getAllAnswer(String question) throws Exception {
		ArrayList <String> answers = new ArrayList<String>() ;
		InputStream inp = new FileInputStream(DATA_FILEPATH);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheet(putility.getProperty("QUESTION_ANSWER_SHEET"));
		try{
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				if (rowLoop != null){
					Cell c = rowLoop.getCell(new Integer(putility.getProperty("COLUMN_NUMBER_QUESTION")).intValue());
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String rowQuestion = c.getStringCellValue().trim();
						if (question.trim().equalsIgnoreCase(rowQuestion)) {
							Cell cell = sheet.getRow(i).getCell(new Integer(putility.getProperty("COLUMN_NUMBER_ANSWER")).intValue());
							cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
							String answer = cell.getStringCellValue();
							answers.add(answer);
						}
					}
				}
			}
			return answers;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while getting the answer by getAllAnswer(Str1) for question : "+question+" Exception : " + e.getMessage());
			return answers;
		}		
	}	

	public boolean isAnswerExists(String answerToCompare, ArrayList<String> allAnswers) throws Exception {
		boolean result = false;
		try{
			if (allAnswers != null && allAnswers.size() > 0 && answerToCompare != null && answerToCompare.length() > 0){
				for (int i = 0; i < allAnswers.size(); i++) {
					if(allAnswers.get(i).equalsIgnoreCase(answerToCompare)){
						result = true;
						break;
					}
				}
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while checking the answer by isAnswerExists(Str1, ArrayList<Str>) for answer : "+answerToCompare+" Exception : " + e.getMessage());
			return result;
		}		
	}	 
	
} 
