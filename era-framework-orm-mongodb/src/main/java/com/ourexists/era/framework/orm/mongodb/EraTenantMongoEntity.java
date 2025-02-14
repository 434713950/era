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

package com.ourexists.era.framework.orm.mongodb;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pengcheng
 * @date 2022/8/2 14:21
 * @since 1.1.0
 */
@Getter
@Setter
public class EraTenantMongoEntity extends EraMongoEntity{

    private static final long serialVersionUID = 239737921508782785L;
    
    private String tenantId;
}
