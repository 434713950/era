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

package com.ourexists.era.framework.core.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户信息
 *
 * @author pengcheng
 * @date 2020/4/23 23:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -6314495604422944703L;

    private String id;

    private String email;

    private String nickName;

    private String accName;

    private String username;

    /**
     * 身份证号
     */
    private String idCard;


    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 账户入驻时间
     */
    private Date settledTime;

    /**
     * 账户过期时间
     */
    private Date expireTime;

    private String avatarUrl;

    private String country;

    private String province;

    private String city;

    private String language;

    private Integer sex;

    private String source;

    private String sourceId;

    private String unionId;

    private Integer perfection;

    private String mobile;

    private String details;

    /**
     * 所有的租戶及角色
     */
    private Map<String, String> tenantRoles;
}
