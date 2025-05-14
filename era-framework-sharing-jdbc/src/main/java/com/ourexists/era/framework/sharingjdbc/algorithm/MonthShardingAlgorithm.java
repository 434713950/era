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

package com.ourexists.era.framework.sharingjdbc.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/7/25 9:53
 * @since 1.0.0
 */
public class MonthShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    private final DateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("yyyyMM");

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        String time = STANDARD_DATE_FORMAT.format(preciseShardingValue.getValue());
        return preciseShardingValue.getLogicTableName() +
                "_" + time;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        List<String> list = new ArrayList<>();
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        Date lowerDate = valueRange.lowerEndpoint();
        Date upperDate = valueRange.upperEndpoint();
        List<String> suffixList = getSuffixListForRange(lowerDate, upperDate);
        for (String tableName : collection) {
            if (containTableName(suffixList, tableName)) {
                list.add(tableName);
            }
        }
        return list;
    }

    @Override
    public String getType() {
        return "MONTH";
    }

    private List<String> getSuffixListForRange(Date lowerSuffix, Date upperSuffix) {
        List<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(lowerSuffix);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(upperSuffix);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        while (min.before(max)) {
            result.add(STANDARD_DATE_FORMAT.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

    private boolean containTableName(List<String> suffixList, String tableName) {
        boolean flag = false;
        for (String s : suffixList) {
            if (tableName.endsWith(s)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
