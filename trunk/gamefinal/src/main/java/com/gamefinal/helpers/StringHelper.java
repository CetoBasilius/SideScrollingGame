package com.gamefinal.helpers;

public class StringHelper {

	public static int countOccurrences(String string, char charToFind)
	{
		int count = 0;
		for (int i=0; i < string.length(); i++)
		{
			if (string.charAt(i) == charToFind)
			{
				count++;
			}
		}
		return count;
	}
	
	public static String reverseString(String inString) {
		String resultString = "";
		if(inString.length()>0) {
			for(int stringCharIndex=inString.length()-1;stringCharIndex>=0;stringCharIndex--) {
				resultString+=inString.charAt(stringCharIndex);
			}
		}
		return resultString;
	}

}
