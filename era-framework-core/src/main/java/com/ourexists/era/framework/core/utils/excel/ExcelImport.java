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

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.framework.core.exceptions.ImportExportException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * excel导入
 * 该工具包基于easypoi封装。实体注解请使用easy的实体注解
 *
 * @author pengcheng
 * @date 2021/9/1 14:23
 * @since 1.0.0
 */
public class ExcelImport {

    public static <T extends BaseExcelModel> List<String> importData(
            ImportChecker<T> checker, ImportDataWriter<T> writer, MultipartFile file, Class<T> clazz) {
        try (InputStream is = file.getInputStream()){
            return importData(checker, writer, is, clazz);
        } catch (IOException e) {
            return Collections.singletonList("导入Excel失败!");
        }
    }

    /**
     * 导入excel
     *
     * 无法通过校验的将会构造错误信息写出。通过校验的数据将通过writer进行数据写入
     *
     * @param checker   导入校验器
     * @param writer    导入数据写入器
     * @param is        导入的输入流 注意：输入流不会关闭
     * @param clazz     导入的model类型
     * @param <T>       BaseExcelModel
     * @return  导入失败部分的错误信息
     */
    public static <T extends BaseExcelModel> List<String> importData(
            ImportChecker<T> checker, ImportDataWriter<T> writer, InputStream is, Class<T> clazz) {
        List<String> failedMsgs = new ArrayList<>();
        try {
            ExcelImportResult<T> result = importData(checker, is, clazz);
            if (CollectionUtil.isBlank(result.getList()) && CollectionUtil.isBlank(result.getFailList())) {
                failedMsgs.add("导入文件无数据,请查看文件!");
                return failedMsgs;
            }
            failedMsgs.addAll(assembleErrorMsg(result));
            failedMsgs.addAll(writer.write(result.getList()));
        } catch (ImportExportException e) {
            failedMsgs.add(e.getMessage());
        }
        return failedMsgs;
    }

    /**
     * 导入excel
     *
     * @param checker 导入校验器
     * @param is      导入的输入流. 注意：输入流不会关闭
     * @param clazz   导入的model类型
     * @param <T>     BaseExcelModel
     * @return 导入的结果
     * @throws ImportExportException 导入的异常记录
     */
    public static <T extends BaseExcelModel> ExcelImportResult<T> importData(
            ImportChecker<T> checker, InputStream is, Class<T> clazz) throws ImportExportException {
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerify(true);
        importParams.setVerifyHandler((IExcelVerifyHandler<T>) model -> {
            ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
            try {
                checker.check(model);
            } catch (ImportExportException e) {
                result.setSuccess(false);
                result.setMsg(e.getMessage());
                return result;
            }
            return result;
        });

        try {
            return ExcelImportUtil.importExcelMore(is, clazz, importParams);
        } catch (Exception e) {
            throw new ImportExportException("导入Excel失败!");
        }
    }


    public static <T extends BaseExcelModel> List<String> assembleErrorMsg(ExcelImportResult<T> result) {
        List<String> failedMsgs = new ArrayList<>();
        List<T> failed = result.getFailList();
        if (CollectionUtil.isNotBlank(failed)) {
            failed.forEach(fail -> failedMsgs.add("第" + (fail.getRowNum() + 1) + "行, " + fail.getErrorMsg()));
        }
        return failedMsgs;
    }
}
