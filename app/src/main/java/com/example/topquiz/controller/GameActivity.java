package com.example.topquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.R;
import com.example.topquiz.model.Question;
import com.example.topquiz.model.QuestionBank;

import java.util.Arrays;

import static java.lang.System.out;

public class GameActivity extends AppCompatActivity implements View.OnClickListener { // mon activity implemente le click de touche  pour pas repeter

    // les touches
    private TextView mQuestionTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    // implementer le model
    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
   
    private int mScore;// score de lutilisateur
    private int mNumberOfQuestions; // nbr de question

    public static final String BUNDLE_STATE_SCORE = "currentScore";  // sauvegarder le score actuel
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion"; // sauv le numero de question a la quel on est

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE"; // pour enregisrer le score

    private boolean mEnableTouchEvents;// pour pas cliquer 2 fois penant le temps darret

    @Override
    protected void onCreate(Bundle savedInstanceState) { // methode surcharger   automatiquement lors de la cr"ation du projet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null){ // si il existe
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        }
        else {
            mScore = 0; // initialisation
            mNumberOfQuestions = 4; // nbr de question
        }

        System.out.println("GameActivity::onCreate()"); // trace

        mEnableTouchEvents = true;// activer ou pas les different elements

        // implementer le model fonction appeler plus bas
        mQuestionBank = this.generateQuestions();
        // valeurs des touches
        mQuestionTextView = (TextView) findViewById(R.id.activity_game_question_text);
        mAnswerButton1 = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswerButton2 = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswerButton3 = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswerButton4 = (Button) findViewById(R.id.activity_game_answer4_btn);

        // chaque bouton possede une information
        mAnswerButton1.setTag(0);
        mAnswerButton2.setTag(1);
        mAnswerButton3.setTag(2);
        mAnswerButton4.setTag(3);

        // pour appeler onclick        par lactivite courante
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);



        mCurrentQuestion = mQuestionBank.getQuestion(); // la 1ere question est memoriser
        this.displayQuestion(mCurrentQuestion);

    }

    @Override // pour enregistrer des valeurs sur android
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(BUNDLE_STATE_SCORE, mScore);  // ajouter le score
        outState.putInt(BUNDLE_EXTRA_SCORE, mNumberOfQuestions); // ajouter le nbr de question poser
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {    // methode appeler a chaque fois que lutilisateur clique sur le une reponse
        // identifient du bouton (on force le int ) renvoie un objet
        int responseIndex = (int) v.getTag();
        // si le bouton est egale a la question courante et a l'ID de la  question courante
        if (responseIndex == mCurrentQuestion.getAnswerIndex()) { // recuperer sa reponse associé
            // Good answer
            // message toasr ( contexte activity, text, durer d'affichage 2sec) afficher
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            mScore++; // si la reponse est juste on incremente le score
        }
        else {
            // Wrong answer
            Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                if (--mNumberOfQuestions == 0){// on vas décrementer le nbr de question a chaque clic
                    endGame(); // si = 0 on arrete le jeu
                }

                else { // sinon
                    mCurrentQuestion = mQuestionBank.getQuestion(); // nouvel question
                    displayQuestion(mCurrentQuestion); // affiche la nouvelle question
                }
            }
        }, 2000);// attendre 2sec
    }

    @Override  // A chaque fois que l'utilisateur touche lécran
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // on regarde si = a true
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }



    private void endGame() { // arret du jeu
        // boite de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!") // titre
                .setMessage("Your score is " + mScore) // message
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // une seul bouton apeler ok
                        Intent intent = new Intent(); // pour passer la valeur 42
                        // mettre une valeur dans intent ( recuperer la valeur mscore )
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent); // enregistrer intent sur android
                        finish();// fini l'activité     arret du jeu
                    }
                })

                .create() // creer la boite de dialogue
                .show(); // affiche la boite de dialogue


    }


    private void displayQuestion(final Question question) {  // prend en parametre une question
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));  // et mets a jour les different chemps dinterface graphique
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }


    private QuestionBank generateQuestions() {  // generer toute une liste de question
        Question question1 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9));
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
