package com.dgut.common.util;

import java.util.Calendar;
import java.util.Random;

public class RandomGeneration {
	static Random rand=new Random(); 
	
	public static int NextInt(final int min, final int max)
	{
		
	    int tmp = Math.abs(rand.nextInt());
	    return tmp % (max - min + 1) + min;
	}
	
	public static String generateID(){
		Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		String month=String.format("%2d", c.get(Calendar.MONTH)+1).replace(" ", "0");
		String day=String.format("%2d", c.get(Calendar.DAY_OF_MONTH)+1).replace(" ", "0");
		String minute=String.format("%2d", c.get(Calendar.MINUTE)).replace(" ", "0");
		return year+month+day+minute+RandomGeneration.NextInt(10000,99999);
	}

}
