package com.example.topquiz.model;


import java.util.Collections;
import java.util.List;

public class QuestionBank {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {  // recuper toute la list des questions possible
        mQuestionList = questionList;

        // Shuffle the question list           Mélangez la liste des questions
        Collections.shuffle(mQuestionList);

        mNextQuestionIndex = 0;
    }

    public Question getQuestion() {   // et fourni une nouvel question ici
        //    Assurez-vous de parcourir les questions
        if (mNextQuestionIndex == mQuestionList.size()) {
            mNextQuestionIndex = 0;
        }

        // Please note the post-incrementation       Veuillez noter l'incrémentation
        return mQuestionList.get(mNextQuestionIndex++);
    }

}
