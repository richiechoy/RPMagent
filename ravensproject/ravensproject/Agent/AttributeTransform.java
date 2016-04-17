package ravensproject.Agent;

import java.util.Arrays;
import java.util.List;

public class AttributeTransform implements RavensTransform{
	String AttName;
	String AttAVal; // keep both vals for raw change 
	String AttBVal;
	String ValChange; // this is the processed value change, so we can understand the human-understandable change between two objects
	//  we can also do better than a "-" separated string, maybe make an object ValueTransform
	
	public AttributeTransform(String name, String a, String b)
	{
		AttName = name;
		AttAVal = a;
		AttBVal = b;
		GetDiffScore();
	}
		
	public void GetDiffScore()
	{
		String nullAtt = "N/A";
		if(AttAVal == null || AttAVal.isEmpty())
		{
			ValChange = nullAtt + "-" + AttBVal;
			return;
		}
		else if(AttBVal == null || AttBVal.isEmpty())
		{
			ValChange = AttAVal + "-" + nullAtt;
			return;
		}
		
		
		switch(AttName)
		{
			case "angle":
				// if there is a difference in the angle, find the scaling factor
				int angleA = Integer.parseInt(AttAVal);
				int angleB = Integer.parseInt(AttBVal);
				int scaleFactor = 0;
				
				if(angleA != 0)
				{
					if( (angleB % angleA) == 0)
					{
						scaleFactor = angleB/angleA;
					}
					else
					{
						for(int i = 0; i < 100; i ++)
						{
							if( ((angleB + 360 * i) % angleA) == 0)
							{
								scaleFactor = (angleB + 360 * i)/angleA;
								break;
							}
						}
					}
				}
				// if we couldn't find the scale factor, just record the raw change
				if(scaleFactor == 0)
				{
					ValChange = AttAVal + "-" + AttBVal;
				}
				
				ValChange = Integer.toString(scaleFactor);
				break;
				
			case "alignment":
				String vertA = AttAVal.split("-")[0]; // top or bottom
				String horzA = AttAVal.split("-")[1]; // left or right
				String vertB = AttBVal.split("-")[0]; // top or bottom
				String horzB = AttBVal.split("-")[1]; // left or right
				String diffString = "";
				if(vertA.equalsIgnoreCase(vertB))
				{
					diffString += "same";
				}
				else
				{
					diffString += "diff";
				}
				
				diffString += "-";
				
				if(horzA.equalsIgnoreCase(horzB))
				{
					diffString += "same";
				}
				else
				{
					diffString += "diff";
				}
				ValChange = diffString;
				break;
		
			case "size":
				// first we try to quantify how much the size increased with this array of increasing size
				List<String> SizingArr = Arrays.asList("very small", "small", "medium", "large", "very large", "huge");
				if( SizingArr.contains(AttAVal) && SizingArr.contains(AttBVal))
				{
					int sizeDiff = Math.abs(SizingArr.indexOf(AttAVal) - SizingArr.indexOf(AttBVal));
					ValChange = Integer.toString(sizeDiff);	
				}
				// if we can't, then we just do a "this-to-that" change
				else
				{
					ValChange = AttAVal + "-" + AttBVal;
				}
				break;
				
			case "inside":
			case "above":
			case "overlaps":
			case "left-of":
				ValChange = "relational";
				break;
				
			default:
				if(AttAVal.equals(AttBVal))
				{
					ValChange = "same";
				}
				else
				{
					ValChange = AttAVal + "-" + AttBVal;
				}
				break;
		}
	}
	
}
