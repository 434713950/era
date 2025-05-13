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

package com.ourexists.era.framework.webmvc.configure;

import com.ourexists.era.framework.webmvc.I18nUtil;
import com.ourexists.era.framework.webmvc.handler.ExceptionAnalysisHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author pengcheng
 * @date 2022/2/21 15:51
 * @since 1.0.0
 */
@Import({OpenApiConfigurer.class,
        LocaleConfigurer.class,
        WebMvcConfiguration.class,
        EraSecurityConfigurer.class,
        ExceptionAnalysisHandler.class})
public class EraEnhanceConfiguration {

    @Bean
    public I18nUtil i18nUtil(MessageSource messageSource) {
        return new I18nUtil(messageSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
