package com.ourexists.era.framework.excel;

import com.alibaba.excel.support.ExcelTypeEnum;

public interface ExportExecutor<T> {
    /**
     * 导出
     *
     * @param exportId  导出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     */
    void export(String exportId, T condition, String fileName, ExcelTypeEnum excelType);
}
