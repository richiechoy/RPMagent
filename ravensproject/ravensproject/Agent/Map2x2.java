package ravensproject.Agent;

public class Map2x2 
{
	ImageTransform Transform1;
	
	public Map2x2(ImageTransform transform)
	{
		Transform1 = transform;	
	}
	// easy to compare transforms 1-1
	int compareToMap(Map2x2 other)
	{
		return Math.abs(Transform1.pixelDiff - other.Transform1.pixelDiff);
	}
}
