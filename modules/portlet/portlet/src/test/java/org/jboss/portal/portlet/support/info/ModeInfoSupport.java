/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.jboss.portal.portlet.support.info;

import org.jboss.portal.Mode;
import org.jboss.portal.common.i18n.LocalizedString;
import org.jboss.portal.portlet.api.info.ModeInfo;

import java.util.Locale;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5448 $
 */
public class ModeInfoSupport implements ModeInfo
{

   /** . */
   private final LocalizedString description;

   /** . */
   private final Mode mode;

   public ModeInfoSupport(Mode mode)
   {
      this.description = new LocalizedString(mode + " mode", Locale.ENGLISH);
      this.mode = mode;
   }

   public LocalizedString getDescription()
   {
      return description;
   }

   public Mode getMode()
   {
      return mode;
   }

   public String getModeName()
   {
      return mode.toString();
   }
}
