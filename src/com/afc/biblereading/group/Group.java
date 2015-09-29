package com.afc.biblereading.group;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {
	private String ID;
	private String name = null;
	private ArrayList<Integer> membersId = null;
	private HashMap<Integer, Member> members = null;
	private int finishRate = 0;
	
	public Group(String ID, String name, ArrayList<Integer> membersId) {
		super();
		this.ID = ID;
		this.name = name;
		this.membersId = membersId;
		this.members = new HashMap<Integer, Member>();
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
	
	public void setMembersID(ArrayList<Integer> membersId) {
		this.membersId = new ArrayList<Integer>();
		this.membersId.addAll(membersId);
	}	
	public ArrayList<Integer> getMembersId() {
		return this.membersId;
	}	
	public int getGroupSize() {
		if (this.membersId == null){
			return 0;
		}
		return this.membersId.size();
	}
	
	public void setFinishRate(int finishRate) {
		this.finishRate = finishRate;
	}	
	public int getFinishRate() {
		return this.finishRate;
	}
	
	public void addMember(Member member){
		this.members.put(member.getId(), member);
		if (members.size() == membersId.size()){
			notify();
		}
	}
	public ArrayList<Member> getMembers(){
		return (ArrayList<Member>) this.members.values();
	}
	public Member getMembersByID(int id){
		return this.members.get(id);
	}
}
