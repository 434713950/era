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

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ourexists.era.framework.orm.mybatisplus.handler.EraTenantLineHandler;
import com.ourexists.era.framework.orm.mybatisplus.tenant.EraTenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * <p>mybatis-plus一些常用配置</p>
 *
 * @author pengcheng
 * @date 2018-11-06
 */
@Slf4j
public class EraMybatisPlusApplication {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(DataSource dataSource) throws SQLException {
        DbType dbType = DbType.MYSQL;
        String dbProductName = dataSource
                .getConnection()
                .getMetaData()
                .getDatabaseProductName()
                .toLowerCase();
        for (DbType value : DbType.values()) {
            if (dbProductName.contains(value.getDb())) {
                dbType = value;
                break;
            }
        }
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //多租户插件
        interceptor.addInnerInterceptor(new EraTenantLineInnerInterceptor(new EraTenantLineHandler()));
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        //防止全表更新与删除插件: BlockAttackInnerInterceptor
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new EraMyBatisPlusMetaObjectHandler();
    }
}
