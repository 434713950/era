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

package com.ourexists.era.framework.oss;

import com.ourexists.era.framework.oss.template.AliOssTemplate;
import com.ourexists.era.framework.oss.template.MinioOssTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author pengcheng
 * @date 2022/3/2 15:32
 * @since 1.0.0
 */
@Import({OssProperties.class})
public class OssApplication {

    @Bean
    private OssTemplate ossTemplate(OssProperties ossProperties) {
        if (StringUtils.isEmpty(ossProperties.getType()) ||
                ossProperties.getType().equalsIgnoreCase(OssTypeEnum.ALI.name())) {
            return new AliOssTemplate(ossProperties);
        } else if (ossProperties.getType().equalsIgnoreCase(OssTypeEnum.MINIO.name())) {
            return new MinioOssTemplate(ossProperties);
        }
        return new AliOssTemplate(ossProperties);
    }
}
