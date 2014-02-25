package com.cel.dataxfer.jython;

public class Transfer {
	
	private static TransferType transfer;

	public static int transfer(String pkg){
		TransferFactory factory = new TransferFactory();
		transfer =  factory.create(pkg);
		return transfer.begin();		
	}
	
	public static String[] getMsg(){
		return transfer.getMsg();
	}
	
	public static int batch(){
		TransferFactory factory = new TransferFactory();
		transfer = factory.create("");
		transfer.setBatchMode(true);
		return transfer.begin();
	}
}
