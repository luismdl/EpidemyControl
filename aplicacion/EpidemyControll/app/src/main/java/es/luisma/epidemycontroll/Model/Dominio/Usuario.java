package es.luisma.epidemycontroll.Model.Dominio;

import java.util.Date;

public class Usuario {

    private String USERNAME;
    private String EMAIL;
    private String PASSWORD;
    private Date BIRTHDATE;
    private int ADMIN;
    private int ID;

    public Usuario(String USERNAME, String EMAIL, String PASSWORD, Date BIRTHDATE, int ADMIN, int ID) {
        this.USERNAME = USERNAME;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.BIRTHDATE = BIRTHDATE;
        this.ADMIN = ADMIN;
        this.ID = ID;
    }

    public Usuario(String USERNAME, String EMAIL, String PASSWORD, Date BIRTHDATE) {
        this.USERNAME = USERNAME;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.BIRTHDATE = BIRTHDATE;
    }

    public String getUserName() {
        return USERNAME;
    }

    public void setUserName(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEmail() {
        return EMAIL;
    }

    public void setEmail(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public void setPassword(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public Date getBirthDate() {
        return BIRTHDATE;
    }

    public void setBirthDate(Date BIRTHDATE) {
        this.BIRTHDATE = BIRTHDATE;
    }

    public int isAdmin() {
        return ADMIN;
    }

    public void setAdmin(int ADMIN) {
        this.ADMIN = ADMIN;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
