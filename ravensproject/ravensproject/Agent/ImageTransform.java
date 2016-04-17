package ravensproject.Agent;

public class ImageTransform implements RavensTransform
{
	FigureImage fImage1;
	FigureImage fImage2;
	int pixelDiff = 0;
	float pixelScale = 0.0f; // scale factor of 1, means the figures have the same # of pixels!
	// when we mod the two, 
	// 1 = 500 pix
	// 2 = 1005
	// pixelDiff = 550
	// scale = 1
	// what is our tolerance where we say it is a valid scale;
	//const int scaleThreshold = 5;
	public ImageTransform(FigureImage f1, FigureImage f2)
	{
		fImage1 = f1;
		fImage2 = f2;
		pixelScale = (float) f2.numPix / (float) f1.numPix;
		GetDiffScore();
	}
	
	public void GetDiffScore()
	{
		pixelDiff = fImage1.numPix - fImage2.numPix;
	}

}