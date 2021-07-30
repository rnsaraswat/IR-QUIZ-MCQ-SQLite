package com.example.irquizmcqsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.example.irquizmcqsqlite.QuizDbHelper.getInstance;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private TextView textViewDifficulty;
    private TextView textViewCategory;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
//    private List<Question> questionList;
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long backPressedTime;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d("Ravi", "QuizActivity onCreate Start 31");
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);
        Log.d("Ravi", "QuizActivity onCreate Start 32");



        textViewCategory.setText("Category: " + categoryName);
        textViewDifficulty.setText("Difficulty: " + difficulty);
        Log.d("Ravi", "QuizActivity onCreate Start 33");
        if (savedInstanceState == null) {
            Log.d("Ravi", "QuizActivity onCreate Start 34");
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            Log.d("Ravi", "QuizActivity onCreate Start 35" + questionCountTotal);
            showNextQuestion();
            Log.d("Ravi", "QuizActivity onCreate Start 36");
        } else {
            Log.d("Ravi", "QuizActivity onCreate Start 37");
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            Log.d("Ravi", "QuizActivity onCreate Start 38");
            if (!answered) {
                startCountDown();
                Log.d("Ravi", "QuizActivity onCreate Start 39");
            } else {
                updateCountDownText();
                Log.d("Ravi", "QuizActivity onCreate Start 40");
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 41");
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 42");
                        checkAnswer();
                        Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 43");
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                        Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 44");
                    }
                } else {
                    Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 45");
                    showNextQuestion();
                    Log.d("Ravi", "QuizActivity setOnClickListener onClick Start 46");
                }
            }
        });
    }

    private void showNextQuestion() {
        Log.d("Ravi", "QuizActivity showNextQuestion Start 47");
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            Log.d("Ravi", "QuizActivity showNextQuestion Start 48");
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
            Log.d("Ravi", "QuizActivity showNextQuestion Start 49");
        } else {
            finishQuiz();
            Log.d("Ravi", "QuizActivity showNextQuestion Start 50");
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Ravi", "QuizActivity startCountDown Start 51");
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                Log.d("Ravi", "QuizActivity onFinish Start 52");
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText() {
        Log.d("Ravi", "QuizActivity updateCountDownText Start 53");
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        Log.d("Ravi", "QuizActivity updateCountDownText Start 54");
        if (timeLeftInMillis < 10000) {
            Log.d("Ravi", "QuizActivity updateCountDownText Start 55");
            textViewCountDown.setTextColor(Color.RED);
        } else {
            Log.d("Ravi", "QuizActivity updateCountDownText Start 56");
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        Log.d("Ravi", "QuizActivity checkAnswer Start 57");
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNumber()) {
            Log.d("Ravi", "QuizActivity checkAnswer Start 58");
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
        Log.d("Ravi", "QuizActivity checkAnswer Start 59");
    }
    private void showSolution() {
        Log.d("Ravi", "QuizActivity showSolution Start 60");
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNumber()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                Log.d("Ravi", "QuizActivity showSolution Start 61");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                Log.d("Ravi", "QuizActivity showSolution Start 62");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                Log.d("Ravi", "QuizActivity showSolution Start 63");
                break;
            case 4:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 4 is correct");
                Log.d("Ravi", "QuizActivity showSolution Start 64");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
            Log.d("Ravi", "QuizActivity showSolution Start 65");
        } else {
            buttonConfirmNext.setText("Finish");
            Log.d("Ravi", "QuizActivity showSolution Start 66");
        }
    }
    private void finishQuiz() {
        Log.d("Ravi", "QuizActivity finishQuiz Start 67");
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
        Log.d("Ravi", "QuizActivity finishQuiz Start 68");
    }
    @Override
    public void onBackPressed() {
        Log.d("Ravi", "QuizActivity onBackPressed Start 69");
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Log.d("Ravi", "QuizActivity onBackPressed Start 70");
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
            Log.d("Ravi", "QuizActivity onBackPressed Start 71");
        }
        backPressedTime = System.currentTimeMillis();
        Log.d("Ravi", "QuizActivity onBackPressed Start 72");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Ravi", "QuizActivity onDestroy Start 73");
        if (countDownTimer != null) {
            countDownTimer.cancel();
            Log.d("Ravi", "QuizActivity onDestroy Start 74");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Ravi", "QuizActivity onSaveInstanceState Start 75");
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
        Log.d("Ravi", "QuizActivity onSaveInstanceState Start 76");
    }
}
