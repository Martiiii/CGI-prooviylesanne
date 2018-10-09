package com.cgi.dentistapp.misc;

/**
 * Class for sample dentists
 */
public class Dentist {

    private String firstname;

    private String lastname;

    public Dentist() {
    }

    public Dentist(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    private String getFirstname() {
        return firstname;
    }

    private String getLastname() {
        return lastname;
    }

    public String getFullname() {
        return this.getFirstname() + " " + this.getLastname();
    }
}
