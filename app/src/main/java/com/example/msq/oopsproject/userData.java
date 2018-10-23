package com.example.msq.oopsproject;


public class userData {

    public String fName;
    public String lName;
    public long mobileNum;
    public String sex;
    public String e_mail;
    public String bloodGroup;
    public boolean knowCPR;

    public userData() {

    }

    public userData(String fName, String lName, long mobileNum, String sex, String e_mail, String bloodGroup, boolean knowCPR) {
        this.fName = fName;
        this.lName = lName;
        this.mobileNum = mobileNum;
        this.sex = sex;
        this.bloodGroup = bloodGroup;
        this.knowCPR = knowCPR;
        this.e_mail = e_mail;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public long getMobileNum() {
        return mobileNum;
    }

    public String getSex() {
        return sex;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public boolean isKnowCPR() {
        return knowCPR;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setMobileNum(long mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setKnowCPR(boolean knowCPR) {
        this.knowCPR = knowCPR;
    }
}
