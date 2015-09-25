package com.afc.biblereading.helper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.afc.biblereading.group.Group;
import com.afc.biblereading.group.Member;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;

import static com.afc.biblereading.user.definitions.Consts.EMPTY_STRING;

public class DataHolder {
    private static DataHolder dataHolder;
    private List<QBUser> qbUsersList = new ArrayList<QBUser>();
    private List<QBCustomObject> qbGroupList = new ArrayList<QBCustomObject>();
    private List<Group> groupList = new ArrayList<Group>();
    private ArrayList<String> userLogList = new ArrayList<String>();
    private ArrayList<Member> Member = new ArrayList<Member>();
    private QBCustomObject signInUserQBGroup;
    private QBUser signInQbUser;
    private Group signInUserGroup;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }
    public void setUserLogList(ArrayList<String> userLogList){
    	this.userLogList = userLogList;
    }
    public ArrayList<String> getUserLogList(){
    	return this.userLogList;
    }
    public void setQBGroupList(List<QBCustomObject> qbGroupList){
    	Log.v("data Holder", "set group list");
    	this.qbGroupList = qbGroupList;
    }
    public QBCustomObject getQBGroup(int index){
    	return this.qbGroupList.get(index);
    }
    public void setGroupList(List<Group> GroupList) {
        this.groupList = GroupList;
    }
    public int getGroupListSize() {
        return groupList.size();
    }
	public Group getGroup(int index) {
		return groupList.get(index);
	}
	public String getGroupName(int index) {
        return groupList.get(index).getName();
	}
	public int getGroupRate(int index) {
		return groupList.get(index).getFinishRate();
	}
	
    public void setQbUsersList(List<QBUser> qbUsersList) {
        this.qbUsersList = qbUsersList;
    }

    public int getQBUserListSize() {
        return qbUsersList.size();
    }

    public String getQBUserName(int index) {
        return qbUsersList.get(index).getFullName();
    }

    public List<String> getQbUserTags(int index) {
        return qbUsersList.get(index).getTags();
    }

    public QBUser getQBUser(int index) {
        return qbUsersList.get(index);
    }

    public QBUser getLastQBUser() {
        return qbUsersList.get(qbUsersList.size() - 1);
    }

    public void addQbUserToList(QBUser qbUser) {
        qbUsersList.add(qbUser);
    }

    public QBUser getSignInQbUser() {
        return signInQbUser;
    }

    public void setSignInQbUser(QBUser singInQbUser) {
        this.signInQbUser = singInQbUser;
    }   
    
    public Group getSignInUserGroup() {
        return signInUserGroup;
    } 
    
    public void setSignInUserGroup(Group group) {
    	Log.v("data holder", "set user group");
        this.signInUserGroup = group;
    }
    

    public QBCustomObject getSignInUserQbGroup() {
		return signInUserQBGroup;
    } 
    
    public void setSignInUserQbGroup(QBCustomObject group) {
    	Log.v("data holder", "set user group");
        this.signInUserQBGroup = group;
    }

    public String getSignInUserOldPassword() {
        return signInQbUser.getOldPassword();
    }

    public int getSignInUserId() {
    	return signInQbUser.getId();
    }

    public void setSignInUserPassword(String singInUserPassword) {
        signInQbUser.setOldPassword(singInUserPassword);
    }

    public String getSignInUserLogin() {
        return signInQbUser.getLogin();
    }

    public String getSignInUserEmail() {
        return signInQbUser.getEmail();
    }

    public String getSignInUserFullName() {
        return signInQbUser.getFullName();
    }

    public String getSignInUserPhone() {
        return signInQbUser.getPhone();
    }

    public String getSignInUserWebSite() {
        return signInQbUser.getWebsite();
    }

    public String getSignInUserTags() {
        if (signInQbUser.getTags() != null) {
            return signInQbUser.getTags().getItemsAsString();
        } else {
            return EMPTY_STRING;
        }
    }
}
