package com.ourexists.era.framework.excel;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.ourexists.era.framework.excel.export.ExportPartDataResult;

import java.util.Collection;
import java.util.List;

public interface ExportDataHandler<T, R> {

    /**
     * 每次的导出数据
     * @param condition
     * @param exportPartDataResult
     * @return
     */
    List<R> exportData(T condition, ExportPartDataResult exportPartDataResult);

    /**
     * 导出的数据类型
     * @return
     */
    Class<R> exportDataClass();

    /**
     * 导出的列
     * @param condition
     * @return
     */
    default Collection<Integer> includeColumnIndexes(T condition) {
        return null;
    }

    /**
     * 导出样式
     * @param condition
     * @return
     */
    default List<WriteHandler> loadWriteHandlers(T condition) {
        return null;
    }

    /**
     * 完成后处理
     *
     * @param exportId  导出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     */
    default void completeHandle(String exportId, T condition, String fileName, ExcelTypeEnum excelType) {
    }

    /**
     * 失败处理
     *
     * @param exportId  出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     * @param e         失败异常
     */
    default void failedHandle(String exportId, T condition, String fileName, ExcelTypeEnum excelType, Throwable e) {
    }

    /**
     * 导出前处理
     */
    default void beforeExport(String exportId, T condition, String fileName, ExcelTypeEnum excelType) {
    }

    /**
     * 单次分片前处理
     *
     * @param condition            导出条件
     * @param dataClass            数据类
     * @param excelType            导出的文件类型
     * @param exportPartDataResult 分片数据处理集
     * @param writeHandlers        样式处理器
     */
    default void beforePartHandle(T condition, Class<R> dataClass, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) {
    }

    /**
     * 单次分片后处理
     *
     * @param condition            导出条件
     * @param dataClass            数据类
     * @param excelType            导出的文件类型
     * @param exportPartDataResult 分片数据处理集
     * @param writeHandlers        样式处理器
     */
    default void afterPartHandle(T condition, Class<R> dataClass, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) {
    }
}
