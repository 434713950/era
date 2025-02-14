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

package com.ourexists.era.framework.oauth2.token;

import com.ourexists.era.framework.oauth2.AuthConstants;
import com.ourexists.era.framework.oauth2.EraUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * jwt授权令牌装换器
 *
 * @author PengCheng
 * @date 2018/8/28
 */
public class EraJwtAccessTokenConverter extends JwtAccessTokenConverter {

    /**
     * 转换授权令牌
     * @param token
     * @param authentication
     * @return
     */
    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> map = (Map<String, Object>) super.convertAccessToken(token, authentication);
        //加入授权信息
        map.put("license", AuthConstants.LICENSE);
        Object pri = authentication.getPrincipal();
        if (pri instanceof EraUser) {
            EraUser eraUser = (EraUser)pri;
            map.put("accStatus", eraUser.getAccStatus());
        }
        return map;
    }

    /**
     * 提取授权令牌
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        return super.extractAccessToken(value, map);
    }

    /**
     * 提取认证信息
     * @param map
     * @return
     */
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        return super.extractAuthentication(map);
    }
}
