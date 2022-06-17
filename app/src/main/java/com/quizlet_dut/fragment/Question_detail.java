package com.quizlet_dut.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quizlet_dut.R;

public class Question_detail extends AppCompatActivity {

    FirebaseFirestore firestore;
    EditText txtA,txtB,txtC,txtD,txtAnswer,txtQuestion;
    Button btnEdit,btnDelete;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        firestore = FirebaseFirestore.getInstance();

        txtA = findViewById(R.id.txt_answerA);
        txtB = findViewById(R.id.txt_answerB);
        txtC = findViewById(R.id.txt_answerC);
        txtD = findViewById(R.id.txt_answerD);
        txtAnswer = findViewById(R.id.txt_TrueAnswer);
        txtQuestion = findViewById(R.id.txt_Quest);


        intent = getIntent();
        if(intent.getExtras() != null){
            String question = intent.getStringExtra("question");
            String A = intent.getStringExtra("A");
            String B = intent.getStringExtra("B");
            String C = intent.getStringExtra("C");
            String D = intent.getStringExtra("D");
            String true_answer = intent.getStringExtra("true_answer");


            txtQuestion.setText(question);
            txtA.setText(A);
            txtB.setText(B);
            txtC.setText(C);
            txtD.setText(D);
            txtAnswer.setText(true_answer);
        }

        btnEdit = findViewById(R.id.btn_Edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckEmptyInfor() == false){
                    Toast.makeText(Question_detail.this, "Please insert full information and number of test must be int",Toast.LENGTH_SHORT).show();

                }
                else if(isNumeric(txtAnswer.getText().toString()) == false){
                    Toast.makeText(Question_detail.this, "Answer must be int and value between(1-4)",Toast.LENGTH_SHORT).show();
                }
                else if((isNumeric(txtAnswer.getText().toString()) == true) && ((Integer.valueOf(txtAnswer.getText().toString()) > 4)
                        || (Integer.valueOf(txtAnswer.getText().toString()) < 1))){
                    Toast.makeText(Question_detail.this, "Answer must be int and value between(1-4)",Toast.LENGTH_SHORT).show();
                }
                else{
                    intent = getIntent();
                    String id_question = intent.getStringExtra("id_question");
                    String question = txtQuestion.getText().toString();
                    String A = txtA.getText().toString();
                    String B = txtB.getText().toString();
                    String C = txtC.getText().toString();
                    String D = txtD.getText().toString();
                    long true_answer = Long.parseLong(txtAnswer.getText().toString());
                    UpdateQuestion(id_question,question,A,B,C,D,true_answer);
                }
            }
        });

        btnDelete = findViewById(R.id.btn_Delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = getIntent();
                String id_question = intent.getStringExtra("id_question");
                DeleteQuestion(id_question);
            }
        });
    }

    private boolean CheckEmptyInfor(){
        String empty = "";
        if( txtA.getText().toString().equals(empty) || txtB.getText().toString().equals(empty) ||  txtC.getText().toString().equals(empty) ||  txtD.getText().toString().equals(empty)
                ||  txtAnswer.getText().toString().equals(empty) || txtQuestion.getText().toString().equals(empty) ) {

            return false;
        }
        else{
            return true;
        }
    }

    // hàm kiểm tra dữ liệu nhập vào có phải là số hay không?
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void UpdateQuestion(String id_question, String question, String A, String B, String C, String D, long true_answer){
        firestore.collection("Questions").document(id_question).update("QUESTION",question);
        firestore.collection("Questions").document(id_question).update("A",A);
        firestore.collection("Questions").document(id_question).update("B",B);
        firestore.collection("Questions").document(id_question).update("C",C);
        firestore.collection("Questions").document(id_question).update("D",D);
        firestore.collection("Questions").document(id_question).update("ANSWER",true_answer);
        Toast.makeText(Question_detail.this, "Update Success",Toast.LENGTH_SHORT).show();
    }

    public void DeleteQuestion(String id_question){
        firestore.collection("Questions").document(id_question).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Question_detail.this, "Delete Success, Back to List Question to Test",Toast.LENGTH_SHORT).show();
                        Log.w("Debug", "Delete Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Question_detail.this, "Delete Fail",Toast.LENGTH_SHORT).show();
                        Log.w("Debug", "Error adding document", e);
                    }
                });

    }

}