/**
 *  NetP5 is a processing and java library for tcp and udp ip communication.
 *
 *  2006 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */

package sojamo.http;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.io.FileNotFoundException;
import processing.core.PImage;


public class HTTPImage {

  public static byte[] convert(PImage thePImage){
  ByteArrayOutputStream out = new ByteArrayOutputStream();
  BufferedImage img = new BufferedImage(thePImage.width, thePImage.height, BufferedImage.TYPE_INT_RGB );
  for(int i = 0; i < thePImage.width; i++)
  {
    for(int j = 0; j < thePImage.height; j++)
    {
      int id = j*thePImage.width+i;
      img.setRGB(i,j, thePImage.pixels[id]);
    }
  }
  try{
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam encpar = encoder.getDefaultJPEGEncodeParam(img);
    encpar.setQuality(1,false);
    encoder.setJPEGEncodeParam(encpar);
    encoder.encode(img,encpar);
  }
  catch(FileNotFoundException e){
    System.out.println(e);
  }
  catch(IOException ioe){
    System.out.println(ioe);
  }
  return out.toByteArray();
}


  public byte[] getJpeg() {
    int width = 40;
    int height = 40;
    BufferedImage img = new BufferedImage(width, height,
                                          BufferedImage.TYPE_INT_RGB);
    Graphics2D gp = img.createGraphics();

    gp.drawOval(10, 10, 20, 20);

    // loadPixels();
    // img.setRGB(0, 0, width, height, g.pixels, 0, width);

    String myExtension = "png";
    if (myExtension.equals("jpg")) {

      try {
        ByteArrayOutputStream myStream = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec
            .createJPEGEncoder(myStream);
        JPEGEncodeParam p = encoder.getDefaultJPEGEncodeParam(img);
        // set JPEG quality to 50% with baseline optimization
        p.setQuality(0.5f, true);
        encoder.setJPEGEncodeParam(p);
        encoder.encode(img);
        return myStream.toByteArray();
      }
      catch (IOException ioe) {
        System.out.println(ioe);
      }

    }
    else if (myExtension.equals("png")) {
      try {

        ByteArrayOutputStream myStream = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(img, "png", myStream);
        return myStream.toByteArray();
      }
      catch (Exception e) {
        System.err.println("error while saving as ");
        e.printStackTrace();
      }
    }
    return new byte[0];
  }

}
