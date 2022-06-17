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

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<QuestionModel> questionList;

    public AnswersAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_item_layout, parent, false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {

        String ques = questionList.get(position).getQuestion();
        String A = questionList.get(position).getOptionA();
        String B = questionList.get(position).getOptionB();
        String C = questionList.get(position).getOptionC();
        String D = questionList.get(position).getOptionD();
        int selected = questionList.get(position).getSelectedAns();
        int ans = questionList.get(position).getCorrectAns();

        holder.setData(position, ques, A, B, C, D, selected, ans);
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

        private void setData(int pos, String ques, String A, String B, String C, String D, int selected, int correctAns) {
            quesNo.setText("QuestionAdd No. "+ String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. " + A);
            optionB.setText("B. " + B);
            optionC.setText("C. " + C);
            optionD.setText("D. " + D);

            if(selected == -1) {
                result.setText("UN ANSWERED");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                setOptionColor(selected, R.color.normal);

            }
            else {
                if(selected == correctAns) {
                    result.setText("CORRECT");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
                    setOptionColor(selected, R.color.green);
                }
                else {
                    result.setText("WRONG");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                    setOptionColor(selected, R.color.red);
                }
            }

        }

        private void setOptionColor(int selected , int color) {
            optionA.setTextColor(itemView.getContext().getResources().getColor(R.color.normal));
            optionB.setTextColor(itemView.getContext().getResources().getColor(R.color.normal));
            optionC.setTextColor(itemView.getContext().getResources().getColor(R.color.normal));
            optionD.setTextColor(itemView.getContext().getResources().getColor(R.color.normal));

            switch (selected) {
                case 1:
                    optionA.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 2:
                    optionB.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 3:
                    optionC.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 4:
                    optionD.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;

            }
        }

    }
}
