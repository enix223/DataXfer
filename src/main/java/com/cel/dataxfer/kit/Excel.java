package com.cel.dataxfer.kit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel {

	private File excel;
	private HSSFWorkbook workbook;
	
	public Excel(String filePath){
		try{
			excel = new File(filePath);
			initWorkBook();
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Excel(File excel){
		this.excel = excel;
		initWorkBook();
	}
	
	private void initWorkBook(){
		try {
			InputStream is = new FileInputStream(this.excel);
			this.workbook = new HSSFWorkbook(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * Get the cell of the giving position
	 * @param sht
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public HSSFCell getCell(HSSFSheet sht, int rowIndex, int columnIndex){
		if(sht == null){
			return null;
		}
		
		HSSFRow row = sht.getRow(rowIndex);
		if(row == null){
			return null;
		}
		
		HSSFCell cell = row.getCell(columnIndex);
		return cell;
	}
	
	public HSSFCell getCell(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		return getCell(sht, rowIndex, columnIndex);
	}
	
	public HSSFCell getCell(int sheetIndex, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheetIndex);	
		return getCell(sht, rowIndex, columnIndex);
	}
	
	/**
	 * Get cell value to string
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public String getValueToStr(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToStr(cell);
	}
	
	/**
	 * Get cell value to double
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public double getValueToNumeric(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToNumeric(cell);
	}
	
	/**
	 * Get cell value to date
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public Date getValueToDate(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToDate(cell);
	}
	
	/**
	 * Get cell value to boolean
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public boolean getValueToBoolean(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToBoolean(cell);
	}
	
	/**
	 * Get cell value to int type.
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public int getValueToInt(String sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheet(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToInt(cell);
	}
	
	/* ---------------------------------- */
	/**
	 * Get cell value to string
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public String getValueToStr(int sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToStr(cell);
	}
	
	/**
	 * Get cell value to double
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public double getValueToNumeric(int sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToNumeric(cell);
	}
	
	/**
	 * Get cell value to int
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public int getValueToInt(int sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToInt(cell);
	}
	
	/**
	 * Get cell value to date
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public Date getValueToDate(int sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToDate(cell);
	}
	
	/**
	 * Get cell value to boolean
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public boolean getValueToBoolean(int sheet, int rowIndex, int columnIndex){
		HSSFSheet sht = workbook.getSheetAt(sheet);
		HSSFCell cell = this.getCell(sht, rowIndex, columnIndex);
		return getValueToBoolean(cell);
	}
	
	/* ---------------------------------- */
	/**
	 * Get the cell values to string
	 * @param cell
	 * @return
	 */
	public String getValueToStr(HSSFCell cell){
		return cell.getStringCellValue();
	}
	
	/**
	 * Get the cell value to int
	 * @param cell
	 * @return
	 */
	public double getValueToNumeric(HSSFCell cell){
		return cell.getNumericCellValue();
	}
	
	/**
	 * Get the cell value to date
	 * @param cell
	 * @return
	 */
	public Date getValueToDate(HSSFCell cell){
		return cell.getDateCellValue();
	}
	
	/**
	 * Get the cell value to boolean type
	 * @param cell
	 * @return
	 */
	public boolean getValueToBoolean(HSSFCell cell){
		return cell.getBooleanCellValue();
	}
	
	/**
	 * Get the cell value to int
	 * @param cell
	 * @return
	 */
	public int getValueToInt(HSSFCell cell){
		return (int)cell.getNumericCellValue();		
	}
	
	/* ---------------------------------- */	
	/**
	 * Get the max row num in the specified sheet
	 * @param sheetName
	 * @return
	 */
	public int getMaxRows(String sheetName){
		HSSFSheet sheet = workbook.getSheet(sheetName);
		return getMaxRows(sheet);
	}
	
	public int getMaxRows(int sheetIdx){
		HSSFSheet sheet = workbook.getSheetAt(sheetIdx);
		return getMaxRows(sheet);
	}
	
	public int getMaxRows(HSSFSheet sheet){
		return sheet.getLastRowNum();
	}
	
	/**
	 * Validate the excel template correct or not.
	 * Please make sure the template should got header line.
	 * @param sheet
	 * @param columns
	 * @return
	 */
	public boolean validateTemplate(String sheet, ArrayList<String> columns){
		for(int i = 0; i < columns.size(); i ++){
			try {
				String value = this.getValueToStr(sheet, 0, i);
				if(!columns.get(i).equals(value)){
					return false;
				}
			} catch (Exception e) {
				return false;
			}			
		}
		return true;
	}
	
	/**
	 * Validate the excel template correct or not.
	 * Please make sure the template should got header line.
	 * @param sheet
	 * @param columns
	 * @return
	 */
	public boolean validateTemplate(HSSFSheet sheet, ArrayList<String> columns){
		return validateTemplate(sheet.getSheetName(), columns);
	}
	
	/**
	 * Validate the excel template correct or not.
	 * Please make sure the template should got header line.
	 * @param index
	 * @param columns
	 * @return
	 */
	public boolean validateTemplate(int index, ArrayList<String> columns){
		HSSFSheet sheet = this.workbook.getSheetAt(index);
		return validateTemplate(sheet.getSheetName(), columns);
	}

	/* ---------------------------------- */
	/*
	 * Property getter & setter
	 */
	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}
}
