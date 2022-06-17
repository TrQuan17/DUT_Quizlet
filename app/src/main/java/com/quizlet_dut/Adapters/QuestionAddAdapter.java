package com.quizlet_dut.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quizlet_dut.Models.QuestionAdd;
import com.quizlet_dut.R;

import java.util.List;

public class QuestionAddAdapter extends BaseAdapter {

    private List<QuestionAdd> questionAddList;

    public QuestionAddAdapter(List<QuestionAdd> questionAddList) {
        this.questionAddList = questionAddList;
    }

    @Override
    public int getCount() {
        return questionAddList.size();
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
        txtQuetion.setText(questionAddList.get(i).getQuestion());
        txtCategory.setText(questionAddList.get(i).getName_category());
        txtTest.setText(questionAddList.get(i).getTest());
        return myView;
    }
}
