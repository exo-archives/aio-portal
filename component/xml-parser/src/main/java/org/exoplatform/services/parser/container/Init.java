package org.exoplatform.services.parser.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Oct 4, 2006  
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
public @interface Init {
  Class [] value () default {};
}
