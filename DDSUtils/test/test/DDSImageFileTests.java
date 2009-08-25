/**
 * 
 */
package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import junit.framework.TestCase;
import Helper.Stopwatch;
import JOGL.DDSImage;
import Model.DDSImageFile;

/**
 * @author danielsenff
 *
 */
public class DDSImageFileTests extends DDSTestCase {
	
	public void testFourCC() throws FileNotFoundException, IOException {
		assertTrue("is a dds image", DDSImage.isDDSImage(new FileInputStream(textureDDS1024)));
	}

	public void testDimensions() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		assertEquals(1024, ddsimage.getHeight());
		assertEquals(1024, ddsimage.getWidth());
	}
	
	public void testIsDXT5() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		assertEquals(DDSImage.D3DFMT_DXT5, ddsimage.getPixelformat());
	}
	
	public void testActivateMipMaps() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		assertEquals("activated mipmaps beforehand", true, ddsimage.hasMipMaps());
		ddsimage.activateMipMaps(true);
		assertEquals("activated mipmaps", true, ddsimage.hasMipMaps());
		ddsimage.activateMipMaps(false);
		assertEquals("deactivated mipmaps", false, ddsimage.hasMipMaps());
	}
	
	public void testHasMipMaps() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		assertEquals("has MipMaps", true, ddsimage.hasMipMaps());
	}

	public void testNumMipMap() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		
		try {
			int numMipMaps = DDSImage.read(textureDDS1024).getNumMipMaps();
			assertEquals("Number of MipMaps from original:",numMipMaps, ddsimage.getNumMipMaps());
			assertEquals("calculated number of MipMaps", numMipMaps, DDSImageFile.calculateMaxNumberOfMipMaps(ddsimage.getWidth(), ddsimage.getHeight()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void testDecompressBufferedImage() {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		BufferedImage originalImage = ddsimage.getData();
		
		stopwatch.stop();
		stopwatch.printMilliseconds("Time fo producing BufferedImage: ");
		
		assertEquals(1024, originalImage.getHeight());
		assertEquals(1024, originalImage.getWidth());
		assertEquals(BufferedImage.TYPE_4BYTE_ABGR, originalImage.getType());
		assertEquals("File objects are identical", textureDDS1024.getAbsolutePath(), ddsimage.getAbsolutePath());
	}
	/*
	public void testGetAllMipMapBuffer() {
		DDSImageFile ddsimage = DDSImageFileTests.loadDDSImage(original1024);
		assertMipMapBuffer(ddsimage.generateAllMipMapBuffer());
	}*/

	public void testMipMapBuffer() {
		DDSImageFile ddsimage = loadDDSImageFile(textureDDS1024);
		assertEquals("has mipmaps", true, ddsimage.hasMipMaps());
		assertEquals("numMipMaps", 11, ddsimage.getNumMipMaps());
//		assertMipMapBuffer(MipMapsUtil.compressMipMaps(ddsimage.getWidth(), 
//				ddsimage.getHeight(), ddsimage.generateAllMipMapBuffer(), Squish.CompressionType.DXT5));
	}
	
	private void assertMipMapBuffer(ByteBuffer[] buffer) {
	    int curSize = buffer[0].remaining();
	    for (int i = 0; i < buffer.length; i++) {
	      int remaining = buffer[i].remaining();
	      System.out.println("MipMapGen-Level: " + i + " remaining: " + remaining + " expected " + curSize);
	      
	      if (curSize>16) {
	    	  // squished, 16 is min blocksize
	    	  assertEquals(curSize, remaining);
	    	  curSize /= 4;  
	      }
	      
	    }
	}
	
	public void testIsPowerOfTwo() {
		assertEquals(true, DDSImageFile.isPowerOfTwo(2048));
		assertEquals(true, DDSImageFile.isPowerOfTwo(1024));
		assertEquals(true, DDSImageFile.isPowerOfTwo(512));
		
		assertEquals(false, DDSImageFile.isPowerOfTwo(612));
		assertEquals(false, DDSImageFile.isPowerOfTwo(33));
		assertEquals(false, DDSImageFile.isPowerOfTwo(78));
		
	}
	
}
