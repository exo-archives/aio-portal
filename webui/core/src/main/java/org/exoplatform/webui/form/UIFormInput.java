/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.form;

import java.util.List;

import org.exoplatform.webui.form.validator.Validator;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 6, 2006
 */
public interface UIFormInput<E> {
  
  public String getName()  ;
  public String getBindingField()  ;
  
  public String getLabel();

  public UIFormInput addValidator(Class clazz) throws Exception ;
  public List<Validator>  getValidators()  ;
  
  public E getValue() throws Exception ;
  public UIFormInput setValue(E value) throws Exception;
  
  public Class<E> getTypeValue() ;
  
  public void reset();
  
}