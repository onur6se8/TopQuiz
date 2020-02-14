package com.example.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topquiz.R;
import com.example.topquiz.model.User;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

         //  type      nom choisi       pour l'instant null
    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;

    private User mUser; // utilisateur
    // identifiant dactivity
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    // stoker linfo sur android  score et prenom de lutilisateur
    private SharedPreferences mPreferences;


    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";   // score
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME"; // prenom



    @Override   // pour recuperer le score de game activity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // si 42 = 42
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
        {
            // je recupere le score, je mets 0 si rien est envoyer
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            // mettre une valeur int dans mes preferences   score
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            greetUser();// a la fin d'une partie

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {   // methode surcharger   automatiquement lors de la cr"ation du projet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // charge le fichier layout activity main xml

        System.out.println("MainActivity::onCreate()");

        // initialisation
        mUser = new User();
        // score, prenom     methode ( mode dacces )
        mPreferences = getPreferences(MODE_PRIVATE);

        //  on asossie a la variable  ( on force le type )   pour réferencer les élement graphique
        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);  // avec l'ID
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);

        // variable . methode qui dit si le bouton doit etre activer ou non
        mPlayButton.setEnabled(false); // ( il prend un boolean ) le bouton sera griser

        greetUser();// au lancement

        // class methode pour savoir si du text a ete taper
        mNameInput.addTextChangedListener(new TextWatcher() { // ( prend un parametre

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override     // methode appeler a chaque fois que lutilisateur utilise une touche
            public void onTextChanged(CharSequence s, int start, int before, int count) { // charSequence s  contient le texte taper
                // bouton. methode ( si la variable s converti en chaine de caractere sa valeur est different de 0 on active le bouton )
                mPlayButton.setEnabled(s.toString().length() != 0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // bouton play . lorsquon clique sur le bouton  ( parametre)
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override    // methode appeler a chaque fois que lutilisateur clique sur le bouton
            public void onClick(View v) {

                //  variable = nom saisie par l'utilisateur
                String firstname = mNameInput.getText().toString();
                // mUser = User = class User
                mUser.setFirstName(firstname); // on envoie a setFirstname le prenom

                // acces a mes preferences.methode().mettre une valeur string ( valeur , prenom ) appliquer
                mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstName()).apply();
                // vouloir faire quelque chose   variable    instance de intent ( 2 parametr )
                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                // demarrer l'activité   (  oui , identifiant dactivity renvoie 42)
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);

            }
        });
    }

    private void greetUser(){
        // pour recuperer le prenom de lutilisateur
        String firstName = mPreferences.getString(PREF_KEY_FIRSTNAME, null);

        if ( firstName != null )// si ya une valeur
        {
            int score = mPreferences.getInt(PREF_KEY_SCORE, 0); // on prend le score

            String fullText = "Welcome back, " + firstName + "!\nYour last score was " + score + ", will you do better this time?";

            mGreetingText.setText(fullText); // changer le text de bienvenu
            mNameInput.setText(firstName);// afficher le prenom
            mNameInput.setSelection(firstName.length());
            mPlayButton.setEnabled(true);// activer le bouton qui est griser
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("MainActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("MainActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("MainActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("MainActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("MainActivity::onDestroy()");
    }


}
