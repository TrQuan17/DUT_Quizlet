package com.quizlet_dut;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHoder> {

    private List<TestModel> testList;

    public TestAdapter(List<TestModel> testList){
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_item_layout, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHoder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position, progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        private TextView testNo;
        private TextView topScore;
        private ProgressBar progressBar;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);

            testNo = itemView.findViewById(R.id.testNo);
            topScore = itemView.findViewById(R.id.scoreText);
            progressBar = itemView.findViewById(R.id.testProgressBar);
        }

        private void setData(int pos, int progress){
            testNo.setText("Test No: " + String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress) + " %");

            progressBar.setProgress(progress);
        }

    }
}