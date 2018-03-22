package com.avaldes.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelOutputService {
	private static final Logger LOGGER = Logger.getLogger(ExcelOutputService.class);
	
	public ExcelOutputService(){
		
	}
	public XSSFWorkbook createExcelOutputFile(){
		XSSFWorkbook workbook = null;
		    try {          
		           
		        ClassLoader loader = getClass().getClassLoader();
		        File file = new File("F:\\Freelancing\\jars\\Excel_Output.xlsx");  //file should be at classpath
		        FileInputStream is = new FileInputStream(file);
		           
		        // Get the workbook instance for XLSX file
		        workbook = new XSSFWorkbook();
		        XSSFSheet rankerSheet1 = workbook.createSheet("Anish");

		        writeExcelOutputData(rankerSheet1, workbook);         
		        
		        is.close();
		           
		    } catch (FileNotFoundException e) {
		            LOGGER.error(e);
		   } catch (IOException e) {
		            LOGGER.error(e);
		   }
		        return workbook;
		}

		private void writeExcelOutputData(XSSFSheet rankerSheet, XSSFWorkbook worksheet){
		           
		            XSSFRow row1 = rankerSheet.createRow(1);
		            row1.createCell(0).setCellValue(1);
		            row1.createCell(1).setCellValue(1234);
		            row1.createCell(2).setCellValue("Test Excel");
		            row1.createCell(3).setCellValue("Address");
		           
		            XSSFCell cell4 = row1.createCell(4);
		               cell4.setCellValue(10.00);
		            XSSFCellStyle style = worksheet.createCellStyle();
		            style.setDataFormat((short)8);      // this will format cell with $ sign Ex: $10.00
		            cell4.setCellStyle(style);  
		           
		           
		          //Creating Data format %
		            XSSFCellStyle percentStyle = worksheet.createCellStyle();
		            percentStyle.setDataFormat(worksheet.createDataFormat().getFormat("0.0%"));
		           
		            XSSFCell cell5 = row1.createCell(5);
		            cell5.setCellStyle(percentStyle);      
		            cell5.setCellValue(20.00);   

		    }
		
		
//		public static void main(String[] args){
//			ExcelOutputService e = new ExcelOutputService();
//			XSSFWorkbook workbook = e.createExcelOutputFile();
//			String path = "F:\\Freelancing\\jars\\Excel_Output1.xlsx";
//			FileOutputStream fop;
//			try {
//				fop = new FileOutputStream(path);
//				workbook.write(fop);
//				fop.close();
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			
//		}
		
}
