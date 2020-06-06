package com.impp.grow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ArrayAdapterQuestions extends ArrayAdapter {

    private Context context;
    private Question question;
    private ArrayList<String> answers;
    private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O"};
    private int countAlphabet = 0;
    private ArrayList<CardView> cardViewList;



    public ArrayAdapterQuestions(@NonNull Context context, int resource,  @NonNull ArrayList<String> objects) {
        super(context, resource,objects);

        this.context = context;
        this.answers = objects;

        cardViewList = new ArrayList<CardView>();
    }


    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        String answer = answers.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_item_answer, null);

        TextView letterText = (TextView) view.findViewById(R.id.letterNumber);
        TextView answerText = (TextView) view.findViewById(R.id.textView);
        CardView answerCardView = ((CardView)view.findViewById(R.id.card_view_answer));

        //set text
        letterText.setText(alphabet[countAlphabet]);
        countAlphabet++;
        answerText.setText(answer);


        //add to card view list
        cardViewList.add(answerCardView);

        return view;
    }

    public ArrayList<CardView> getCardViewList(){

        return cardViewList;
    }

    public void enterAnswer(int positionClicked, int correctAnswerID, View view, String additionalInfo, Context context){
        Log.d("Enter", "PositionClicked: " + positionClicked + " CorrectAnswerID: " +correctAnswerID + " View: " + view);
        LinearLayout layout = (LinearLayout) view;

        cardViewList.get(positionClicked).getBackground().setAlpha(100);
        CardView correctCardView = cardViewList.get(correctAnswerID);

        //if answer was correct, clicked card becomes green
        if (positionClicked == correctAnswerID) {
            //Correct answer handling
            correctCardView.setCardBackgroundColor(Color.parseColor("#00C589"));
        }
        //if answer was not correct, clicked card becomes red and correct answers becomes green
        else {
            cardViewList.get(positionClicked).setCardBackgroundColor(Color.parseColor("#DB647A"));
            LinearLayout linearLayoutWrongAnswer = (LinearLayout)  cardViewList.get(positionClicked).getChildAt(0);

            TextView textViewWrongAnswerLetter = (TextView) linearLayoutWrongAnswer.getChildAt(0);
            textViewWrongAnswerLetter.setTextColor(Color.parseColor("#ffffff"));
            textViewWrongAnswerLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_answer_letter_wrong_clicked));

            LinearLayout linearLayoutWrongAnswerText= (LinearLayout) linearLayoutWrongAnswer.getChildAt(1);
            TextView textViewWrongAnswer = (TextView) linearLayoutWrongAnswerText.getChildAt(0);
            textViewWrongAnswer.setTextColor(Color.parseColor("#ffffff"));

            //correct answer
            correctCardView.setCardBackgroundColor(Color.parseColor("#00C589"));
        }

        LinearLayout linearLayout = (LinearLayout)  correctCardView.getChildAt(0);
        TextView textViewAnswerLetter = (TextView) linearLayout.getChildAt(0);
        textViewAnswerLetter.setText("");
        textViewAnswerLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_answer_correct));

        LinearLayout linearLayoutAnswerText = (LinearLayout)  linearLayout.getChildAt(1);
        TextView textViewAnswer = (TextView) linearLayoutAnswerText.getChildAt(0);
        textViewAnswer.setTextColor(Color.parseColor("#FFFFFF"));

        if (!additionalInfo.equals("")){
            TextView textAdditionalInfo = new TextView(context);
            textAdditionalInfo.setPadding(0, 12, 0,0);
            textAdditionalInfo.setText(additionalInfo);
            textAdditionalInfo.setTextColor(Color.parseColor("#CCFFFFFF"));
            //textAdditionalInfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

            ((LinearLayout) linearLayoutAnswerText).addView(textAdditionalInfo);
        }

        //Set other wrong, not clicked answers to disabled
        for(int i = 0; i < cardViewList.size(); ++i) {
            if(i != correctAnswerID && i != positionClicked){
                LinearLayout linearLayoutWrongAnswer = (LinearLayout)  cardViewList.get(i).getChildAt(0);

                TextView textViewWrongAnswerLetter = (TextView) linearLayoutWrongAnswer.getChildAt(0);
                textViewWrongAnswerLetter.setTextColor(Color.parseColor("#DBDBDB"));
                textViewWrongAnswerLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_answer_letter_disabled));

                //Set wrong answers disabled
                LinearLayout linearLayoutWrongAnswerText= (LinearLayout) linearLayoutWrongAnswer.getChildAt(1);
                TextView textViewWrongAnswer = (TextView) linearLayoutWrongAnswerText.getChildAt(0);
                textViewWrongAnswer.setTextColor(Color.parseColor("#DBDBDB"));
            }
        }
    }
}
