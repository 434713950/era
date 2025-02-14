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

package com.ourexists.era.framework.scheduler.core.accesstoken;

import com.ourexists.era.framework.scheduler.core.accesstoken.manager.JobAccessTokenManger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 微信AccessToken定时获取
 *
 * @author linzhihao
 * @date 2020/9/4 10:40
 */
@Slf4j
public class JobAccessTokenTimer {

    @Autowired
    private JobAccessTokenManger jobAccessTokenManger;

    /**
     * 定时刷新AccessToken
     */
    @Scheduled(fixedDelay = 7000000)
    public void refreshAccessToken() {
        log.debug("【令牌定时器】starting.....");
        jobAccessTokenManger.refresh();
        log.debug("【令牌定时器】end..........");
    }
}
