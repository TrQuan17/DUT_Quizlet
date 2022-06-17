package com.quizlet_dut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.quizlet_dut.Adapters.AnswersAdapter;
import com.quizlet_dut.Adapters.BookmarksAdapter;

public class BookMarksActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marks);
        questionsView = findViewById(R.id.ba_recycler_view);

        Toolbar toolbar = findViewById(R.id.ba_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("BOOK MARKS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(BookMarksActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        questionsView.setLayoutManager(layoutManager);

        DbQuery.loadBookmarks(new MyCompeleteListenner() {
            @Override
            public void onSuccess() {
                BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(DbQuery.g_bookmarksList);
                questionsView.setAdapter(bookmarksAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            BookMarksActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}