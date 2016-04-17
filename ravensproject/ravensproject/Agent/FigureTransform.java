package ravensproject.Agent;

import java.util.ArrayList;
import java.util.List;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

public class FigureTransform implements RavensTransform{

	    List<ObjectTransform> ObjTransformList; 
	    RavensFigure figA;
	    RavensFigure figB;
	    int numObjDiff = 0;
	    int DiffScore = 0;

	    //List<String> MapList;
	    //public HashMap<String,ArrayList<String>> ObjectAttributeMap;
		
		public FigureTransform(RavensFigure a, RavensFigure b)
		{
			figA = a;
			figB = b;
			ObjTransformList = new ArrayList<ObjectTransform>();
			
			numObjDiff = a.getObjects().size() - b.getObjects().size();

			GenerateMapping();
			GetDiffScore(); // and this creates the mapping
		}
		
		// Do object-object correspondence
		public void GenerateMapping()
		{
			
			// list all possible object-object pairs
			List<ObjectTransform> TotalTransformList = new ArrayList<ObjectTransform>();
			List<ObjectTransform> BestTransformList = new ArrayList<ObjectTransform>();
			
			for(String aKey : figA.getObjects().keySet())
			{
				RavensObject objA = figA.getObjects().get(aKey);
				for(String bKey : figB.getObjects().keySet())
				{
					RavensObject objB = figB.getObjects().get(bKey);
					TotalTransformList.add(new ObjectTransform(objA, objB));
				}
			}
			
			// now find the best match ups
			int MaxPairs = Math.min(figA.getObjects().size(), figB.getObjects().size());	
			for(int i = 0; i < MaxPairs; i++)
			{
				// pick the lowest diff transform each time
				int min = Integer.MAX_VALUE;
				
				ObjectTransform MinDiffObj = TotalTransformList.get(0); // init
				for(int t = 0; t < TotalTransformList.size(); t++)
				{
					int diff = TotalTransformList.get(t).diffScore;
					if(diff < min)
					{
						min = diff;
						MinDiffObj = TotalTransformList.get(t);
					}
				}
				
				// add to best list
				BestTransformList.add(MinDiffObj);
				
				// now reduce the transform list to what is still valid
				List<ObjectTransform> removeList = new ArrayList<ObjectTransform>();
				for(ObjectTransform ot : TotalTransformList)
				{
					// if any transforms have the same A or B obj, remove it
					if(ot.objA.getName().equalsIgnoreCase(MinDiffObj.objA.getName()) || ot.objB.getName().equalsIgnoreCase(MinDiffObj.objB.getName()) )
					{
						removeList.add(ot);
					}
				}
				for(ObjectTransform ot : removeList)
				{
					TotalTransformList.remove(ot); // can improve this run time
				}	
			}
			
			// if we have max # of pairs, then we are done, otherwise find leftovers
			if(BestTransformList.size() != MaxPairs)
			{
				BestTransformList = AddLeftOvers(BestTransformList, figA);
				BestTransformList = AddLeftOvers(BestTransformList, figB);
			}

			// now that we have the right set of object pairings, store away the list.
			ObjTransformList = BestTransformList; // replaces ObjectMap
		}
		
		public List<ObjectTransform> AddLeftOvers(List<ObjectTransform> BestTransformList, RavensFigure fig)
		{
			for(String obj : fig.getObjects().keySet())
			{
				boolean objFound = false;
				for(int i = 0; i < BestTransformList.size(); i++)
				{
					if(BestTransformList.get(i).objA != null)
					{
						if(obj.equalsIgnoreCase(BestTransformList.get(i).objA.getName()))
						{
							objFound = true;
							break;
						}
					}
				}
				
				if(!objFound)
				{
					// Add to BestPairs list
					BestTransformList.add(new ObjectTransform(fig.getObjects().get(obj), null));			
				}
			}
			return BestTransformList;
			
		}
		
		// calculate my total difference score between two figures
		public void GetDiffScore()
		{
			int diff = 0;
			for(ObjectTransform tran : ObjTransformList)
			{
				diff += tran.diffScore;
			}
			DiffScore = diff;
		}

		// return a difference score based on transform comparison
	    public int CompareToTransform(FigureTransform otherMap)
	    {
	    	// do the same thing as mapping objects - we find correspondence between the two maps!
	    	// we also start by picking the most similar map matches

	    	int MaxPairs = Math.min(ObjTransformList.size(), otherMap.ObjTransformList.size());
	    	List<ObjectTransformPair> BestotpList = new ArrayList<ObjectTransformPair>();
	    	List<ObjectTransformPair> TotalotpList = new ArrayList<ObjectTransformPair>();
	    	
	    	// get total list of OPTs
	    	for(ObjectTransform ot1 : ObjTransformList)
	    	{
	        	for(ObjectTransform ot2 : otherMap.ObjTransformList)
	        	{	
	        		TotalotpList.add(new ObjectTransformPair(ot1, ot2));
	        	}
	    	}

	    	// for each pair we can pick, grab the lowest diff pair until done
	    	for(int i = 0; i < MaxPairs; i++)
	    	{
				int minDiff = Integer.MAX_VALUE;
	    		ObjectTransformPair bestMatch = TotalotpList.get(0);
	    		for(ObjectTransformPair otp : TotalotpList)
	    		{
	        		if(otp.DiffScore < minDiff)
	        		{
	        			bestMatch = otp;
	        			minDiff = otp.DiffScore;
	        		}
	    		}
	    		
	    		// pick best match
	    		BestotpList.add(bestMatch);

		    	// reduce the total options
	        	List<ObjectTransformPair> RemoveotpList = new ArrayList<ObjectTransformPair>();
	    		for(ObjectTransformPair otp : TotalotpList)
	    		{
	    			// if our bestMatch inbvalidates the otp...
	    			if(bestMatch.otA.Name.equals(otp.otA.Name) || bestMatch.otB.Name.equals(otp.otB.Name))
	    			{
	    				RemoveotpList.add(otp);
	    			}
	    		}
	    		
	    		//remove 
	    		for(ObjectTransformPair removeOTP : RemoveotpList)
	    		{
	    			TotalotpList.remove(removeOTP);
	    		}
	    	}

			
			// then i'll add the difference in # of elements to see if transforms are missing
			int diff = 0;
			for(ObjectTransformPair pair : BestotpList)
			{
				diff += pair.DiffScore;
			}
			diff +=  Math.abs(ObjTransformList.size() - otherMap.ObjTransformList.size());
	  
			// then i'll add the difference by how many objects they differ by.
			diff += Math.abs(numObjDiff - otherMap.numObjDiff);
			
	    	//System.out.println("transform diff = " + diff);
	    	return diff;
	    }

	}
