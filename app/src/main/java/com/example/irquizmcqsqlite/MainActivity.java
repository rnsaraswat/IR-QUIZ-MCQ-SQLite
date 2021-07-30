package com.example.irquizmcqsqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // define variables
    private static final int REQUEST_CODE_QUIZ = 2;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_NAME = "extraName";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;
    private TextView textViewHighscore;
    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Ravi", "MainActivity onCreate Start 1");
        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spinner_category);

        loadCategories();
        loadDifficultyLevels();
        loadHighscore();


//        startbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name=nametext.getText().toString();
//                Intent intent=new Intent(getApplicationContext(), Question.class);
//                intent.putExtra("myname",name);
//                startActivity(intent);
//            }
//        });

        Button aboutbutton=(Button)findViewById(R.id.buttonquit);
        aboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ravi", "MainActivity aboutbutton onClick Start 2");
                Intent intent=new Intent(getApplicationContext(),DevelopersActivity.class);
                startActivity(intent);
            }
        });


//        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, difficultyLevels);
//        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDifficulty.setAdapter(adapterDifficulty);
//
//        loadHighscore();
        Log.d("Ravi", "MainActivity  onCreate Start 3");
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        Log.d("Ravi", "MainActivity onCreate Start 3");
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ravi", "MainActivity buttonStartQuiz onClick Start 4");
                startQuiz();
                Log.d("Ravi", "MainActivity buttonStartQuiz onClick Start 5");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.item1:Bundle dataBundleQuestion = new Bundle();
                dataBundleQuestion.putInt("id", 0);
                Log.d("Ravi", "onOptionsItemSelected Start 10");
                Intent intentQuestion = new Intent(getApplicationContext(),QuestionAddModifyDelete.class);
                intentQuestion.putExtras(dataBundleQuestion);
                Log.d("Ravi", "onOptionsItemSelected Start 11");
                startActivity(intentQuestion);
                return true;
            case R.id.item2:Bundle dataBundleCategory = new Bundle();
                dataBundleCategory.putInt("id", 0);
                Log.d("Ravi", "onOptionsItemSelected Start 10");
                Intent intentCategory = new Intent(getApplicationContext(),CategoryAddModifyDelete.class);
                intentCategory.putExtras(dataBundleCategory);
                Log.d("Ravi", "onOptionsItemSelected Start 11");
                startActivity(intentCategory);
                return true;
            default:
                Log.d("Ravi", "onOptionsItemSelected Start 12");
                return super.onOptionsItemSelected(item);
        }
    }

    private void startQuiz() {
        Log.d("Ravi", "MainActivity startQuiz Start 6");
        final EditText nametext=(EditText)findViewById(R.id.editTextTextPersonName);
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String nameText=nametext.getText().toString();
        String categoryName = selectedCategory.getName();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Log.d("Ravi", "MainActivity startQuiz Start 7");
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_NAME,nameText);
//        intent.putExtra("myname",name);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        Log.d("Ravi", "MainActivity startQuiz Start 8");
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Ravi", "MainActivity onActivityResult Start 10");
        if (requestCode == REQUEST_CODE_QUIZ) {
            Log.d("Ravi", "MainActivity onActivityResult Start 11");
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                Log.d("Ravi", "MainActivity onActivityResult Start 12");
                if (score > highscore) {
                    updateHighscore(score);
                    Log.d("Ravi", "MainActivity onActivityResult Start 13");
                }
            }
        }
    }
    private void loadCategories() {
        Log.d("Ravi", "MainActivity loadCategories Start 14");
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();
        Log.d("Ravi", "MainActivity loadCategories Start 15");
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        Log.d("Ravi", "MainActivity loadCategories Start 16");
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("Ravi", "MainActivity loadCategories Start 17");
        spinnerCategory.setAdapter(adapterCategories);
    }
    private void loadDifficultyLevels() {
        Log.d("Ravi", "MainActivity loadDifficultyLevels Start 18");
        String[] difficultyLevels = Question.getAllDifficultyLevels();
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        Log.d("Ravi", "MainActivity loadDifficultyLevels Start 19");
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("Ravi", "MainActivity loadDifficultyLevels Start 20");
        spinnerDifficulty.setAdapter(adapterDifficulty);
        Log.d("Ravi", "MainActivity loadDifficultyLevels Start 21");
    }

    private void loadHighscore() {
        Log.d("Ravi", "MainActivity loadHighscore Start 22");
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("Ravi", "MainActivity loadHighscore Start 23");
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        Log.d("Ravi", "MainActivity loadHighscore Start 24");
        textViewHighscore.setText("Highscore: " + highscore);
        Log.d("Ravi", "MainActivity loadHighscore Start 25");
    }
    private void updateHighscore(int highscoreNew) {
        Log.d("Ravi", "MainActivity updateHighscore Start 26");
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);
        Log.d("Ravi", "MainActivity updateHighscore Start 26");
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("Ravi", "MainActivity updateHighscore Start 27");
        SharedPreferences.Editor editor = prefs.edit();
        Log.d("Ravi", "MainActivity updateHighscore Start 28");
        editor.putInt(KEY_HIGHSCORE, highscore);
        Log.d("Ravi", "MainActivity updateHighscore Start 29");
        editor.apply();
        Log.d("Ravi", "MainActivity updateHighscore Start 30");
    }
}
