package com.app.mvc.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

@Slf4j
public class TestExcel {

    public static void main(String args[]) throws Exception {
        readAndCopyExcel();
    }

    public static void readAndCopyExcel() throws Exception {
        /**
         * 如果要测试该case, 请先在项目首层(pom.xml相同路径)放置一个test.xlsx文件
         * 或者改这里的xlsx文件指定位置的文件
         */
        String newFile = "temp.xlsx";
        String originFile = "test.xlsx";

        Workbook writeWorkbook = new SXSSFWorkbook();
        Sheet writeSheet = writeWorkbook.createSheet();

        Workbook readWorkBook = new XSSFWorkbook(new FileInputStream(originFile)); // 将指定的excel读取到内存对象中
        Sheet readSheet = readWorkBook.getSheetAt(0); // 读取excel第一个sheet

        log.info("excel rows : {}", readSheet.getLastRowNum());
        for (int index = 0; index < readSheet.getLastRowNum(); index++) { // 遍历excel的每行
            try {
                Row readRow = readSheet.getRow(index); // 根据行号取出excel的每一行
                if (readRow == null) {
                    break;
                }
                Iterator<Cell> cellIterator = readRow.cellIterator();
                Row writeRow = writeSheet.createRow(index); // 在新的excel文件中添加一行
                int temp = 0;
                while (cellIterator.hasNext()) {      // 迭代遍历excel每行的每一列
                    Cell curCell = cellIterator.next(); //
                    if (temp == 0 && curCell == null) { // 处理可能存在的脏数据,这里假设第一列为行号
                        break;
                    }

                    // 在新excel的当前行中添加一个cell
                    Cell writeRowCell = writeRow.createCell(temp);
                    writeRowCell.setCellType(curCell.getCellType());
                    switch (curCell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        writeRowCell.setCellValue(curCell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        writeRowCell.setCellValue(curCell.getBooleanCellValue());
                        break;
                    default:
                        writeRowCell.setCellValue(curCell.getStringCellValue());
                    }
                    temp++;
                }
            } catch (Exception e) {
                log.error("parse excel exception, row: {}", index, e);
                throw e;
            }
        }

        log.info("generate new excel");
        FileOutputStream newExcel = new FileOutputStream(newFile);
        try {
            writeWorkbook.write(newExcel);
        } finally {
            newExcel.flush();
            newExcel.close();
        }
    }

}

