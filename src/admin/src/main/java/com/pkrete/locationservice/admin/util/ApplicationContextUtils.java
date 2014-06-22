/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
 
/**
 * This utility class offers access to Spring's applicationContext by
 * implementin the ApplicationContextAware interface. The applicationContext
 * is stored in a static variable and exposed through a get method so that
 * it can be accessed all through the application.
 * 
 * @author Petteri Kivimäki
 */
public class ApplicationContextUtils implements ApplicationContextAware {
 
  private static ApplicationContext ctx;
 
  /**
   * Set the applicationContext value.
   * @param appContext new value
   * @throws BeansException 
   */
  @Override
  public void setApplicationContext(ApplicationContext appContext)
      throws BeansException {
    ctx = appContext;
  }
 
  /**
   * Returns the applicationContext object.
   * @return  applicationContext object
   */
  public static ApplicationContext getApplicationContext() {
    return ctx;
  }
}

