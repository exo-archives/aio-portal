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

package org.exoplatform.services.chars.chardet ;


public class EUCTWVerifier extends Verifier {

  public EUCTWVerifier() {

    cclass = new int[256/8] ;

    cclass[0] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[1] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[2] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[3] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((0) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[4] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[5] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[6] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[7] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[8] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[9] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[10] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[11] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[12] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[13] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[14] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[15] = ((((  ((((  (((( 2) << 4) | (2)))  ) << 8) | (((((2) << 4) | ( 2))) ))) ) << 16) | (  ((((  ((((2) << 4) | (2))) ) << 8) | (   ((((2) << 4) | (2))) )))))) ;
    cclass[16] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[17] = ((((  ((((  (((( 0) << 4) | (6)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[18] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[19] = ((((  ((((  (((( 0) << 4) | (0)))  ) << 8) | (((((0) << 4) | ( 0))) ))) ) << 16) | (  ((((  ((((0) << 4) | (0))) ) << 8) | (   ((((0) << 4) | (0))) )))))) ;
    cclass[20] = ((((  ((((  (((( 4) << 4) | (4)))  ) << 8) | (((((4) << 4) | ( 4))) ))) ) << 16) | (  ((((  ((((4) << 4) | (4))) ) << 8) | (   ((((3) << 4) | (0))) )))))) ;
    cclass[21] = ((((  ((((  (((( 1) << 4) | (1)))  ) << 8) | (((((1) << 4) | ( 1))) ))) ) << 16) | (  ((((  ((((1) << 4) | (1))) ) << 8) | (   ((((5) << 4) | (5))) )))))) ;
    cclass[22] = ((((  ((((  (((( 1) << 4) | (1)))  ) << 8) | (((((1) << 4) | ( 1))) ))) ) << 16) | (  ((((  ((((1) << 4) | (1))) ) << 8) | (   ((((1) << 4) | (1))) )))))) ;
    cclass[23] = ((((  ((((  (((( 1) << 4) | (1)))  ) << 8) | (((((1) << 4) | ( 1))) ))) ) << 16) | (  ((((  ((((1) << 4) | (1))) ) << 8) | (   ((((1) << 4) | (1))) )))))) ;
    cclass[24] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((1) << 4) | (3))) ) << 8) | (   ((((1) << 4) | (1))) )))))) ;
    cclass[25] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[26] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[27] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[28] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[29] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[30] = ((((  ((((  (((( 3) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;
    cclass[31] = ((((  ((((  (((( 0) << 4) | (3)))  ) << 8) | (((((3) << 4) | ( 3))) ))) ) << 16) | (  ((((  ((((3) << 4) | (3))) ) << 8) | (   ((((3) << 4) | (3))) )))))) ;

    states = new int[6] ;

    states[0] = ((((  ((((  (((( eError) << 4) | (     4)))  ) << 8) | (((((     3) << 4) | (      3))) ))) ) << 16) | (  ((((  ((((     3) << 4) | (eStart))) ) << 8) | (   ((((eError) << 4) | (eError))) )))))) ;
    states[1] = ((((  ((((  (((( eItsMe) << 4) | (eItsMe)))  ) << 8) | (((((eError) << 4) | ( eError))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eError))) ) << 8) | (   ((((eError) << 4) | (eError))) )))))) ;
    states[2] = ((((  ((((  (((( eError) << 4) | (eStart)))  ) << 8) | (((((eError) << 4) | ( eItsMe))) ))) ) << 16) | (  ((((  ((((eItsMe) << 4) | (eItsMe))) ) << 8) | (   ((((eItsMe) << 4) | (eItsMe))) )))))) ;
    states[3] = ((((  ((((  (((( eError) << 4) | (eError)))  ) << 8) | (((((eError) << 4) | ( eError))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eStart))) ) << 8) | (   ((((eStart) << 4) | (eStart))) )))))) ;
    states[4] = ((((  ((((  (((( eStart) << 4) | (eStart)))  ) << 8) | (((((eError) << 4) | ( eStart))) ))) ) << 16) | (  ((((  ((((eError) << 4) | (eError))) ) << 8) | (   ((((eError) << 4) | (     5))) )))))) ;
    states[5] = ((((  ((((  (((( eStart) << 4) | (eStart)))  ) << 8) | (((((eStart) << 4) | ( eStart))) ))) ) << 16) | (  ((((  ((((eStart) << 4) | (eStart))) ) << 8) | (   ((((eError) << 4) | (eStart))) )))))) ;

    charset =  "x-euc-tw";
    stFactor =  7;

  }

}
