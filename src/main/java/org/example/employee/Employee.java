package org.example.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Employee {
    private int eid;
    private String fname;
    private String lname;
    private String gender;
    private String contact;
    private String designation;
    private String image;
    private String doj;

    private String mail;

    private int salary;

    // Constructor
    public Employee(int eid, String fname, String lname, String gender, String contact,String mail, String designation, String image, String doj,int salary) {
        this.eid = eid;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.contact = contact;
        this.mail=mail;
        this.designation = designation;
        this.image = image;
        this.doj = doj;
        this.salary=salary;
    }

    // Getters and Setters
    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }



    @Override
    public String toString() {
        return
                "eid=" + eid +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", gender='" + gender + '\'' +
                ", contact='" + contact + '\'' +
                        ", mail='" + mail + '\'' +
                ", designation='" + designation + '\'' +
                ", image='" + image + '\'' +
                ", doj='" + doj + '\'' + ", salary=' "+salary+
                '}';
    }




}
