package ravensproject.Agent;

public class ObjectTransformPair implements RavensTransform
{
	int DiffScore;
	ObjectTransform otA;
	ObjectTransform otB;
	
	public ObjectTransformPair(ObjectTransform a, ObjectTransform b)
	{
		otA = a;
		otB = b;
		DiffScore = 0;
		GetDiffScore();
	}

	public void GetDiffScore()
	{
		int diffs = 0;
		
		// first compare any att name matches if values differ
		for(AttributeTransform at1 : otA.AttTransformList)
		{
			for(AttributeTransform at2 : otB.AttTransformList)
			{
				if(at1.AttName.equalsIgnoreCase(at2.AttName) && !at1.ValChange.equalsIgnoreCase(at2.ValChange))
				{
					diffs++;
				}
			}
		}
		
		// then check if they have atts that the other doesn't
		for(AttributeTransform at1 : otA.AttTransformList)
		{
			boolean attFound = false;
			for(AttributeTransform at2 : otB.AttTransformList)
			{
				if(at1.AttName.equalsIgnoreCase(at2.AttName))
				{
					attFound = true;
					break;
				}
			}
			
			if(!attFound)
			{
				diffs++;
			}
		}

		for(AttributeTransform at2 : otB.AttTransformList)
		{
			boolean attFound = false;
			for(AttributeTransform at1 : otA.AttTransformList)
			{
				if(at2.AttName.equalsIgnoreCase(at1.AttName))
				{
					attFound = true;
					break;
				}
			}
			
			if(!attFound)
			{
				diffs++;
			}
		}
		DiffScore = diffs;
	}

}