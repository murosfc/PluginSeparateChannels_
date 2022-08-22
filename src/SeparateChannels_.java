import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.ImageStack;

import ij.IJ;
import ij.process.ImageProcessor;

public class SeparateChannels_ implements PlugIn{
	private ImagePlus image, redChannel, greenChannel, blueChannel;
	
	public void run(String arg) {
		this.image = IJ.getImage();
		if (this.image.getType() != ImagePlus.COLOR_RGB) {
			IJ.error("The image must be Type RGB in order to run this plugin");
		}
		else {
			separateChannels();
		}	
	}
	
	private void separateChannels(){		
		int x, y, pixelValue[] = {0,0,0};
		int width = this.image.getWidth(), height=this.image.getHeight();
		
		ImageProcessor[] processors = this.getProcessors(width, height);		
		
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				pixelValue = processors[3].getPixel(x, y, pixelValue);
				processors[0].putPixel(x, y, pixelValue[0]);
				processors[1].putPixel(x, y, pixelValue[1]);
				processors[2].putPixel(x, y, pixelValue[2]);
				
			}
		}		
		this.image.close();
		this.getStackedImages().show();			
	}
	
	private ImageProcessor[] getProcessors(int width, int height) {
		ImageProcessor[] processors = new ImageProcessor[4];
		
		this.redChannel = IJ.createImage("RED", "8-bit", width, height, 1);
		this.greenChannel = IJ.createImage("GREEN", "8-bit", width, height, 1);
		this.blueChannel = IJ.createImage("BLUE", "8-bit", width, height, 1);
		
		processors[0] = redChannel.getProcessor();
		processors[1] = greenChannel.getProcessor();
		processors[2] = blueChannel.getProcessor();
		processors[3] = this.image.getProcessor();
		
		return processors;
	}
	
	private ImagePlus getStackedImages() {
		ImagePlus[] imgs = new ImagePlus[3];
		imgs[0] = redChannel;		
		imgs[1] = greenChannel;
		imgs[2] = blueChannel;	
		ImageStack stack = ImageStack.create(imgs);
		return new ImagePlus("RGB by channel", stack);			
	}

}