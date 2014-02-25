package com.cel.dataxfer.core;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;


public class LotusKit {
	
	private String host = "h01sdhkdb01";
	private String sender = "enix.yu.guanyuan@simedarby.com.hk";
	private Vector<String> receivers; 
	private String database = "C:\\Program Files (x86)\\IBM\\Lotus\\Notes\\Data\\names.nsf";
	
	public LotusKit(){
		receivers = new Vector<String>();
	}
	
	
	/**
	 * Send email with giving subject & content.
	 * @param subject
	 * @param content
	 * @return
	 */
	public boolean SendMail(String subject, String content){
		
		try {
			Session session = NotesFactory.createSessionWithFullAccess();
			Database nsf = session.getDatabase("", database);			
						
			//setup new email
			Document document = nsf.createDocument();
			document.setSaveMessageOnSend(true);
			document.appendItemValue("Body", content);
			document.appendItemValue("Subject", subject);
			document.appendItemValue("SentTo", receivers);
			document.send();
			
			return true;
		} catch (NotesException e) {
			System.out.println("Send email failed.");
			e.printStackTrace();
			return false;
		}				
	}

	
	/*
	 * Properties methods
	 */
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	public Vector<String> getReceiver(){
		return this.receivers;
	}
	
	public void addReceiver(String recv){
		receivers.add(recv);
	}
}
