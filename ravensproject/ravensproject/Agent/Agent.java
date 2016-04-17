package ravensproject.Agent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.awt.Color;
// Uncomment these lines to access image processing.
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ravensproject.RavensFigure;
import ravensproject.RavensProblem;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    
    // TODO: need to sort visual answers to get #12
    // make verbal catch better 
    

 // for each problem, we create a horizontal and vertical mapping function.
 // the answer that fits both mappings closest is the answer.
 // FigureMapping defines the relationship between two figures.
 // the contents try to 
    
    public int Solve(RavensProblem problem) 
    {			
    	System.out.println("prob: " + problem.getName());
		List<Integer> visualAnswers = new ArrayList<Integer>();
		List<Integer> verbalAnswers = new ArrayList<Integer>();
		int ans = 4; // guess #4 if not changed
    	try
    	{
    		// Run the agent for each respective data type (visual vs verbal)
    		// We get a List<int> sorted by best answer at the front
	    	if(problem.hasVisual())
	    		visualAnswers = SolveVisual(problem);
	    	if(problem.hasVerbal())
	    		verbalAnswers = SolveVerbal(problem);
	
	    	System.out.println("VisualAns=" + visualAnswers.toString());
	    	System.out.println("VerbalAns=" + verbalAnswers.toString());
	    	
	    	ans = FindAnswer(problem, visualAnswers, verbalAnswers, true);
	    	
			// Check answer and show debug info
			int CorrectAns = problem.checkAnswer(ans);
			String ProblemResult = "	*** Incorrect: ";
			if(ans == CorrectAns)
				ProblemResult = "Correct: ";
			System.out.println("     " + ProblemResult + "Guess = " + ans + "; Answer = " + CorrectAns);
	    	
	        return ans;
    	}
    	catch(Exception e)
    	{
    		// if we error, guess #4 as the answer. High risk = High return >=]
    		return ans;
    	}
    }
    
    // first attempt to draw the correct answer from each agents' results
    private int FindAnswer(RavensProblem problem, List<Integer> visualAnswers, List<Integer> verbalAnswers, boolean trustVerbal)
    {
    	int ans = 4;
    	// now to pick the best answer between our two sub-agents.
    	// 1. default - some problems don't have both data types, pick the only option
    	// 2. agree - if both agents share the same answer
    	//		A. if they share multiple answers, just pick the first one.
    	// 3. disagree - pick the agent that is most certain
    	// 		A. use the agent that has one answer, over the one that has many possible answers
    	
    	// case 1
    	if(!problem.hasVerbal() && problem.hasVisual())
    	{
    		ans = visualAnswers.get(0);
    	}
    	else if(problem.hasVerbal() && !problem.hasVisual())
    	{
    		ans = verbalAnswers.get(0);
    	}
    	
    	else
    	{
    		// case 2 - find all matches - start with visualAnswers as outer loop
    		// since we trust it more
    		boolean agentsAgree = false;
    		for(int vis : visualAnswers)
    		{
    			for(int verb : verbalAnswers)
    			{
    				if(vis == verb)
    				{
    					ans = vis;
    					agentsAgree = true;
    					break;
    				}
    			}	
    		}
    		
    		// case 3 - 
    		if(!agentsAgree)
    		{
    			// try to pick the most confident one
    	    	if((visualAnswers.size() == 1) && (verbalAnswers.size() > 1))
    	    	{
    	    		ans = visualAnswers.get(0);
    	    	}
    	    	else if((visualAnswers.size() > 1) && (verbalAnswers.size() == 1))
    	    	{
    	    		ans = verbalAnswers.get(0);
    	    	}
    	    	// otherwise pick one of any of them
    	    	else
    	    	{
    	    		if(trustVerbal)
    	    		{
    	    			ans = verbalAnswers.get(0);
    	    		}
    	    	}
    		}
    	}
    	return ans;
    	
    }
    
    // verbals are only 2x2 right now, but can be expanded to 
    public List<Integer> SolveVerbal(RavensProblem problem)
    {
    	int[] HorzMatchArray;
    	int[] VertMatchArray;
    	int Weight1 = 5;
    	int Weight2 = 1;

    	// init to the 3x3 case
    	int NumOptions = 8;
    	String aStr = "E";
    	String bStr = "F";
    	String cStr = "H";
		if(problem.getProblemType().equalsIgnoreCase("2x2"))
		{
			NumOptions = 6;
	    	bStr = "B";
	    	aStr = "A";
	    	cStr = "C";
		}
		
		HorzMatchArray = new int[NumOptions];
		VertMatchArray = new int[NumOptions];

		RavensFigure A = problem.getFigures().get(aStr);
		RavensFigure B = problem.getFigures().get(bStr);
		RavensFigure C = problem.getFigures().get(cStr);
		
		FigureTransform HorzMapping = new FigureTransform(A, B);
		int TargetHorzDiff = HorzMapping.DiffScore;
		FigureTransform VertMapping = new FigureTransform(A, C);
		int TargetVertDiff = VertMapping.DiffScore;
		
		double[] DistanceList = new double[NumOptions];;
		
		// for each possible answer
		for(int i = 0; i < NumOptions; i++)
		{
			RavensFigure fig = problem.getFigures().get(Integer.toString(i+1));

			// HORIZONTAL MAPPING	
			FigureTransform map = new FigureTransform(C, fig);
			// 1. Compare possible answer's diff score against target diff score
			int OptionDiffScore = map.DiffScore;
			HorzMatchArray[i] = Weight1 * Math.abs(TargetHorzDiff - OptionDiffScore) + Weight2 * HorzMapping.CompareToTransform(map); 
		
			// VERTICAL MAPPING
			map = new FigureTransform(B, fig);
			// 1. Compare possible answer's diff score against target diff score
			OptionDiffScore = map.DiffScore;
			VertMatchArray[i] = Weight1 * Math.abs(TargetVertDiff - OptionDiffScore) + Weight2 * VertMapping.CompareToTransform(map);
			
			// add to my distance vector (from the origin)
			DistanceList[i] = Math.sqrt(Math.pow(HorzMatchArray[i], 2) + Math.pow(VertMatchArray[i], 2));
		}
		
		// pick the answer with smallest euclidian distance from origin
		double min = DistanceList[0]; 
		for(int i = 0; i < NumOptions; i++)
		{
			if(DistanceList[i] < min)
			{
				min = DistanceList[i];
			}
		}

		// from the minimum, take the best answers
		double ansTolerance = 0.1f;
		List<Integer> AnswerArray = new ArrayList<Integer>();
		for(int i = 0; i < NumOptions; i++)
		{
			// if it fits within our tolerance
			double diff = Math.abs(DistanceList[i] - min);
			if( (min != 0) && ((diff / min) < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			else if((min == 0) && (DistanceList[i] < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			
		}
		return AnswerArray;

    }

    public enum VisualType
    {
    	NoShift, LeftShift, RightShift, AddA, AddB, AddC
    }
    
    public List<Integer> SolveVisual(RavensProblem problem)
    {
    	List<FigureImage> imgList = new ArrayList<FigureImage>();
    	
    	// create the images for each figure, pixel counts
    	for(String fig : problem.getFigures().keySet())
    	{
    		FigureImage fi = new FigureImage(problem.getFigures().get(fig));      
    		imgList.add(fi);
			//System.out.println(fig + ", " + fi.numPix);
			//System.out.println("-" + fi.numPix);
    	}

//		return SolveVisual2x2(imgList, problem);
    	if(problem.getProblemType().equalsIgnoreCase("2x2"))
    	{
    		return SolveVisual2x2(imgList, problem);
    	}
    	else
    	{
    		return SolveVisual3x3(imgList, problem);
    	}
    	
    }
    
    public List<Integer> SolveVisual3x3(List<FigureImage> imgList, RavensProblem problem)
    {
    	// create horz and vert target maps
    	int NumOptions = 8;
    	if(imgList == null)
    	{
    		return null; 
    	}
    	
		FigureImage A = getFigureImage("A", imgList);
    	FigureImage B = getFigureImage("B", imgList);
    	FigureImage C = getFigureImage("C", imgList);
    	FigureImage D = getFigureImage("D", imgList);
    	FigureImage E = getFigureImage("E", imgList);
    	FigureImage F = getFigureImage("F", imgList);
    	FigureImage G = getFigureImage("G", imgList);
    	FigureImage H = getFigureImage("H", imgList);
    	VisualType problemType = DetermineVisualType(imgList, problem);
    	System.out.println("Visual Type = " +  problemType);

		double[] DistanceList = new double[NumOptions];;
		
		for(int i = 0; i < NumOptions; i++)
		{
			RavensFigure fig = problem.getFigures().get(Integer.toString(i+1));
			FigureImage ansFigure = new FigureImage(fig);
			
			// this method maximizes more relationships ...
			switch(problemType)
			{
			case NoShift:
				DistanceList[i] = findSquareDiff(B, C, H, ansFigure) + // right
				findSquareDiff(E, F, H, ansFigure) + // bottom 
				findSquareDiff(A, C, G, ansFigure) + // outsides
				findSquareDiff(D, F, G, ansFigure); // left
				break;
			case LeftShift:
				DistanceList[i] = findSquareDiff(F, D, H, ansFigure) + // 
					findSquareDiff(A, B, H, ansFigure) + // 
					findSquareDiff(A, F, D, ansFigure); // 
				break;
			case RightShift:
				DistanceList[i] = findSquareDiff(D, E, H, ansFigure) + // Left
						findSquareDiff(A, B, ansFigure, G) + // top
						findSquareDiff(B, F, E, ansFigure) + // top right
						findSquareDiff(E, F, ansFigure, G); // right
				break;
			case AddA:
				DistanceList[i] = Math.abs(G.numPix + H.numPix - ansFigure.numPix) + 
						Math.abs(C.numPix + F.numPix - ansFigure.numPix);
				break;
			case AddB:
				DistanceList[i] = Math.abs(-G.numPix + H.numPix + ansFigure.numPix) + 
						Math.abs(-C.numPix + F.numPix + ansFigure.numPix);
				break;
			case AddC:
				DistanceList[i] = Math.abs(G.numPix - H.numPix + ansFigure.numPix) + 
						Math.abs(C.numPix - F.numPix + ansFigure.numPix);
				break;
			
			default:
				DistanceList[i] = findSquareDiff(B, C, H, ansFigure) + // right
				findSquareDiff(E, F, H, ansFigure) + // bottom 
				findSquareDiff(A, C, G, ansFigure) + // outsides
				findSquareDiff(D, F, G, ansFigure); // left
				break;
			
			}
		}
		
		// pick the answer with smallest euclidian distance from origin
		double min = Double.MAX_VALUE; 
		for(int i = 0; i < NumOptions; i++)
		{
			if(DistanceList[i] < min)
			{
				min = DistanceList[i];
			}
		}
		
		double ansTolerance = 0.1f;
		List<Integer> AnswerArray = new ArrayList<Integer>();
		for(int i = 0; i < NumOptions; i++)
		{
			// if it fits within our tolerance, 
			double diff = Math.abs(DistanceList[i] - min);
			if( (min != 0) && ((diff / min) < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			else if((min == 0) && (DistanceList[i] < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			
		}
		return AnswerArray;
		
    	
    }
    
    // this tries to classify the visual patterns in the RPM problem
    // we try all options and determine if applying pattern recognition produces a better answer
    public VisualType DetermineVisualType(List<FigureImage> imgList, RavensProblem problem)
    {
		FigureImage A = getFigureImage("A", imgList);
    	FigureImage B = getFigureImage("B", imgList);
    	FigureImage C = getFigureImage("C", imgList);
    	FigureImage D = getFigureImage("D", imgList);
    	FigureImage E = getFigureImage("E", imgList);
    	FigureImage F = getFigureImage("F", imgList);
    	FigureImage G = getFigureImage("G", imgList);
    	FigureImage H = getFigureImage("H", imgList);
    	    	
    	int noShiftDiff = findSquareDiff(A, B, D, E) + // top 
				findSquareDiff(D, E, G, H) + // left
				findSquareDiff(B, C, E, F); // right    
    	noShiftDiff = noShiftDiff / 3;
    	int leftShiftDiff = findSquareDiff(A, B, F, D) + // top 
				findSquareDiff(E, F, G, H) + // left
				findSquareDiff(B, C, D, E); // right
    	leftShiftDiff = leftShiftDiff / 3;
    	int rightShiftDiff = findSquareDiff(A, B, E, F) + // top 
				findSquareDiff(F, D, G, H) + // left
				findSquareDiff(B, C, F, D); // right
    	rightShiftDiff = rightShiftDiff / 3;
    	
    	// SECOND - if no shift, then check for additive properties
    	// A. 1+2 = 3	
    	int aDiff = Math.abs(A.numPix + B.numPix - C.numPix) + Math.abs(D.numPix + E.numPix - F.numPix) + // horz diff
    			Math.abs(A.numPix + D.numPix - G.numPix) + Math.abs(B.numPix + E.numPix - H.numPix); // vvert diff
    	aDiff = aDiff / 4;
    	// B. 2+3 = 1	
    	int bDiff = Math.abs(-A.numPix + B.numPix + C.numPix) + Math.abs(-D.numPix + E.numPix + F.numPix) + // horz diff
    			Math.abs(-A.numPix + D.numPix + G.numPix) + Math.abs(-B.numPix + E.numPix + H.numPix); // vvert diff
    	bDiff = bDiff / 4;
    	// C. 1+3 = 2
    	int cDiff = Math.abs(A.numPix - B.numPix + C.numPix) + Math.abs(D.numPix - E.numPix + F.numPix) + // horz diff
    			Math.abs(A.numPix - D.numPix + G.numPix) + Math.abs(B.numPix - E.numPix + H.numPix); // vvert diff
    	cDiff = cDiff / 4;
    	
//    	System.out.println("No=" +  noShiftDiff + ", " +
//    			"Left=" +  leftShiftDiff + ", " + 
//    			"Right=" +  rightShiftDiff + ", " + 
//    			"aDiff=" +  aDiff + ", " + 
//    			"bDiff=" +  bDiff + ", " + 
//    			"cDiff=" +  cDiff 
//    			);

    	// IMPORTANT - Add these in the same order as the enumerations
    	List<Integer> typeDiffList = new ArrayList<Integer>();
    	typeDiffList.add(noShiftDiff);
    	typeDiffList.add(leftShiftDiff);
    	typeDiffList.add(rightShiftDiff);
    	typeDiffList.add(aDiff);
    	typeDiffList.add(bDiff);
    	typeDiffList.add(cDiff);
    	int min = Integer.MAX_VALUE;
    	int minIndex = 0;
    	for(int x = 0; x < typeDiffList.size(); x++)
    	{
    		if(typeDiffList.get(x) < min)
    		{
    			min = typeDiffList.get(x);
    			minIndex = x;
    		}
    	}
    	return VisualType.values()[minIndex];
    	
    }
    
   public int findSquareDiff(FigureImage a, FigureImage b, FigureImage c, FigureImage d)
   {
	   	if(a == null || b == null || c == null || d == null)
	   		return Integer.MAX_VALUE;
	   	ImageTransform LhorzTransform1 = new ImageTransform(a, b);
	   	Map2x2 LhorzMap1 = new Map2x2(LhorzTransform1);
	   	ImageTransform LhorzTransform2 = new ImageTransform(c, d);
	   	Map2x2 LhorzMap2 = new Map2x2(LhorzTransform2);
	   	ImageTransform LvertTransform1 = new ImageTransform(a, c);
	   	Map2x2 LvertMap1 = new Map2x2(LvertTransform1);
	   	ImageTransform LvertTransform2 = new ImageTransform(b, d);
	   	Map2x2 LvertMap2 = new Map2x2(LvertTransform2);
	   	return  LhorzMap1.compareToMap(LhorzMap2) + LvertMap1.compareToMap(LvertMap2); 
   }
    
    public List<Integer> SolveVisual2x2(List<FigureImage> imgList, RavensProblem problem)
    {
    	// create horz and vert target maps
    	int NumOptions = 6;
    	if(imgList == null)
    	{
    		return null; 
    	}
    	
    	FigureImage A = getFigureImage("A", imgList);;
    	FigureImage B = getFigureImage("B", imgList);;
    	FigureImage C = getFigureImage("C", imgList);;
    	
    	ImageTransform horzTransform = new ImageTransform(A, B);
    	Map2x2 horzMap = new Map2x2(horzTransform);
    	
    	ImageTransform vertTransform = new ImageTransform(A, C);
    	Map2x2 vertMap = new Map2x2(vertTransform);
    	
    	int[] HorzMatchArray = new int[NumOptions];
		int[] VertMatchArray = new int[NumOptions];		
		double[] DistanceList = new double[NumOptions];;
		
		for(int i = 0; i < NumOptions; i++)
		{
			RavensFigure fig = problem.getFigures().get(Integer.toString(i+1));
			FigureImage ansFigure = new FigureImage(fig);

			// HORIZONTAL MAPPING
	    	ImageTransform horzAnsTransform = new ImageTransform(C, ansFigure);
	    	Map2x2 horzAnsMap = new Map2x2(horzAnsTransform);
			HorzMatchArray[i] = horzMap.compareToMap(horzAnsMap); 
		
			// VERTICAL MAPPING
	    	ImageTransform vertAnsTransform = new ImageTransform(B, ansFigure);
	    	Map2x2 vertAnsMap = new Map2x2(vertAnsTransform);
	    	VertMatchArray[i] = vertMap.compareToMap(vertAnsMap); 
			
	    	// add to my distance vector (from the origin)
	    	DistanceList[i] = Math.sqrt(Math.pow(HorzMatchArray[i], 2) + Math.pow(VertMatchArray[i], 2));
		}
		
		// pick the answer with smallest euclidian distance from origin
		double min = Double.MAX_VALUE; 
		for(int i = 0; i < NumOptions; i++)
		{
			if(DistanceList[i] < min)
			{
				min = DistanceList[i];
			}
		}
		
		double ansTolerance = 0.1f;
		List<Integer> AnswerArray = new ArrayList<Integer>();
		for(int i = 0; i < NumOptions; i++)
		{
			// if it fits within our tolerance, 
			double diff = Math.abs(DistanceList[i] - min);
			if( (min != 0) && ((diff / min) < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			else if((min == 0) && (DistanceList[i] < ansTolerance) )
			{
				AnswerArray.add(i+1);
			}
			
		}
		return AnswerArray;
    }
    
    public FigureImage getFigureImage(String name, List<FigureImage> imgList)
    {
    	for(FigureImage fi : imgList)
    	{
    		if(fi.figName.equalsIgnoreCase(name))
    		{
    			return fi;
    		}
    	}
    	return null; // if not found
    }
}

