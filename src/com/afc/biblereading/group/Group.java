package com.afc.biblereading.group;

import java.util.ArrayList;

public class Group {
	private String ID;
	private String name = null;
	private ArrayList<Integer> members = null;
	private int finishRate = 0;
	
	public Group(String ID, String name, ArrayList<Integer> members) {
		super();
		this.ID = ID;
		this.name = name;
		this.members = members;
	}
	
	public void setGroupID(String groupID) {
		this.ID = groupID;
	}	
	public String getGroupID() {
		return this.ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	public String getName() {
		return this.name;
	}
	
	public void setMembersList(ArrayList<Integer> members) {
		this.members = members;
	}	
	public ArrayList<Integer> getMembersList() {
		return this.members;
	}
	
	public void setFinishRate(int finishRate) {
		this.finishRate = finishRate;
	}	
	public int getFinishRate() {
		return this.finishRate;
	}
}
