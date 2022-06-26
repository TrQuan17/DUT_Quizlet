package com.quizlet_dut.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quizlet_dut.Adapters.CategoryAdapter;
import com.quizlet_dut.DbQuery;
import com.quizlet_dut.R;

public class HomeFragment extends Fragment {

    private GridView catView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        catView = view.findViewById(R.id.cat_Grid);
//        loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);

        return view;
    }

}