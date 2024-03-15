package org.xavier.star.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtils {

    //判断文件格式
    private static Workbook judgeExcel(String filePath){
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readExcel(String filePath, int type){
        Workbook wb;
        Sheet sheet;
        Row row;
        List<String> list = new ArrayList<>();

        wb = judgeExcel(filePath);
        if(wb != null){
            try {
                List<List<Object>> sheetList = new ArrayList<>();
                sheet = wb.getSheetAt(0);

                //循环行
                for(int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++){
                    //指定行的值
                    row = sheet.getRow(rowNum);
                    if(row==null) continue;
                    //定义存放一行的List
                    List<Object> rowList = new ArrayList<>();
                    //循环列
                    for(int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++){
                        Cell cell = sheet.getRow(rowNum).getCell(cellNum);
//                      rowList.add(cell.getStringCellValue());
                        String str = new String();
                        if (type == 0){

                            if ((rowNum == 6 && cellNum == 6) || (rowNum == 7 && cellNum == 6) || (rowNum == 8 && cellNum == 6) || (rowNum == 9 && cellNum == 6)
                            || (rowNum == 10 && cellNum == 6) || (rowNum >= 12 && cellNum == 4)){

                                if(rowNum < 12) {
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }
                                else if(cell == null) break;

                                else str += cell.getStringCellValue();
                            }
                            str = str.trim();
                            if(!str.equals("")) list.add(str);
                        }
                        else if(type == 1){
                            if ((rowNum == 0 && cellNum == 1) || (rowNum == 1 && cellNum == 1) || (rowNum == 2 && cellNum == 1)){
                                list.add(cell.getStringCellValue());
                            }
                        }
                        else if (type == 2){
                            if(rowNum == 0 && cellNum == 0) {
                                list.add(cell.getStringCellValue());
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String filePath = "D:/FILE/UNZIPPED/1/广州科盟KM-1030C超声波清洗机/2-产品编码/产品编码.xlsx";
        List<String> list = readExcel(filePath,1);
        System.out.println(list);
    }
}