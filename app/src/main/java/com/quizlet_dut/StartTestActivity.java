package com.quizlet_dut;

import static com.quizlet_dut.DbQuery.g_catList;
import static com.quizlet_dut.DbQuery.g_selected_test_index;
import static com.quizlet_dut.DbQuery.loadQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quizlet_dut.databinding.ActivityLoginBinding;
import com.quizlet_dut.databinding.ActivityStartTestBinding;

public class StartTestActivity extends AppCompatActivity {
    private ActivityStartTestBinding binding;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartTestBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        init();

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        progressDialog.show();

        loadQuestions(new MyCompeleteListenner() {

            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Something went wrong! Please try again. ",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        binding.btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData() {
        binding.stCatName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());
        binding.stTestNo.setText("Test No. " + String.valueOf(DbQuery.g_selected_test_index + 1));
        if(DbQuery.g_quesList.size() == 0) {
            System.out.println("Chuc ban som fix duoc loi nay");
        }
        binding.stTotalQues.setText(String.valueOf(DbQuery.g_quesList.size()));
        binding.stBestScore.setText(String.valueOf(DbQuery.g_testList.get(g_selected_test_index).getTopScore()));
        binding.stTime.setText(String.valueOf(DbQuery.g_testList.get(g_selected_test_index).getTime()));
    }
}