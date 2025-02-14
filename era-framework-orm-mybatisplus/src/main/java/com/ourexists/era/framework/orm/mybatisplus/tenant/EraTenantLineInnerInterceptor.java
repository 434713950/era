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

package com.ourexists.era.framework.orm.mybatisplus.tenant;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.user.EraDataAccessAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengcheng
 * @date 2022/7/25 17:15
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EraTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private TenantLineHandler tenantLineHandler;

    public EraTenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
        super(tenantLineHandler);
        this.tenantLineHandler = tenantLineHandler;
    }

    protected BinaryExpression andLikeExpression(Table table, Expression where, OperatorModel operatorModel) {
        if (!EraDataAccessAuth.checkTenantControlPower(operatorModel)){
            return super.andExpression(table, where);
        } else {
            return likeExpression(table, where);
        }
    }

    protected BinaryExpression likeExpression(Table table, Expression where) {
        StringValue tenantIdStr = (StringValue) tenantLineHandler.getTenantId();
        StringValue like = new StringValue(tenantIdStr.getValue() + "%");
        //获得where条件表达式
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(this.getAliasColumn(table));
        likeExpression.setRightExpression(like);
        if (null != where) {
            if (where instanceof OrExpression) {
                return new AndExpression(likeExpression, new Parenthesis(where));
            } else {
                return new AndExpression(likeExpression, where);
            }
        }
        return likeExpression;
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        final Table table = update.getTable();
        if (tenantLineHandler.ignoreTable(table.getName())) {
            // 过滤退出执行
            return;
        }
        update.setWhere(this.andLikeExpression(table, update.getWhere(), OperatorModel.UPDATE));

    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        if (tenantLineHandler.ignoreTable(delete.getTable().getName())) {
            // 过滤退出执行
            return;
        }
        delete.setWhere(this.andLikeExpression(delete.getTable(), delete.getWhere(), OperatorModel.DELETE));
    }

    @Override
    protected Expression builderExpression(Expression currentExpression, List<Table> tables) {
        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(tables)) {
            return currentExpression;
        }
        // 构造每张表的条件
        List<Table> tempTables = tables.stream()
                .filter(x -> !tenantLineHandler.ignoreTable(x.getName()))
                .collect(Collectors.toList());

        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(tempTables)) {
            return currentExpression;
        }

        List<BinaryExpression> equalsTos = new ArrayList<>();
        Expression tenantId = tenantLineHandler.getTenantId();

        if (!EraDataAccessAuth.checkTenantControlPower(OperatorModel.QUERY)) {
            equalsTos = tempTables.stream()
                    .map(item -> new EqualsTo(getAliasColumn(item), tenantId))
                    .collect(Collectors.toList());
        } else {
            for (Table item : tempTables) {
                StringValue tenantIdStr = (StringValue) tenantId;
                StringValue like = new StringValue(tenantIdStr.getValue() + "%");
                LikeExpression likeExpression = new LikeExpression();
                likeExpression.setLeftExpression(getAliasColumn(item));
                likeExpression.setRightExpression(like);
                equalsTos.add(likeExpression);
            }
        }

        // 注入的表达式
        Expression injectExpression = equalsTos.get(0);
        // 如果有多表，则用 and 连接
        if (equalsTos.size() > 1) {
            for (int i = 1; i < equalsTos.size(); i++) {
                injectExpression = new AndExpression(injectExpression, equalsTos.get(i));
            }
        }

        if (currentExpression == null) {
            return injectExpression;
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), injectExpression);
        } else {
            return new AndExpression(currentExpression, injectExpression);
        }
    }

}
