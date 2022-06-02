package com.quizlet_dut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.quizlet_dut.databinding.ActivityQuestionsBinding;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionview = findViewById(R.id.questions_view);
        QuestionsAdapter quesAdapter = new QuestionsAdapter(DbQuery.g_quesList);
        questionview.setAdapter(quesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(QuestionsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionview.setLayoutManager(layoutManager);


    }
}