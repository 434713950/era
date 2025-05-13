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

package com.ourexists.era.oauth2.core;

import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2022/12/5 17:48
 * @since 1.1.0
 */
@Slf4j
public class UserManager {

    private final TokenStore tokenStore;

    public UserManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void updateCurrentUser(UserInfo userInfo, Integer accStatus) {
        //获取当前用户的认证信息
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        EraAuthenticationToken old = (EraAuthenticationToken) oAuth2Authentication.getUserAuthentication();
        Object o = old.getPrincipal();
        if (!(o instanceof EraUser)) {
            log.debug("update current user fail.unknow user type");
            return;
        }
        EraUser eraUser = (EraUser) o;
        eraUser.setAccStatus(accStatus);
        eraUser.setUserInfo(userInfo);
        EraAuthenticationToken ne = new EraAuthenticationToken(eraUser, old.getCredentials(), old.getAuthorities());
        ne.setDetails(old.getDetails());
        OAuth2Authentication d = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), ne);
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(oAuth2Authentication);
        log.debug("source token {}", accessToken.getValue());
        tokenStore.storeAccessToken(accessToken, d);
        UserContext.setUser(userInfo);
    }
}
