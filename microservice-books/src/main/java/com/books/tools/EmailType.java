package com.books.tools;

public class EmailType {

     String email;
     String titre;
     String dateDeFinDuPret;
     String username;

    public EmailType() { super();
    }

    public EmailType(String email, String titre, String dateDeFinDuPret,String username) {
        this.email = email;
        this.titre = titre;
        this.dateDeFinDuPret = dateDeFinDuPret;
        this.username=username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDateDeFinDuPret() {
        return dateDeFinDuPret;
    }

    public void setDateDeFinDuPret(String dateDeFinDuPret) {
        this.dateDeFinDuPret = dateDeFinDuPret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
