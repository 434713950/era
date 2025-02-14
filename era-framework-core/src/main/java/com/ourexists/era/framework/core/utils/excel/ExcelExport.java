/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.framework.core.utils.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.ourexists.era.framework.core.exceptions.ImportExportException;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author pengcheng
 * @date 2021/6/30 14:44
 * @since 1.0.0
 */
public class ExcelExport {

    /**
     * 导出
     * @param list              导出的数据
     * @param title             导出的标题内容
     * @param sheetName         sheet名称
     * @param pojoClass         导出的类类型
     * @param isCreateHeader    是否创建行首
     * @param <T>   导出的类
     * @return  形成的workbook信息
     */
    public static <T> Workbook exportExcel(List<T> list, String title, String sheetName, Class<T> pojoClass, boolean isCreateHeader) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        return defaultExport(list, pojoClass, exportParams);
    }

    /**
     * 导出
     * @param list      导出的数据
     * @param title     导出的标题内容
     * @param sheetName sheet名称
     * @param pojoClass 导出的类类型
     * @param <T>   导出的类
     * @return  形成的workbook信息
     */
    public static <T> Workbook exportExcel(List<T> list, String title, String sheetName, Class<T> pojoClass) {
        return defaultExport(list, pojoClass, new ExportParams(title, sheetName));
    }

    /**
     * 导出
     * @param list  导出的数据
     * @return  形成的workbook信息
     */
    public static Workbook exportExcel(List<Map<String, Object>> list) {
        return ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
    }

    /**
     * 默认导出方案
     * @param list
     * @param pojoClass
     * @param exportParams
     * @param <T>
     * @return
     */
    private static <T> Workbook defaultExport(List<T> list, Class<T> pojoClass, ExportParams exportParams) {
        return ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
    }

//    public static String generateFile(String fileName, Workbook workbook) {
//        ByteArrayOutputStream fos = null;
//        ByteArrayInputStream fis = null;
//        try {
//            fos = new ByteArrayOutputStream();
//            workbook.write(fos);
//            fis = new ByteArrayInputStream(fos.toByteArray());
//            return ossUtils.upload(fis, fileName);
//        } catch (IOException e) {
//            throw new BusinessException("【excel工具】导出失败!");
//        } finally {
//            IOUtils.closeQuietly(fis);
//            IOUtils.closeQuietly(fos);
//            try {
//                workbook.close();
//            } catch (IOException e) {
//                //nothing
//            }
//        }
//    }

    /**
     * 下载excel文件
     * @param fileName  文件名
     * @param response  响应体
     * @param workbook  excel文件流
     */
    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws ImportExportException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new ImportExportException("【excel工具】导出失败!");
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                //nothing
            }
        }
    }
}
