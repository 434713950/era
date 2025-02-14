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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengcheng
 * @date 2022/4/11 18:54
 * @since 1.0.0
 */
public class TokenConfigurer {

    @Value("${era.authorization.privateKey:" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCJYCuldvXyMDl" +
            "nyk5Pd0ST9t3AnUeBrMB1Pu35bVbHDwmbz95M4c4rOiTVvnwJDalgphyVFw4lj5V" +
            "3GwBYwd+QUv68/VQE0L1C+dGIDHSXGWuO+wqvWVDedkDyB391v7R841BYfYjauEb" +
            "v4niaju79T/MDSuyOdbffqiN2NQ9vQ3oEYcR2zzPyhAIoooO7J3ChELuze2u5e6L" +
            "87NJDD4eD9E6miaqfHr+iRxcO5lxFu/NoCBLF6ufV5eWt0GtLtj6LFULCuXyHVn7" +
            "lpg0oz1mnZvAwuZwlplgRGUDYU7rvUMNQ9jpPb4rusZ4lV7auu/r6w5vByTG+OC1" +
            "FljUAxxjAgMBAAECggEAED1m2DO84f3u9MYtgpZEKcX4S6ou9cYFrz+LlsRmOSrR" +
            "dT2VB6RhyUHdaoW5/nZpA8s6yUV5OVqH+FKpzivkcpEvs9w4Rux1i13liJ4kzL3h" +
            "24MzrBePoudMWd6hc5xKUHGY/UEef7PvBI0vEXtlZ8GKBp2LbCseaC+GFedvSYKj" +
            "nJF5VH1OkVQJ4Hi91Ty37A0fctkAS0UJwrUHinNaOZH0/uEb8VkXe5sRUWjV/61g" +
            "B/us3D1esh0rgX8r2pf0BiYdpZVvmRvpbG7RFh6aV00Z9yurhilht3n8SYoyiR6D" +
            "a4DfLf4c7l2n21inKxDrcs3yOYSkqNbBty8sM+pqMQKBgQDhW9M/rF/9Zz0y9ZMF" +
            "oz1Av4wRWCZBOur7vnYoXjkzBCHPpELagBiWlHEGHGqCAgNPDh3ODwkdlNyGs/Yi" +
            "83O7DOzvAi/7kWxj3VMtLzg5lRUSVAG08X7I9s8yKJGspjyfJWaRmp/sSiK472Ej" +
            "Z4qCs2wv7lDQWWQenxIMiqQ+FQKBgQDci0I0z+x9BT3LNj4DuJ43td5YoTASncJ9" +
            "IomsMNSBlBV8jS7pbI9gUbEe6bKsAP3JZ9LKKOZZw1Gdmm8OBLzE6kCfTPvsOF3o" +
            "L5+3YDbI48WPG/vQXzPWuQur6znqZ9LN1woTtjnvjxjuA7duLWr2VvicN1r6yqGe" +
            "+L3/vnYGlwKBgQCDUKprs9nLItk6VHZzFoeerv7DLIY+BELgpj7bjuvtmj7Ja4G5" +
            "KGMrexvQ38YAM4QSELu1UnWRUyidJUgLXajWGdYF/1ROVpK+Lj1FWvp8My6wG1tT" +
            "QFUMbSSWqaUY4VT5tyVpOpxtZ1WMRZBovPCs8DfeRhO5FB2O6knuHOtPcQKBgGVJ" +
            "B2TdwMxB5fk3tg9bcD5BphWqITvLfBCgFf4ghtfjvGJxLIRDOS2RFvkNduMLqYIf" +
            "zmzNj8zVqNvqmuojPtZohrkiT+hSkr4ZcQ50f3SPNqHcxi8SKeqJHVUdOIHwoJ2s" +
            "DpKy87STUW2uA0X0UdVEZ/TGb2ASQ1uQ/SFAxU+NAoGAB7RrvSrxduXAIL1yWUso" +
            "P9JvuNQSFSqrwvwxD0Z27okiQRtH5Witgc03HRhv2Hw42eCElqpiUJaEwSwogORg" +
            "eoIb53tLStwhCrUbTKCHtvQZblVkrhHMxCeRdR02r2Lkh//64wsKO+tHT7Dv6RGr" +
            "BU2YdA/9kIE11pw8bp3gOro=")
    private String privateKey ;


    @Value("${era.authorization.publicKey:" +
            "-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwiWArpXb18jA5Z8pOT3d" +
            "Ek/bdwJ1HgazAdT7t+W1Wxw8Jm8/eTOHOKzok1b58CQ2pYKYclRcOJY+VdxsAWMH" +
            "fkFL+vP1UBNC9QvnRiAx0lxlrjvsKr1lQ3nZA8gd/db+0fONQWH2I2rhG7+J4mo7" +
            "u/U/zA0rsjnW336ojdjUPb0N6BGHEds8z8oQCKKKDuydwoRC7s3truXui/OzSQw+" +
            "Hg/ROpomqnx6/okcXDuZcRbvzaAgSxern1eXlrdBrS7Y+ixVCwrl8h1Z+5aYNKM9" +
            "Zp2bwMLmcJaZYERlA2FO671DDUPY6T2+K7rGeJVe2rrv6+sObwckxvjgtRZY1AMc" +
            "YwIDAQAB" +
            "-----END PUBLIC KEY-----}")
    private String publicKey;


    /**
     * tokenstore 定制化处理
     * 这里使用自定义的redis令牌存储做处理
     * @return TokenStore
     */
    @Bean
    public TokenStore redisTokenStore(RedisTemplate redisTemplate) {
        return new RedisTemplateTokenStore(redisTemplate);
    }

    /**
     * token增强器
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(2);
            additionalInfo.put("license", AuthConstants.LICENSE);
            Object pri = authentication.getPrincipal();
            if (pri instanceof EraUser) {
                EraUser eraUser = (EraUser) pri;
                additionalInfo.put("accStatus", eraUser.getAccStatus());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        EraJwtAccessTokenConverter jwtAccessTokenConverter = new EraJwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(privateKey);
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }
}
