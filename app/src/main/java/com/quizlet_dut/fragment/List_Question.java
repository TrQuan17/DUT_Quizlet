package com.quizlet_dut.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.quizlet_dut.R;
import com.quizlet_dut.Models.QuestionAdd;
import com.quizlet_dut.Adapters.QuestionAddAdapter;

import java.util.ArrayList;

public class List_Question extends AppCompatActivity {
    private GridView questionView;

    Button btnShow;
    FirebaseFirestore firestore;
    ArrayList<QuestionAdd> questionAddArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);

        firestore = FirebaseFirestore.getInstance();

        btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllQuestion();
                questionView = findViewById(R.id.question_Grid);
                QuestionAddAdapter adapter = new QuestionAddAdapter(questionAddArrayList);
                questionView.setAdapter(adapter);
                questionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(view.getContext(), Question_detail.class).putExtra("test", questionAddArrayList.get(position).getTest())
                                .putExtra("question", questionAddArrayList.get(position).getQuestion())
                                .putExtra("A", questionAddArrayList.get(position).getAnswer_A())
                                .putExtra("B", questionAddArrayList.get(position).getAnswer_B())
                                .putExtra("C", questionAddArrayList.get(position).getAnswer_C())
                                .putExtra("D", questionAddArrayList.get(position).getAnswer_D())
                                .putExtra("true_answer",String.valueOf(questionAddArrayList.get(position).getTrue_answer()))
                                .putExtra("id_question", questionAddArrayList.get(position).getId_question());
                        view.getContext().startActivity(intent);
                    }
                });
            }
        });
    }

    public void loadAllQuestion(){

        firestore.collection("Questions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots) {
                            questionAddArrayList.add(new QuestionAdd(
                                    doc.getString("TEST"),
                                    doc.getString("QUESTION"),
                                    doc.getString("CATEGORY"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER"),
                                    doc.getString("NAME_CATEGORY"),
                                    doc.getId()
                            ));
                        }
                    }
                });

    }
}