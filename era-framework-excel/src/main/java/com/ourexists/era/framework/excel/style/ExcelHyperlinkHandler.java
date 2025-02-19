package com.ourexists.era.framework.excel.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.List;

/**
 * 超链接样式处理器
 */
public class ExcelHyperlinkHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //获得超链接，当前单元格的内容就是一个超链接
        String stringCellValue = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : null;
        if (StringUtils.isNotBlank(stringCellValue) && stringCellValue.startsWith("http")) {
            CreationHelper creationHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
            Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(stringCellValue);
            cell.setCellValue("链接");
            cell.setHyperlink(hyperlink);//添加超链接
        }
    }
}
