package com.ourexists.era.framework.excel.style;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.fastjson.JSON;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据合并样式处理器
 */
@Slf4j
public class ExcelFillCellMergeStrategy implements CellWriteHandler {

    /**
     * 合并的列
     */
    private List<Integer> mergeCol;

    /**
     * 求和合并的列
     */
    private List<Integer> sumCol;

    /**
     * 合并依据
     */
    private int deployCol;

    /**
     * 数据起始行
     */
    private int startRow = 0;

    public ExcelFillCellMergeStrategy() {
    }

    /**
     *
     * @param deployColIndex    作为合并基准的列数据
     * @param mergeColIndex     需要合并的列
     */
    public ExcelFillCellMergeStrategy(int deployColIndex, List<Integer> mergeColIndex) {
        this(deployColIndex, 0, mergeColIndex, null, null);
    }


    /**
     * 所有的列编号都同easy-excel注解@ExcelProperty中的index对应
     *
     * @param deployColIndex    作为合并基准的列数据
     * @param startRow  起始行,主要用来跳过标题
     * @param mergeColIndex 合并的列
     * @param sumColIndex   求和的列
     * @param includeColumnIndexes  控制实际导出的列,不控制导出列则置为null.
     *                              样式控件主要以excel中的数据进行处理。如何相关列不存在则不会进行处理
     */
    public ExcelFillCellMergeStrategy(int deployColIndex, int startRow, List<Integer> mergeColIndex, List<Integer> sumColIndex, List<Integer> includeColumnIndexes) {
        this.startRow = startRow;
        if (CollectionUtil.isBlank(includeColumnIndexes)) {
            this.mergeCol = mergeColIndex;
            this.sumCol = sumColIndex;
            this.deployCol = deployColIndex;
        } else {
            this.mergeCol = new ArrayList<>();
            this.sumCol = new ArrayList<>();
            for (int i = 0; i < includeColumnIndexes.size(); i++) {
                Integer index = includeColumnIndexes.get(i);
                if (index == deployColIndex) {
                    this.deployCol = i;
                }
                for (Integer colIndex : mergeColIndex) {
                    if (index.equals(colIndex)) {
                        this.mergeCol.add(i);
                        break;
                    }
                }
                for (Integer colIndex : sumColIndex) {
                    if (index.equals(colIndex)) {
                        this.sumCol.add(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        int curRowIndex = cell.getRowIndex();
        int curColIndex = cell.getColumnIndex();
        if (curRowIndex <= this.startRow) {
            return;
        }
        if (this.mergeCol != null) {
            for (int columnIndex : this.mergeCol) {
                if (curColIndex == columnIndex) {
                    mergeWithPrevRow(writeSheetHolder, cell, this.deployCol, false);
                    break;
                }
            }
        }
        if (this.sumCol != null) {
            for (int columnIndex : this.sumCol) {
                if (curColIndex == columnIndex) {
                    mergeWithPrevRow(writeSheetHolder, cell, this.deployCol, true);
                    break;
                }
            }
        }

    }

    /**
     * 当前单元格向上合并
     * .
     *
     * @param writeSheetHolder writeSheetHolder
     * @param cell             当前单元格
     */
    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int deployColIndex, boolean sum) {
        int curRowIndex = cell.getRowIndex();
        int curColIndex = cell.getColumnIndex();

        Row preRow = cell.getSheet().getRow(curRowIndex - 1);
        if (preRow == null) {
            // 当获取不到上一行数据时，使用缓存sheet中数据
            preRow = writeSheetHolder.getCachedSheet().getRow(curRowIndex - 1);
        }
        Cell preCell = preRow.getCell(curColIndex);

        //此处需要注意：所以获取每一行第一列数据和上一行第一列数据进行比较，如果相等合并
        String preDeployValue = preRow.getCell(deployColIndex).getStringCellValue();
        String curDeployValue = cell.getRow().getCell(deployColIndex).getStringCellValue();
        if (preDeployValue.equals(curDeployValue)) {
            double sumVal = 0;
            boolean isExte = false;
            if (CellType.NUMERIC.equals(cell.getCellType())) {
                double cur = cell.getNumericCellValue();
                double pre = preCell.getNumericCellValue();
                //求和的話向上+值
                if (sum) {
                    sumVal = cur + pre;
                    cell.setCellValue(sumVal);
                    isExte = true;
                } else {
                    if (cur == pre) {
                        isExte = true;
                    }
                }
            } else {
                //求和只针对数值类型
                sum = false;
            }
            if (CellType.STRING.equals(cell.getCellType())) {
                if (cell.getStringCellValue().equals(preCell.getStringCellValue())) {
                    isExte = true;
                }
            }
            if (isExte) {
                Sheet sheet = writeSheetHolder.getSheet();
                List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
                boolean isMerged = false;
                for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                    CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                    // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                    if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                        //重新设置合并中所有单元格的值用于合并后的展示
                        if (sum) {
                            for (int j = cellRangeAddr.getFirstRow(); j <= cellRangeAddr.getLastRow(); j++) {
                                cell.getSheet().getRow(j).getCell(curColIndex).setCellValue(sumVal);
                            }
                        }
                        sheet.removeMergedRegion(i);
                        cellRangeAddr.setLastRow(curRowIndex);
                        sheet.addMergedRegion(cellRangeAddr);
                        isMerged = true;
                    }
                }
                // 若上一个单元格未被合并，则新增合并单元
                if (!isMerged) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
                    sheet.addMergedRegion(cellRangeAddress);
                }
            }
        }
    }
}
