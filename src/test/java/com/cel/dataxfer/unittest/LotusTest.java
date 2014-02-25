package com.cel.dataxfer.unittest;

import com.cel.dataxfer.core.LotusKit;


public class LotusTest {
	
	private static LotusKit kit = new LotusKit();
	
	public static void beforeClass(){
		kit.SendMail("Hello world", "This is a test mail");		
		System.out.println("");		
	}
}
