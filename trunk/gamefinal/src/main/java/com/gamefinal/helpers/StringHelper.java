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

}
