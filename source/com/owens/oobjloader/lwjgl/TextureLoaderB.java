/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * 
 * @author sean
 */
public class TextureLoaderB
{

	private final static int TEXTURE_LEVEL = 0;
	private HashMap<String, Integer> loadedTextures = new HashMap<String, Integer>();

	// @TODO: useTextureAlpha== false is broken at the moment. See comment below
	// for call to glTexImage2D()
	public int load(String filename, boolean useTextureAlpha)
			throws IOException
	{
		if (loadedTextures.containsKey(filename))
		{
			return loadedTextures.get(filename);
		}

		File imageFile = new File(filename);

		// System.err.println("For filename " + filename + ", canonical path = "
		// + imageFile.getCanonicalPath());
		if (!imageFile.exists())
		{
			System.err.println("ERROR, FIle " + filename + " does not exist");
		}
		if (!imageFile.canRead())
		{
			System.err.println("ERROR, FIle " + filename + " is not readable");
		}

		BufferedImage img = null;
		img = ImageIO.read(imageFile);

		int[] pixels = new int[img.getWidth() * img.getHeight()];
		PixelGrabber grabber = new PixelGrabber(img, 0, 0, img.getWidth(),
				img.getHeight(), pixels, 0, img.getWidth());
		try
		{
			grabber.grabPixels();
		} catch (InterruptedException e)
		{
			System.err
					.println("InterruptedException while trying to grab pixels, e="
							+ e);
			e.printStackTrace();
			return -1;
		}

		int bufLen = 0;
		if (useTextureAlpha)
		{
			bufLen = pixels.length * 4;
		} else
		{
			bufLen = pixels.length * 3;
		}
		ByteBuffer oglPixelBuf = BufferUtils.createByteBuffer(bufLen);

		for (int y = img.getHeight() - 1; y >= 0; y--)
		{
			for (int x = 0; x < img.getWidth(); x++)
			{
				int pixel = pixels[y * img.getWidth() + x];
				oglPixelBuf.put((byte) ((pixel >> 16) & 0xFF));
				oglPixelBuf.put((byte) ((pixel >> 8) & 0xFF));
				oglPixelBuf.put((byte) ((pixel >> 0) & 0xFF));
				if (useTextureAlpha)
				{
					oglPixelBuf.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
		}

		oglPixelBuf.flip();

		ByteBuffer temp = ByteBuffer.allocateDirect(4);
		temp.order(ByteOrder.nativeOrder());
		IntBuffer textBuf = temp.asIntBuffer();
		GL11.glGenTextures(textBuf);
		int textureID = textBuf.get(0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
				GL11.GL_MODULATE);

		// TODO: We take a flag for whether or not to store the alpha, but if we
		// don't check that flag for the options
		// below - they assume alpha, and if useTextureAlpha is false we only
		// allocate num pixels * 3, so the
		// call below then throws an exception. Long story short either always
		// assuem we want alpha or fix the
		// code below to change those options depending on flag setting.
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, TEXTURE_LEVEL, GL11.GL_RGBA8,
				img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, oglPixelBuf);

		return textureID;
	}
}
