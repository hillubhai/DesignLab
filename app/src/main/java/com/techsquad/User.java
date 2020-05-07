package com.techsquad;

class User {

    private String fName;
    private String lName;
    private String email;
    private String phone;
    private int id;

    User(String fName, String lName, String email, String phone, int id) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getlName() {
        return lName;
    }

    void setlName(String lName) {
        this.lName = lName;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    String getfName() {
        return fName;
    }

    void setfName(String fName) {
        this.fName = fName;
    }
}