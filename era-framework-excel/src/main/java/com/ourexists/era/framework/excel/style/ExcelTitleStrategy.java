package com.ourexists.era.framework.excel.style;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

/**
 * 标题样式处理器
 */
public class ExcelTitleStrategy implements SheetWriteHandler {

    private final String title;

    private int rowNum = 0;

    private int cellNum = 0;

    private int sheetNum = 0;


    public ExcelTitleStrategy(String title) {
        this.title = title;
    }

    public ExcelTitleStrategy(String title, int sheetNum, int rowNum, int cellNum) {
        this.title = title;
        this.sheetNum = sheetNum;
        this.rowNum = rowNum;
        this.cellNum = cellNum;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (StringUtils.isBlank(title)) {
            return;
        }
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(sheetNum);
        Row row1 = sheet.createRow(rowNum);
        Cell cell = row1.createCell(cellNum);
        cell.setCellValue(title);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
}
