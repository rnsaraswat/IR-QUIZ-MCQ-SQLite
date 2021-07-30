package com.example.irquizmcqsqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.SQLInput;

public class Question implements Parcelable {


        public static final String DIFFICULTY_EASY = "Easy";
        public static final String DIFFICULTY_MEDIUM = "Medium";
        public static final String DIFFICULTY_HARD = "Hard";

        private int id;
        private String question;
        private String option1;
        private String option2;
        private String option3;
        private String option4;
        private int answerNumber;
        private String difficulty;
        private int categoryID;


        protected Question(Parcel in) {
                Log.d("Ravi", "Question Question Parcel Start 76");
                id = in.readInt();
                question = in.readString();
                option1 = in.readString();
                option2 = in.readString();
                option3 = in.readString();
                option4 = in.readString();
                answerNumber = in.readInt();
                difficulty = in.readString();
                categoryID = in.readInt();
                Log.d("Ravi", "Question Question Parcel Start 77");
        }

        public Question(String s, String a, String b, String c, String d, int i, String difficultyEasy, int programming) {
                Log.d("Ravi", "Question Question  Start 78");
        }

        public Question() {
                Log.d("Ravi", "Question Question  Start 79");
                this.question = question;
                this.option1 = option1;
                this.option2 = option2;
                this.option3 = option3;
                this.option4 = option4;
                this.answerNumber = answerNumber;
                this.difficulty = difficulty;
                this.categoryID = categoryID;
                Log.d("Ravi", "Question Question  Start 80");
        }



        @Override
        public void writeToParcel(Parcel dest, int flags) {
                Log.d("Ravi", "Question writeToParcel  Start 81");
                dest.writeInt(id);
                dest.writeString(question);
                dest.writeString(option1);
                dest.writeString(option2);
                dest.writeString(option3);
                dest.writeString(option4);
                dest.writeInt(answerNumber);
                dest.writeString(difficulty);
                dest.writeInt(categoryID);
                Log.d("Ravi", "Question writeToParcel  Start 82");
        }

        @Override
        public int describeContents() {
                return 0;
        }

        public static final Creator<Question> CREATOR = new Creator<Question>() {
                @Override
                public Question createFromParcel(Parcel in) {
                        return new Question(in);
                }
                @Override
                public Question[] newArray(int size) {
                        return new Question[size];
                }
        };
        public int getId() {
                return id;
        }
        public void setId(int id) {
                this.id = id;
        }

        public String getQuestion() {
                return question;
        }

        public void setQuestion(String question) {
                this.question = question;
        }

        public String getOption1() {
                return option1;
        }

        public void setOption1(String option1) {
                this.option1 = option1;
        }

        public String getOption2() {
                return option2;
        }

        public void setOption2(String option2) {
                this.option2 = option2;
        }

        public String getOption3() {
                return option3;
        }

        public void setOption3(String option3) {
                this.option3 = option3;
        }

        public String getOption4() {
                return option4;
        }

        public void setOption4(String option4) {
                this.option4 = option4;
        }

        public int getAnswerNumber() {
                return answerNumber;
        }

        public void setAnswerNumber(int answerNumber) {
                this.answerNumber = answerNumber;
        }

        public String getDifficulty() {
                return difficulty;
        }
        public void setDifficulty(String difficulty) {
                this.difficulty = difficulty;
        }
        public int getCategoryID() {
                return categoryID;
        }
        public void setCategoryID(int categoryID) {
                this.categoryID = categoryID;
        }
                public static String[] getAllDifficultyLevels() {
                return new String[]{
                        DIFFICULTY_EASY,
                        DIFFICULTY_MEDIUM,
                        DIFFICULTY_HARD
                };
        }
}
