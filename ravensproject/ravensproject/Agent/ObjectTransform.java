package ravensproject.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ravensproject.RavensObject;

public class ObjectTransform implements RavensTransform{
	public int diffScore;
	public String Name;
	RavensObject objA;
	RavensObject objB;
	List<AttributeTransform> AttTransformList;
	List<String> relationalAtts = Arrays.asList("inside", "above", "overlaps", "left-of");
	
	public ObjectTransform(RavensObject obja, RavensObject objb)
	{
		objA = obja;
		objB = objb;
		diffScore = 0;
		AttTransformList = new ArrayList<AttributeTransform>();
		
		String aName = "";
		String bName = "";
		if( objA != null)
			aName = objA.getName();
		if( objB != null)
			bName = objB.getName();
		Name = aName + ":" + bName;
		GetDiffScore();
		//System.out.println(obja. + "+" + objb);
		GenerateAttMap();
	}
	
	public void GetDiffScore()
	{
		int diff = 0;
		if(objA == null || objB == null)
		{
			diffScore = 1;
			return;
		}
		
		
		HashMap<String, String> TotalAttList = new HashMap<String, String>();
		TotalAttList.putAll(objA.getAttributes());
		TotalAttList.putAll(objB.getAttributes());
		
		for(String attributeName : TotalAttList.keySet()) 
		{
			//grab from both
			String attAVal = objA.getAttributes().get(attributeName);
			String attBVal = objB.getAttributes().get(attributeName);
			
			if((attAVal == null) || (attBVal == null) || attAVal.isEmpty() || attBVal.isEmpty())
			{
				diff++;
			}
			else if(!attAVal.equalsIgnoreCase(attBVal) && !relationalAtts.contains(attributeName))
			{
				diff++;
			}
				
		}
		diffScore = diff;
	}
	
	public void GenerateAttMap()
	{
		HashMap<String, String> TotalAttList = new HashMap<String, String>();
		if(objA != null)
			TotalAttList.putAll(objA.getAttributes());
		if(objB != null)
			TotalAttList.putAll(objB.getAttributes());
		
		for(String attributeName : TotalAttList.keySet()) 
		{
			//grab from both - string is null if doesn't exist
			
			String attAVal = "";
			String attBVal = "";
			if(objA != null)
			{
				attAVal = objA.getAttributes().get(attributeName); 
				//System.out.print(objA.getName());
			}
			//System.out.print("+");

			if(objB != null)
			{
				attBVal = objB.getAttributes().get(attributeName);
				//System.out.println(objB.getName());
			}
			AttTransformList.add(new AttributeTransform(attributeName, attAVal, attBVal));
		}
	}
}
