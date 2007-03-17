/* -*- Mode: C; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*-
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1998 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s): 
 */
/* 
 * DO NOT EDIT THIS DOCUMENT MANUALLY !!!
 * THIS FILE IS AUTOMATICALLY GENERATED BY THE TOOLS UNDER
 *    AutoDetect/tools/
 */

package org.exoplatform.services.parser.chardet ;


public class EUCJPVerifier extends Verifier {

  public EUCJPVerifier() {

    cclass = new int[256/8] ;

    cclass[0] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[1] = ((((  ((((  (((( 5) << 4) | (5)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[2] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[3] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((5) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[4] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[5] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[6] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[7] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[8] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[9] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[10] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[11] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[12] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[13] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[14] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[15] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((4) << 4) | (4))) )))))) ;
    cclass[16] = ((((  ((((  (((( 5) << 4) | (5)))  ) << 8) | (((((5) << 4) | ( 5))) ))) ) << 16) | (  ((((  ((((5) << 4) | (5))) ) << 8) | (   ((((5) << 4) | (5))) )))))) ;
    cclass[17] = ((((  ((((  (((( 3) << 4) | (1)))  ) << 8) | (((((5) << 4) | ( 5))) ))) ) << 16) | (  ((((  ((((5) << 4) | (5))) ) << 8) | (   ((((5) << 4) | (5))) )))))) ;
    cclass[18] = ((((  ((((  (((( 5) << 4) | (5)))  ) << 8) | (((((5) << 4) | ( 5))) ))) ) << 16) | (  ((((  ((((5) << 4) | (5))) ) << 8) | (   ((((5) << 4) | (5))) )))))) ;
    cclass[19] = ((((  ((((  (((( 5) << 4) | (5)))  ) << 8) | (((((5) << 4) | ( 5))) ))) ) << 16) | (  ((((  ((((5) << 4) | (5))) ) << 8) | (   ((((5) << 4) | (5))) )))))) ;
    cclass[20] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (5))) )))))) ;
    cclass[21] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[22] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[23] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[24] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[25] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[26] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[27] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[28] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[29] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[30] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[31] = ((((  ((((  (((( 5) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;

    states = new int[5] ;

    states[0] = ((((  ((((  (((( eError) << 4) | (eError)))  ) << 8) | (((((eError) << 4) | ( eStart))) ))) ) << 16) | (  ((((  ((((     5) << 4) | (     3))) ) << 8) | (   ((((     4) << 4) | (     3))) )))))) ;
    states[1] = ((((  ((((  (((( eItsMe) << 4) | (eItsMe)))  ) << 8) | (((((eItsMe) << 4) | ( eItsMe))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eError))) ) << 8) | (   ((((eError) << 4) | (eError))) )))))) ;
    states[2] = ((((  ((((  (((( eError) << 4) | (eError)))  ) << 8) | (((((eError) << 4) | ( eStart))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eStart))) ) << 8) | (   ((((eItsMe) << 4) | (eItsMe))) )))))) ;
    states[3] = ((((  ((((  (((( eError) << 4) | (     3)))  ) << 8) | (((((eError) << 4) | ( eError))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eStart))) ) << 8) | (   ((((eError) << 4) | (eError))) )))))) ;
    states[4] = ((((  ((((  (((( eStart) << 4) | (eStart)))  ) << 8) | (((((eStart) << 4) | ( eStart))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eError))) ) << 8) | (   ((((eError) << 4) | (     3))) )))))) ;

    charset =  "EUC-JP";
    stFactor =  6;

  }

}
