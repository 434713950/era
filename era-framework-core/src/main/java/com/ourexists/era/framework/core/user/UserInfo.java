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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户信息
 *
 * @author pengcheng
 * @date 2020/4/23 23:21
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -6314495604422944703L;

    private String id;

    private String uid;

    private String email;

    private String nickname;
    private String nickName;

    private String tel;

    private String accName;

    private String username;
    private String userName;

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

    private Integer gender;

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

    public String getUid() {
        return this.id;
    }

    public String getNickname() {
        return this.nickName;
    }

    public String getUsername() {
        return this.userName;
    }

    public Integer getGender() {
        return this.sex;
    }

    public String getTel() {
        return this.mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Date getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(Date settledTime) {
        this.settledTime = settledTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getPerfection() {
        return perfection;
    }

    public void setPerfection(Integer perfection) {
        this.perfection = perfection;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String, String> getTenantRoles() {
        return tenantRoles;
    }

    public void setTenantRoles(Map<String, String> tenantRoles) {
        this.tenantRoles = tenantRoles;
    }
}
