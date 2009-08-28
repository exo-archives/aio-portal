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
package org.jboss.portal.common.i18n;

import org.jboss.portal.common.io.UndeclaredIOException;
import org.jboss.portal.common.util.ConversionException;
import org.jboss.portal.common.util.NullConversionException;
import org.jboss.portal.common.text.CharBuffer;
import org.jboss.portal.common.text.CharWriter;

import java.io.IOException;
import java.util.Locale;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public abstract class AbstractLocaleFormat implements LocaleFormat
{

   public Locale getLocale(String value) throws ConversionException
   {
      if (value == null)
      {
         throw new NullConversionException("No null locale value accepted");
      }
      return internalGetLocale(value);
   }

   public String toString(Locale locale) throws ConversionException
   {
      if (locale == null)
      {
         throw new NullConversionException("No null locale accepted");
      }
      return internalToString(locale);
   }

   public void write(Locale locale, CharWriter writer) throws IOException, ConversionException
   {
      if (locale == null)
      {
         throw new NullConversionException("No null locale accepted");
      }
      if (writer == null)
      {
         throw new IllegalArgumentException("No null writer accepted");
      }
      internalWrite(locale, writer);
   }

   protected abstract Locale internalGetLocale(String value) throws ConversionException;

   protected String internalToString(Locale locale) throws ConversionException
   {
      try
      {
         CharBuffer buffer = new CharBuffer();
         internalWrite(locale, buffer);
         return buffer.asString();
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }

   protected abstract void internalWrite(Locale locale, CharWriter writer) throws IOException, ConversionException;

}
