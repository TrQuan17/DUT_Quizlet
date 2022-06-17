package com.quizlet_dut.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quizlet_dut.R;
import com.quizlet_dut.model.Question;
import com.quizlet_dut.model.Question;

import java.util.List;

public class QuestionAdapter extends BaseAdapter {

    private List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView;
        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        } else {
            myView = view;
        }

        TextView txtQuetion = myView.findViewById(R.id.txt_question);
        TextView txtTest = myView.findViewById(R.id.txt_test);
        TextView txtCategory =   myView.findViewById(R.id.txt_category);
        txtQuetion.setText(questionList.get(i).getQuestion());
        txtCategory.setText(questionList.get(i).getName_category());
        txtTest.setText(questionList.get(i).getTest());
        return myView;
    }
}
