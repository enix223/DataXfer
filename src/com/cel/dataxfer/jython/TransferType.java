package com.cel.dataxfer.jython;

public interface TransferType {

	public int loopTables();
    
	public void transfer();
    
	public int begin();
    
	public String[] getMsg();
	
	public void setBatchMode(boolean flag);
    
}
