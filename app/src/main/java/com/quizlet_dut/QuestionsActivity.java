package com.quizlet_dut;

import static com.quizlet_dut.DbQuery.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quizlet_dut.databinding.ActivityQuestionsBinding;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionview;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button sumitB, markB, clearSelB;
    private ImageButton preQuesB, nextQuesB;
    private ImageView quesListB;
    private int quesID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        QuestionsAdapter quesAdapter = new QuestionsAdapter(g_quesList);
        questionview.setAdapter(quesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(QuestionsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionview.setLayoutManager(layoutManager);

        setSnapHelper();

        setClickListeners();
        startTime();



    }
    private void init() {
        questionview = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        sumitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelB = findViewById(R.id.clear_selB);
        preQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);

        quesID =0;

        tvQuesID.setText("1/" + String.valueOf(g_quesList.size()));
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());



    }
    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionview);

        questionview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                tvQuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(g_quesList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void setClickListeners() {
        preQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID > 0) {
                    questionview.smoothScrollToPosition(quesID - 1);
                }


            }
        });
        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID < g_quesList.size() - 1) {
                    questionview.smoothScrollToPosition(quesID + 1);
                }
            }
        });
    }
    private void startTime() {
        long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;
        CountDownTimer timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))

                        );
                timerTV.setText(time);
            }

            @Override
            public void onFinish() {


            }
        };
        timer.start();
    }
}