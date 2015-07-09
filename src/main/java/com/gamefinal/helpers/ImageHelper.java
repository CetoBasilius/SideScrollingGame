package com.gamefinal.helpers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;

public class ImageHelper {

	public static Image toImage(BufferedImage bufferedImage) {
		/*
		 * converts BufferedImage to Image
		 */
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }
	
	public static Image squareToTriangleImage(Image inImage) {
		/*
		 * slices an image creating a triangle image
		 */
        BufferedImage test = toBufferedImage(inImage);

        int ImageHeight = test.getHeight();
        int ImageWidth = test.getWidth();

        int[] raw = new int[ImageWidth * ImageHeight];
        test.getRGB(0, 0, ImageWidth, ImageHeight, raw, 0, ImageWidth);

        int alpha;
        int red;
        int green;
        int blue;

        int Counter1 = ImageWidth;
        int Counter2 = 0;

        int LolCounter = 0;

        for (int j = 0; j < raw.length; j++) {

            alpha = ((raw[j] & 0xff000000) >> 24);
            red = ((raw[j] & 0x00ff0000) >> 16);
            green = ((raw[j] & 0x0000ff00) >> 8);
            blue = raw[j] & 0x000000ff;

            if (alpha < 0) {
                alpha = 256 + alpha;
            }

            if (Counter2 >= ImageWidth) {
                Counter2 = 0;
                LolCounter = 0;
                Counter1--;
            }
            Counter2++;

            LolCounter++;
            if (LolCounter < Counter1) {
                alpha = 0;
            }

            if (red > 254) {
                red = 254;
            }
            if (green > 254) {
                green = 254;
            }
            if (blue > 254) {
                blue = 254;
            }
            if (red < 0) {
                red = 0;
            }
            if (green < 0) {
                green = 0;
            }
            if (blue < 0) {
                blue = 0;
            }

            Color c = new Color(red, green, blue, alpha);

            raw[j] = c.getRGB();
        }

        test.setRGB(0, 0, ImageWidth, ImageHeight, raw, 0, ImageWidth);
        return toImage(test);
    }
	
	public static BufferedImage toBufferedImage(Image image) {
		/*
		 * Converts Image to BufferedImage
		 */
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = true;//= hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (Exception e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static Image addBrightness(Image inImage, float brightness) {
    	/*
    	 * adds birghtness to an image, default brightness is 1.0f
    	 */
        BufferedImage work = toBufferedImage(inImage);
        RescaleOp op = new RescaleOp(1.0f+brightness, 0.0f, null);
        work = op.filter(work, null);
        return toImage(work);
    }

    public static Image reColorImage(Image inImage, int inRed, int inGreen, int inBlue) {
    	/*
    	 * adds RGB values to individual pixels in image
    	 */
        BufferedImage test = toBufferedImage(inImage);

        int[] raw = new int[test.getWidth() * test.getHeight()];
        test.getRGB(0, 0, test.getWidth(), test.getHeight(), raw, 0, test.getWidth());

        int alpha;
        int red;
        int green;
        int blue;

        for (int j = 0; j < raw.length; j++) {

            alpha = (raw[j] & 0xff000000) >> 24;
            red = (raw[j] & 0x00ff0000) >> 16;
            green = (raw[j] & 0x0000ff00) >> 8;
            blue = (raw[j] & 0x000000ff) >> 0;

            if (alpha < 0) {
                alpha = 254 - alpha;
            }

            red += inRed;
            green += inGreen;
            blue += inBlue;

            if (red > 254) {
                red = 254;
            }
            if (green > 254) {
                green = 254;
            }
            if (blue > 254) {
                blue = 254;
            }
            if (red < 0) {
                red = 0;
            }
            if (green < 0) {
                green = 0;
            }
            if (blue < 0) {
                blue = 0;
            }

            Color c = new Color(red, green, blue, alpha);
  
            raw[j] = c.getRGB();
        }

        test.setRGB(0, 0, test.getWidth(), test.getHeight(), raw, 0, test.getWidth());
        return toImage(test);
    }
}
