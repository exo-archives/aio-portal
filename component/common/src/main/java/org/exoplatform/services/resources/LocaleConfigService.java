/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.resources;

import java.util.Collection;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com This Service
 *         is used to manage the locales that the applications can handle
 */
public interface LocaleConfigService {

  /**
   * @return Return the default LocaleConfig
   */
  public LocaleConfig getDefaultLocaleConfig();

  /**
   * @param lang a locale language
   * @return The LocalConfig
   */
  public LocaleConfig getLocaleConfig(String lang);

  /**
   * @return All the LocalConfig that manage by the service
   */
  public Collection<LocaleConfig> getLocalConfigs();

}
