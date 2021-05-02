package com.example.farmia_mcc;

public class Userdata {

    String name,emailid,mobileno,_password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public Userdata(String name, String emailid, String mobileno, String _password) {
        this.name = name;
        this.emailid = emailid;
        this.mobileno = mobileno;
        this._password = _password;
    }

    public Userdata() {
    }
}
