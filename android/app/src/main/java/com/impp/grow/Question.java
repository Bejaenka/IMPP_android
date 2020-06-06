package com.impp.grow;

import java.util.ArrayList;
import java.util.Random;

public class Question {

    private String category;
    private String question;
    private String additionalInfo;
    private ArrayList<String> answers;
    private int correctAnswerID;

    public Question(String category, String question, String additionalInfo, ArrayList<String> answers, int correctAnswerID){
        this.category = "Category: " + category;
        this.question = question;
        this.additionalInfo = additionalInfo;
        this.answers = answers;
        this.correctAnswerID = correctAnswerID;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerID() {
        return correctAnswerID;
    }


    public static int generateRandomIdCorrectAnswer(int answerCount)
    {

        Random r = new Random();
        if (answerCount>0){
            return r.nextInt(answerCount);
        }
        else {
            return 0;
        }
    }

    public static ArrayList<String> generateAnswerList (int idCorrectAnswer, String correctAnswer, String[] distractors)
    {
        ArrayList<String> answers = new ArrayList<String>();
        int counterArray = 0;

        for (int i = 0; i <= distractors.length ; i++){

            if(i == idCorrectAnswer){
                answers.add(correctAnswer);
            }

            else{
                answers.add(distractors[counterArray]);
                counterArray++;

            }
        }
        return answers;
    }


}
