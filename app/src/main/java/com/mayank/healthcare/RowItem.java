package com.mayank.healthcare;

/**
 * Created by Mayank on 14-02-2017.
 */
public class RowItem {

    private String member_name;
    private int profile_pic_id;

    public RowItem(String member_name, int profile_pic_id) {

        this.member_name = member_name;
        this.profile_pic_id = profile_pic_id;

    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getProfile_pic_id() {
        return profile_pic_id;
    }

    public void setProfile_pic_id(int profile_pic_id) {
        this.profile_pic_id = profile_pic_id;
    }



}



