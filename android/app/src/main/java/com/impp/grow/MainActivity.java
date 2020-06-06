package com.impp.grow;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roacult.backdrop.BackdropLayout;

import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class    MainActivity extends AppCompatActivity implements OnClickListener {


    //variables
    private Question question;
    private int answerAmount;
    private boolean jeopardyMode;
    private String[] categories;
    private int currentCategoryID;
    private BackdropLayout backdropLayout;
    private View back_layout, front_layout;
    private ListView simpleList;
    private boolean fabActive;
    private FloatingActionButton fab;
    private Switch switchJeopardy;
    private String sheetURL;
    private Context context;
    private LinearLayout linearLayoutSheetUrl;
    private FrameLayout frameLayoutCategories;
    private FullScreenDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.MyAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init variables
        context = getApplicationContext();
        answerAmount = 4;
        currentCategoryID = 0;
        sheetURL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTZMOCrZdhsWPB4O-YiLrfE_sR2DcU3hgHQyg1y-_R648YOP3uX9eb0-gAqJN4Re70swEOONzS5t-Yc/pubhtml";
        //sheetURL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTo-d-1ObJn_cXyN2uINb1x8nW58qj5oY5hzLqYL4YJTwIjwY-sBrcM2tzGv564b5VzoPHOJSiaUcSW/pubhtml";
        jeopardyMode = false;

        //backdrop init
        backdropLayout = (BackdropLayout)findViewById(R.id.container);
        back_layout = backdropLayout.getChildAt(0);
        front_layout = backdropLayout.getChildAt(1);

        //init fab
        fab = findViewById(R.id.floatingActionButton);
        fab.hide();
        fabActive = false;

        //init jeopardySwitch
        switchJeopardy = findViewById(R.id.switchJeopardy);
        switchJeopardy.setChecked(jeopardyMode);
        switchJeopardy.setEnabled(false);

        BackendInterface.loadLibrary();
        if (BackendInterface.getDatabaseStatus()==true)
        {
            categories = BackendInterface.getCategories();
            ((TextView)findViewById(R.id.textViewCategory)).setText("Category: " + categories[currentCategoryID]);
            multipleChoiceQuestion (categories[currentCategoryID], jeopardyMode);
        }
        else
        {
            showEditDialog(true);
        }

        //init url field
        ((TextView)findViewById(R.id.sheetUrl)).setText(sheetURL);
        linearLayoutSheetUrl = ((LinearLayout)findViewById(R.id.sheet_url_section));
        linearLayoutSheetUrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                linearLayoutSheetUrl.setEnabled(false);
                showEditDialog(false);

            }
        });

        switchJeopardy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    jeopardyMode = true;
                } else {
                    jeopardyMode = false;
                }
                multipleChoiceQuestion(categories[currentCategoryID], jeopardyMode);
            }
        });

        //init popup categories
        frameLayoutCategories = (FrameLayout) findViewById(R.id.categories_section);
        frameLayoutCategories.setEnabled(false);
        frameLayoutCategories.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu for category selections
                PopupMenu popup = new PopupMenu(MainActivity.this, frameLayoutCategories);

                for (int i = 0; i < categories.length; i++){
                    popup.getMenu().add(0, i, 0, categories[i]);

                }

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu_categories, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ((TextView)findViewById(R.id.textViewCategory)).setText("Category: " + item.getTitle());
                        item.setChecked(true);
                        currentCategoryID = item.getItemId();
                        multipleChoiceQuestion (categories[currentCategoryID], jeopardyMode);

                        return true;
                    }
                });



                popup.show();//showing popup menu
            }
        });

        //init fab
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                multipleChoiceQuestion (categories[currentCategoryID], jeopardyMode);
            }
        });

        backdropLayout.setOnBackdropChangeStateListener(new Function1<BackdropLayout.State, Unit>() {
            @Override
            public Unit invoke(BackdropLayout.State state) {
                switch (state) {
                    case OPEN:
                        //Toast.makeText(context,"Open", Toast.LENGTH_SHORT).show();
                        fab.hide();
                        switchJeopardy.setEnabled(true);
                        frameLayoutCategories.setEnabled(true);
                        break;
                    case CLOSE:
                        //Toast.makeText(context,"Close", Toast.LENGTH_SHORT).show();
                        switchJeopardy.setEnabled(false);
                        frameLayoutCategories.setEnabled(false);
                        if(fabActive == true) {fab.show();}
                        break;
                }
                return null;
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onResume() {

        super.onResume();

    }


    // print a string by dividing it into parts of 1000 characters
    public static void largeLog(String tag, String content) {
       if (content.length() > 1000) {
           Log.d(tag, content.substring(0, 1000));
           largeLog(tag, content.substring(1000));
       } else {
           Log.d(tag, content);
       }
    }

    public String downloadGoogleSheet (String newSheetURL){

        // Download google sheet
        Downloader task = new Downloader(this);
        String result = null;

        try {
            result = task.execute(newSheetURL).get();
            //largeLog("Content", result);
            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createDatabase (String googleSheetContentTest){

        // Download google sheet
        DatabaseCreator task = new DatabaseCreator(this);
        String result = null;

        try {
            result = task.execute(googleSheetContentTest).get();
            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void multipleChoiceQuestion (String category, boolean jeopardyMode){

        fab.hide();
        fabActive = false;

        int questionNumber = BackendInterface.getRandomQuestion(category);
        String[] questionArray = BackendInterface.getQuestionDetails(questionNumber, jeopardyMode);
        String[] distractorArray = BackendInterface.getMCDistractors(questionNumber,  answerAmount-1, jeopardyMode);

        int correctAnswerID = Question.generateRandomIdCorrectAnswer(distractorArray.length);

        question = new Question(category,questionArray[0], questionArray[3], Question.generateAnswerList(correctAnswerID, questionArray[1], distractorArray),correctAnswerID);

        ((TextView)findViewById(R.id.category)).setText(question.getCategory());
        ((TextView)findViewById(R.id.question)).setText(question.getQuestion());

        simpleList = (ListView) findViewById(R.id.simpleListView);
        final ArrayAdapterQuestions arrayAdapterQuestions = new ArrayAdapterQuestions(this, 0, question.getAnswers());
        simpleList.setAdapter(arrayAdapterQuestions);

        // Set an item click listener for ListView
        simpleList.setEnabled(true);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapterQuestions.enterAnswer(position, question.getCorrectAnswerID(), view, question.getAdditionalInfo(), context);
                fab.show();
                fabActive=true;
                simpleList.setEnabled(false);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    private void showEditDialog(boolean atStart) {
        FullScreenDialog dialog = new FullScreenDialog(this, atStart);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, "FullScreenDialog.TAG");
    }


    //Set google sheet
    public void setGoogleSheet(String newGoogleSheetURL) {

        String googleSheetContent = downloadGoogleSheet (newGoogleSheetURL);

        //String appPath = getApplicationContext().getFilesDir().getAbsolutePath();
        //Log.d("apppath", appPath);

        createDatabase(googleSheetContent);
        sheetURL = newGoogleSheetURL;
        currentCategoryID = 0;
        categories = BackendInterface.getCategories();
        ((TextView)findViewById(R.id.textViewCategory)).setText("Category: " + categories[currentCategoryID]);
        multipleChoiceQuestion (categories[currentCategoryID], jeopardyMode);
    }




}
