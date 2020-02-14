package com.example.topquiz.model;

public class User {

    private String mFirstName; // prenom

    public String getFirstName() {  // recuperer le prenom
        return mFirstName;
    }

    public void setFirstName(String firstName) {  // mettre a jour le prenom
        mFirstName = firstName;
    }

    @Override
    public String toString() {   // methode  afficher les differentes valeurs
        return "User{" +
                "mFirstName='" + mFirstName + '\'' +
                '}';
    }
}
