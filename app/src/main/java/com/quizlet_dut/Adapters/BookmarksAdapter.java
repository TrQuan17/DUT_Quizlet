package com.quizlet_dut.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizlet_dut.Models.QuestionModel;
import com.quizlet_dut.R;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private List<QuestionModel> questionList;

    public BookmarksAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_item_layout, parent, false);
        return new BookmarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {

        String ques = questionList.get(position).getQuestion();
        String A = questionList.get(position).getOptionA();
        String B = questionList.get(position).getOptionB();
        String C = questionList.get(position).getOptionC();
        String D = questionList.get(position).getOptionD();
        int ans = questionList.get(position).getCorrectAns();

        holder.setData(position, ques, A, B, C, D, ans);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quesNo, question, optionA, optionB, optionC, optionD, result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);

        }

        private void setData(int pos, String ques, String A, String B, String C, String D, int correctAns) {
            quesNo.setText("Question No. "+ String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. " + A);
            optionB.setText("B. " + B);
            optionC.setText("C. " + C);
            optionD.setText("D. " + D);

            switch (correctAns) {
                case 1:
                    result.setText("ANSWER : " + A);
                    break;
                case 2:
                    result.setText("ANSWER : " + B);
                    break;
                case 3:
                    result.setText("ANSWER : " + C);
                    break;
                case 4:
                    result.setText("ANSWER : " + D);
                    break;
            }
        }

    }
}
