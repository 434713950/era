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

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ourexists.era.framework.core.user.EraDataAccessAuth;
import com.ourexists.era.framework.core.user.OperatorModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

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

    protected Expression andLikeExpression(Table table, Expression where, final String whereSegment, OperatorModel operatorModel) {
        if (!EraDataAccessAuth.checkTenantControlPower(operatorModel)) {
            return super.andExpression(table, where, whereSegment);
        } else {
            return likeExpression(table, where);
        }
    }

    protected Expression likeExpression(Table table, Expression where) {
        StringValue tenantIdStr = (StringValue) tenantLineHandler.getTenantId();
        StringValue like = new StringValue(tenantIdStr.getValue() + "%");
        //获得where条件表达式
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(this.getAliasColumn(table));
        likeExpression.setRightExpression(like);
        if (null != where) {
            if (where instanceof OrExpression) {
                return new ParenthesedExpressionList(new AndExpression(likeExpression, where));
            } else {
                return new AndExpression(likeExpression, where);
            }
        }
        return likeExpression;
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        obj = obj + "_" + OperatorModel.QUERY.name();
        super.processSelect(select, index, sql, obj);
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        obj = obj + "_" + OperatorModel.INSERT.name();
        super.processInsert(insert, index, sql, obj);
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        obj = obj + "_" + OperatorModel.UPDATE.name();
        super.processUpdate(update, index, sql, obj);
    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        obj = obj + "_" + OperatorModel.DELETE.name();
        super.processDelete(delete, index, sql, obj);
    }

    public Expression buildTableExpression(final Table table, final Expression where, final String whereSegment) {
        if (this.tenantLineHandler.ignoreTable(table.getName())) {
            return null;
        }
        String[] d = whereSegment.split("_");
        if (d.length < 2) {
            return null;
        }
        OperatorModel operatorModel = OperatorModel.valueOf(d[1]);
        return andLikeExpression(table, where, d[0], operatorModel);
    }
}
