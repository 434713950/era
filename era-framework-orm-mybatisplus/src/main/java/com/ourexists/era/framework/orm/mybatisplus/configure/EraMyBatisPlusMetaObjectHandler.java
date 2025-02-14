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

package com.ourexists.era.framework.orm.mybatisplus.configure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.user.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * <p>mybatis-plus数据填充策略</p>
 *
 * @author pengcheng
 * @date 2018-11-06
 */
public class EraMyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    public static final String TENANT = "tenantId";

    /**
     * 新增时间自动填充
     * 如需使用，在对应属性上标注 @TableField (fill = FieldFill.INSERT)
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        UserInfo userInfo = UserContext.getUser();
        String createdBy = "";
        String createdId = "";
        String updateBy = "";
        String updateId = "";
        if (userInfo != null) {
            if (getFieldValByName(EraMybatisConstants.FIELD_CREATE_BY, metaObject) == null) {
                createdBy = userInfo.getAccName();
            }
            if (getFieldValByName(EraMybatisConstants.FIELD_CREATE_ID, metaObject) == null) {
                createdId = userInfo.getUid();
            }
            if (getFieldValByName(EraMybatisConstants.FIELD_UPDATE_BY, metaObject) == null) {
                updateBy = userInfo.getAccName();
            }
            if (getFieldValByName(EraMybatisConstants.FIELD_UPDATE_ID, metaObject) == null) {
                updateId = userInfo.getUid();
            }
        }
        //兼容框架處理
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo != null) {
            if (metaObject.hasSetter(TENANT)) {
                Object tenantIdO = getFieldValByName(TENANT, metaObject);
                if (tenantIdO == null) {
                    setFieldValByName(TENANT, tenantInfo.getTenantId(), metaObject);
                } else {
                    String tenantId = (String) tenantIdO;
                    if (StringUtils.isEmpty(tenantId)) {
                        setFieldValByName(TENANT, tenantInfo.getTenantId(), metaObject);
                    }
                }
            }
        }
        Date curDate = new Date();
        setFieldValByName(EraMybatisConstants.FIELD_CREATE_BY, createdBy, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_CREATE_ID, createdId, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_BY, updateBy, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_ID, updateId, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_CREATE_TIME, curDate, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_TIME, curDate, metaObject);
    }

    /**w
     * 修改时间自动填充
     * 如需使用，在对应属性上标注 @TableField (fill = FieldFill.UPDATE)
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = UserContext.getUser();
        String updateBy = "";
        String updateId = "";
        if (userInfo != null) {
            if (getFieldValByName(EraMybatisConstants.FIELD_UPDATE_BY, metaObject) == null) {
                updateBy = userInfo.getUsername();
            }
            if (getFieldValByName(EraMybatisConstants.FIELD_UPDATE_ID, metaObject) == null) {
                updateId = userInfo.getUid();
            }
        }
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_BY, updateBy, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_ID, updateId, metaObject);
        setFieldValByName(EraMybatisConstants.FIELD_UPDATE_TIME, new Date(), metaObject);
    }
}
