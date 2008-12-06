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
package org.exoplatform.portal.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.webui.skin.SkinService;
import org.exoplatform.portal.webui.skin.ResourceRenderer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.cache.concurrent.ConcurrentFIFOExoCache;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.commons.utils.TextEncoder;
import org.exoplatform.commons.utils.CharsetTextEncoder;
import org.exoplatform.commons.utils.TableCharEncoder;
import org.exoplatform.commons.utils.CharsetCharEncoder;
import org.exoplatform.commons.utils.PortalPrinter;
import org.exoplatform.commons.utils.Safe;

public class ResourceRequestFilter implements Filter  {
  
  protected static Log log = ExoLogger.getLogger("portal:ResourceRequestFilter");
 
  private FilterConfig cfg;

  private ImageType[] imageTypes = ImageType.values();

  private ConcurrentMap<String, FutureTask<Image>> mirroredImageCache = new ConcurrentHashMap<String, FutureTask<Image>>();

  private ExoCache cssCache = new ConcurrentFIFOExoCache(50);

  public void init(FilterConfig filterConfig) {
    cfg = filterConfig;
    log.info("Cache eXo Resource at client: " + !PropertyManager.isDevelopping());
  }

  /** The optimized encoder. */
  private static final TextEncoder encoder = new CharsetTextEncoder(new TableCharEncoder(CharsetCharEncoder.getUTF8()));

  public void doFilter(ServletRequest request, final ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request ;
    String uri = URLDecoder.decode(httpRequest.getRequestURI(),"UTF-8");
    final HttpServletResponse httpResponse = (HttpServletResponse)  response ;
    ExoContainer portalContainer = ExoContainerContext.getCurrentContainer();
    SkinService skinService = (SkinService) portalContainer.getComponentInstanceOfType(SkinService.class);

    //
    if(uri.endsWith(".css")) {
      final OutputStream out = response.getOutputStream();
      final Appendable app = new Appendable() {
        public Appendable append(CharSequence csq) throws IOException {
          // julien : yeah there is a nasty cast but for now it is ok as we know it is a string
          // need to work on an optimized appender for
          String s = (String)csq;

          // Get existing bytes
          byte[] bytes = null;
          try {
            bytes = (byte[])cssCache.get(s);
          }
          catch (Exception e) {
            e.printStackTrace();
          }

          // Get bytes if needed
          if (bytes == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(s.length() * 2);
            encoder.encode(s, 0, s.length(), baos);
            baos.flush();
            bytes = baos.toByteArray();
            try {
              cssCache.put(s, bytes);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }

          //
          response.setContentType("text/css; charset=UTF-8");
          //response.setCharacterEncoding("UTF8");
          response.setContentLength(bytes.length);

          //
          try {
            out.write(bytes);
          }
          catch (IOException ignore) {
          }
          finally {
            Safe.close(out);
          }

          //
          return this;
        }
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
          throw new UnsupportedOperationException("Should no be called");
        }
        public Appendable append(char c) throws IOException {
          encoder.encode(c, out);
          return this;
        }
      };

      ResourceRenderer renderer = new ResourceRenderer() {
        public Appendable getAppendable() {
          //
          return app;
        }
        public void setExpiration(long seconds) {
          if (seconds > 0) {
            httpResponse.addHeader("Cache-Control", "max-age=" + seconds + ",s-maxage=" + seconds) ;
          } else {
            httpResponse.setHeader("Cache-Control", "no-cache");
          }
        }
      };

      //
      try {
        skinService.renderCSS(renderer, uri);
        log.debug("Use a merged CSS: " + uri);
      }
      catch (Exception e) {
        log.error("Could not render css " + uri, e);
        httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    } else {

      // Fast matching
      final int len = uri.length();
      if (len >= 7 &&
          uri.charAt(len - 7) == '-' &&
          uri.charAt(len - 6) == 'r' &&
          uri.charAt(len - 5) == 't') {
        for (final ImageType imageType : imageTypes) {
          if (imageType.matches(uri)) {
            final String resource = uri.substring(httpRequest.getContextPath().length(), len - 7) + uri.substring(len - 4);
            FutureTask<Image> futureImg = mirroredImageCache.get(resource);
            if (futureImg == null)
            {
              FutureTask<Image> tmp = new FutureTask<Image>(new Callable<Image>() {
                public Image call() throws Exception {
                  InputStream in = cfg.getServletContext().getResourceAsStream(resource);
                  if (in == null) {
                    return null;
                  }

                  //
                  BufferedImage img = ImageIO.read(in);
                  AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                  tx.translate(-img.getWidth(null), 0);
                  AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                  img = op.filter(img, null);
                  ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
                  ImageIO.write(img, imageType.getFormat(), baos);
                  baos.close();
                  return new Image(imageType, baos.toByteArray());
                }
              });

              //
              futureImg = mirroredImageCache.putIfAbsent(resource, tmp);
              if (futureImg == null) {
                futureImg = tmp;
                futureImg.run();
              }
            }

            //
            try {
              Image img = futureImg.get();
              if (img != null) {
                httpResponse.setContentType(img.type.getMimeType());
                httpResponse.setContentLength(img.bytes.length);
                OutputStream out = httpResponse.getOutputStream();
                out.write(img.bytes);
                out.close();
              } else {
                mirroredImageCache.remove(resource);
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
              }
              return;
            }
            catch (InterruptedException e) {
              // Find out what is relevant to do
              e.printStackTrace();
            }
            catch (ExecutionException e) {
              // Cleanup
              e.printStackTrace();
              mirroredImageCache.remove(resource);
            }
          }
        }
      }

      //
      if(!PropertyManager.isDevelopping()) {
          httpResponse.addHeader("Cache-Control", "max-age=2592000,s-maxage=2592000") ;
      } else {
        if(uri.endsWith(".jstmpl") || uri.endsWith(".js")) {
          httpResponse.setHeader("Cache-Control", "no-cache");
        }
        if(log.isDebugEnabled())
          log.debug(" Load Resource: " + uri);
      }
      chain.doFilter(request, response) ;
    }
  }

  public void destroy() { }
}  