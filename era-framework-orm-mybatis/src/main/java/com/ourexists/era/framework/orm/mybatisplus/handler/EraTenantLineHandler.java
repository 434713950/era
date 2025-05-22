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

package com.ourexists.era.framework.orm.mybatisplus.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.util.StringUtils;

/**
 * @author pengcheng
 * @date 2022/7/8 10:33
 * @since 1.1.0
 */
@Slf4j
public class EraTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        String tenantId = UserContext.getTenant().getTenantId();
        return new StringValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (!tableName.startsWith("t_")) {
            return true;
        }
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo == null) {
            return true;
        }
        String tenantId = tenantInfo.getTenantId();
        if (StringUtils.isEmpty(tenantId)) {
            return true;
        }
        return tenantInfo.avoidTenantCondition();
    }
}
