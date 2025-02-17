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

package com.ourexists.era.framework.core.utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author pengcheng
 * @date 2021/11/25 9:09
 * @since 1.0.0
 */
public class WeightRandom {

    /**
     * 计算权重，初始化或者重新定义权重时使用
     */
    public static <T> T doSelect(List<ItemWithWeight<T>> itemsWithWeights) {
        //所有的商品
        List<T> items = new ArrayList<>();
        // 计算权重总和
        double totalWeight = 0;
        for (ItemWithWeight<T> itemWithWeight : itemsWithWeights) {
            double weight = getWeight(itemWithWeight);
            if (weight < 0) {
                continue;
            }
            totalWeight += weight;
            items.add(itemWithWeight.getItem());
        }

        // 计算每个item的实际权重比例
        double[] actualWeightRatios = new double[items.size()];
        int index = 0;
        for (ItemWithWeight<T> itemWithWeight : itemsWithWeights) {
            double weight = getWeight(itemWithWeight);
            if (weight <= 0) {
                continue;
            }
            actualWeightRatios[index++] = weight / totalWeight;
        }

        // 计算每个item的权重范围
        // 权重范围起始位置
        double[] weights = new double[items.size()];
        double weightRangeStartPos = 0;
        for (int i = 0; i < index; i++) {
            weights[i] = weightRangeStartPos + actualWeightRatios[i];
            weightRangeStartPos += actualWeightRatios[i];
        }

        double random = ThreadLocalRandom.current().nextDouble();
        int idx = Arrays.binarySearch(weights, random);
        if (idx < 0) {
            idx = -idx - 1;
        } else {
            return items.get(idx);
        }

        if (idx < weights.length && random < weights[idx]) {
            return items.get(idx);
        }
        return items.get(0);
    }

    private static double getWeight(ItemWithWeight<?> itemWithWeight) {
        double weight = itemWithWeight.getWeight();
        if (Double.isInfinite(weight)) {
            weight = 10000.0D;
        }
        if (Double.isNaN(weight)) {
            weight = 1.0D;
        }
        return weight;
    }


    public static class ItemWithWeight<T> {
        T item;
        double weight;

        public ItemWithWeight(T item, double weight) {
            this.item = item;
            this.weight = weight;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
}
