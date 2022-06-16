package com.quizlet_dut.fragment;

import static com.quizlet_dut.DbQuery.g_userCount;
import static com.quizlet_dut.DbQuery.g_userList;
import static com.quizlet_dut.DbQuery.myPerformance;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizlet_dut.Adapters.RankAdapter;
import com.quizlet_dut.DbQuery;
import com.quizlet_dut.MainActivity;
import com.quizlet_dut.MyCompeleteListenner;
import com.quizlet_dut.R;
import com.quizlet_dut.TestActivity;

import org.w3c.dom.Text;


public class RankFragment extends Fragment {

    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter rankAdapter;
    private Dialog progressDialog;
    private TextView dialogText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);


        //((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        initViews(view);


        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");
        progressDialog.show();




        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        rankAdapter = new RankAdapter(DbQuery.g_userList);
        usersView.setAdapter(rankAdapter);

        DbQuery.getTopUsers(new MyCompeleteListenner() {
            @Override
            public void onSuccess() {
                rankAdapter.notifyDataSetChanged();
                if(myPerformance.getScore() != 0) {
                    if( ! DbQuery.isMeOnTopList) {
                        calculateRank();
                    }
                    myScoreTV.setText("Score: " + myPerformance.getScore());
                    myRankTV.setText("Rank: " + myPerformance.getRank());

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                Toast.makeText(getContext(), "Something went wrong! Please try again. ",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

        totalUsersTV.setText("Total users: " + DbQuery.g_userCount);
        myImgTextTV.setText(myPerformance.getName().toUpperCase().substring(0,1));
        return view;
    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.total_users);
        myImgTextTV = view.findViewById(R.id.img_text);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.user_view);
    }

    private void calculateRank() {
        int lowTopScore = g_userList.get(g_userList.size() - 1).getScore();

        int remaining_slots = g_userCount - 20;
        int myslot = (myPerformance.getScore()*remaining_slots) / lowTopScore;
        int rank;

        if(lowTopScore != myPerformance.getScore()) {
            rank = g_userCount - myslot;
        }
        else {
            rank = 21;
        }
        myPerformance.setRank(rank);
    }

}