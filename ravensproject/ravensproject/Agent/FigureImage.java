package ravensproject.Agent;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ravensproject.RavensFigure;

class FigureImage
{
	String figName;
	BufferedImage img;
	int numPix = 0;
	
	public FigureImage(RavensFigure fig)
	{
		figName = fig.getName();
		
		try {
			img = ImageIO.read(new File(fig.getVisual()));

			//img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
			for(int i = 0; i < img.getWidth(); i ++)
			{
				for(int j = 0; j < img.getHeight(); j ++)
				{
					int pix = img.getRGB(i, j);
					Color c = new Color(pix);
					int alp = c.getAlpha();
					if((c.getRed() == 0) && (c.getGreen() == 0) && (c.getBlue() == 0))
					{
						numPix++;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
