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
package org.exoplatform.portal.gadget.core;

import org.apache.shindig.common.JsonContainerConfig;
import org.apache.shindig.common.ContainerConfigException;
import org.apache.shindig.auth.BlobCrypterSecurityTokenDecoder;
import org.apache.commons.logging.Log;
import org.exoplatform.container.monitor.jvm.J2EEServerInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.commons.utils.Safe;
import com.google.inject.name.Named;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * The goal of the container config subclass is to integrate security key file along
 * with exo configuration. The implementation will look for a file named <i>key.txt</i>
 * in the <i>gadgets</i> directory of the configuration directoryreturned by the
 * {@link org.exoplatform.container.monitor.jvm.J2EEServerInfo#getExoConfigurationDirectory()}
 * method. If no such valid file can be found then it will let the parent class perform the location.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoContainerConfig extends JsonContainerConfig {

  /** . */
  private Log log = ExoLogger.getLogger(ExoContainerConfig.class);

  /** . */
  private String keyPath;

  @Inject
  public ExoContainerConfig(@Named("shindig.containers.default") String s) throws ContainerConfigException {
    super(s);

    //
    J2EEServerInfo info = new J2EEServerInfo();

    //
    String confPath = info.getExoConfigurationDirectory();
    if (confPath != null) {
      File confDir = new File(confPath);
      if (!confDir.exists()) {
        log.debug("Exo conf directory (" + confPath + ") does not exist");
      } else {
        if (!confDir.isDirectory()) {
          log.debug("Exo conf directory (" + confPath + ") is not a directory");
        } else {
          File keyFile = new File(confDir, "gadgets/key.txt");
          String keyPath = keyFile.getAbsolutePath();
          if (!keyFile.isFile()) {
            log.debug("Found key file " + keyPath + " but it's not a file");
          } else {
            if (!keyFile.exists()) {
              log.debug("No key file found at path " + keyPath + " generating a new key and saving it");
              String key = generateKey();
              Writer out = null;
              try {
                out = new FileWriter(keyFile);
                out.write(key);
                out.write('\n');
                log.info("Generated key file " + keyPath + " for eXo Gadgets");
              } catch (IOException e) {
                log.error("Coult not create key file " + keyPath, e);
              } finally {
                Safe.close(out);
              }
            } else {
              log.info("Found key file " + keyPath + " for gadgets security");
            }
            this.keyPath = keyPath;
          }
        }
      }
    }
  }


  @Override
  public Object getJson(String container, String parameter) {
    if (parameter.equals(BlobCrypterSecurityTokenDecoder.SECURITY_TOKEN_KEY_FILE) && keyPath != null) {
      return keyPath;
    }
    return super.getJson(container, parameter);
  }

  private static String generateKey() {
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed(System.currentTimeMillis());
      byte bytes[] = new byte[32];
      random.nextBytes(bytes);
      BASE64Encoder encoder = new BASE64Encoder();
      return encoder.encode(bytes);
    }
    catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }
}
