package com.example.expensetrackerai;

public class Users {

    String name,mail,pass,userid;

    public Users(){}

    public Users(String userid, String name, String mail, String pass){
        this.userid = userid;
        this.name = name;
        this.mail = mail;
        this.pass = pass;
    }



    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
