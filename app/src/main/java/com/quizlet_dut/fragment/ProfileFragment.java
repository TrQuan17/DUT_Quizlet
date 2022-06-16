package com.quizlet_dut.fragment;

import static com.quizlet_dut.DbQuery.g_userCount;
import static com.quizlet_dut.DbQuery.g_userList;
import static com.quizlet_dut.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.quizlet_dut.DbQuery;
import com.quizlet_dut.LoginActivity;
import com.quizlet_dut.MainActivity;
import com.quizlet_dut.MyCompeleteListenner;
import com.quizlet_dut.MyProfileActivity;
import com.quizlet_dut.R;

public class ProfileFragment extends Fragment {

    private LinearLayout buttonLogout;
    private TextView profile_img_text, name, score, rank;
    private LinearLayout leaderB, profileB, bookmarkB;

    private Dialog progressDialog;
    private TextView dialogText;

    private BottomNavigationView bottomNavigationView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);


        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");




        String userName = DbQuery.myProfileModel.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));

        name.setText(userName);

        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if(DbQuery.g_userList.size() == 0) {
            progressDialog.show();

            DbQuery.getTopUsers(new MyCompeleteListenner() {
                @Override
                public void onSuccess() {

                    if(myPerformance.getScore() != 0) {
                        if( ! DbQuery.isMeOnTopList) {
                            calculateRank();
                        }
                        score.setText("Score: " + myPerformance.getScore());
                        rank.setText("Rank: " + myPerformance.getRank());

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


        }
        else {
            score.setText("Score: " + myPerformance.getScore());
            if(myPerformance.getScore() != 0) {
                rank.setText("Rank: " + myPerformance.getRank());
            }

        }


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
            }
        });
        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
            }
        });


        return view;
    }
    private void initViews(View view) {
        buttonLogout = view.findViewById(R.id.logout_B);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        leaderB = view.findViewById(R.id.leaderboard);
        bookmarkB = view.findViewById(R.id.bookmark);
        profileB = view.findViewById(R.id.profile);
        //bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);


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