package com.quizlet_dut.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quizlet_dut.CategoryAdapter;
import com.quizlet_dut.CategoryModel;
import com.quizlet_dut.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private GridView catView;
    private List<CategoryModel> catList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        catView = view.findViewById(R.id.cat_Grid);
        loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(catList);
        catView.setAdapter(adapter);

        return view;
    }

    private void loadCategories() {
        catList.clear();
        catList.add(new CategoryModel("1", "English", 50));
        catList.add(new CategoryModel("2", "History", 30));
        catList.add(new CategoryModel("3", "Music", 20));
        catList.add(new CategoryModel("4", "Math", 100));
        catList.add(new CategoryModel("5", "Chemistry", 40));
    }
}