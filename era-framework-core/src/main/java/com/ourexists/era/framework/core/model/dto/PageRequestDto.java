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

package com.ourexists.era.framework.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: zhengcan
 * @Date: 2022/3/26
 * @Description: 分页实体请求类
 * @Version: 1.0.0 创建
 */
@ApiModel(value = "PageRequestDto")
public class PageRequestDto {

    @ApiModelProperty("分页起始值")
    private Integer pageIndex;

    @ApiModelProperty("分页大小")
    private Integer pageSize;

    @ApiModelProperty("排序字段")
    private String sortField;

    @ApiModelProperty("是否正序排序")
    private Boolean asc = true;

    public PageRequestDto() {
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getPageIndex() {
        if (null == pageIndex) {
            setPageIndex(1);
        }
        return pageIndex;
    }

    public Integer getPageSize() {
        if (null == pageSize) {
            setPageSize(10);
        }
        return pageSize;
    }

    public Integer getPageStart(){
        return (this.getPageIndex() - 1) * this.getPageSize();
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void doPager() {
        if (this.pageIndex != null && this.pageSize != null) {
            this.pageIndex = (this.pageIndex - 1) * this.pageSize;
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
