package com.quizlet_dut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quizlet_dut.Models.QuestionModel;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV, timeTV, totalTV, correctTV, wrongTV, unattemptedTV;
    private Button leaderB, reAttemptB, viewAnsB;
    private long timeTaken;
    private Dialog progressDialog;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        init();
        loadData();
        setBookmarks();

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);

            }
        });

        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });

        saveResult();
    }

    private void init() {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.time);
        totalTV = findViewById(R.id.total_ques);
        correctTV = findViewById(R.id.correct);
        wrongTV = findViewById(R.id.wrong);
        unattemptedTV = findViewById(R.id.un_attempted);

        leaderB = findViewById(R.id.leader_button);
        reAttemptB = findViewById(R.id.re_attempt_button);
        viewAnsB = findViewById(R.id.view_answer_button);
    }

    private void loadData() {
        int correctQ = 0, wrongQ = 0, unatemptQ = 0;

        for(int i = 0; i < DbQuery.g_quesList.size(); i++) {
            QuestionModel q = DbQuery.g_quesList.get(i);
            if(q.getSelectedAns() == -1) {
                unatemptQ++;
            } else {
                if(q.getSelectedAns() == q.getCorrectAns()) {
                    correctQ++;
                } else {
                    wrongQ++;
                }
            }
        }

        totalTV.setText(DbQuery.g_quesList.size() + "");
        correctTV.setText(correctQ + "");
        unattemptedTV.setText(unatemptQ + "");
        wrongTV.setText(wrongQ + "");

        finalScore = (correctQ*100)/DbQuery.g_quesList.size();
        scoreTV.setText(finalScore + "");

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);
        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))

        );
        timeTV.setText(time);
    }

    private void reAttempt() {
        for(int i = 0; i < DbQuery.g_quesList.size(); i++) {
            QuestionModel q = DbQuery.g_quesList.get(i);

            q.setSelectedAns(-1);
            q.setStatus(DbQuery.NOT_VISITED);
        }

        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompeleteListenner() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong! Please try again later!",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setBookmarks() {
        for(int i = 0; i < DbQuery.g_quesList.size(); i++) {
            QuestionModel q = DbQuery.g_quesList.get(i);

            if(q.isBookmarked()) {
                if(!DbQuery.g_bmIdList.contains(q.getqID())) {
                    DbQuery.g_bmIdList.add(q.getqID());
                    DbQuery.myProfileModel.setBookmarksCount(DbQuery.g_bmIdList.size());
                }
            }
            else {
                if(DbQuery.g_bmIdList.contains(q.getqID())) {
                    DbQuery.g_bmIdList.remove(q.getqID());
                    DbQuery.myProfileModel.setBookmarksCount(DbQuery.g_bmIdList.size());
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}