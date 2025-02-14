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

package com.ourexists.era.framework.core.utils.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 *  该方法无法用在项目启动时加载bean
 * @author pengCheng
 * @date 2018/12/6
 */
public class SpringContextUtil {

    public static Object getBean(String name){
      return  getCurrentApplication().getBean(name);
    }

    public static <T> T  getBean(Class<T> beanClass){
        return  getCurrentApplication().getBean(beanClass);
    }

    public static ApplicationContext getCurrentApplication(){
        return ContextLoader.getCurrentWebApplicationContext();
    }
}
