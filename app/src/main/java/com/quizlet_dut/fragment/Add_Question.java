package com.quizlet_dut.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quizlet_dut.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Question extends AppCompatActivity {
    FirebaseFirestore firestore;
    List<String> listCategory = new ArrayList<>();
    List<String> listCAT_ID = new ArrayList<>(); // lưu lại CAT_ID của Category
    List<String> listNO_OF_TESTS = new ArrayList<>(); // lưu lại NO_OF_TESTS của Category
    List<String> listTEST_ID ; // lưu lại TEST_ID của TESTS_INFO tương ứng
    EditText txtA,txtB,txtC,txtD,txtAnswer,txtQuestion;
    Button btnAddQuestion;


    private SmartMaterialSpinner<String> spinnerCategory;
    private SmartMaterialSpinner<String> spinnerTEST_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        firestore = FirebaseFirestore.getInstance();

        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerTEST_ID = findViewById(R.id.spinner_testID);
        txtA = findViewById(R.id.txt_A);
        txtB = findViewById(R.id.txt_B);
        txtC = findViewById(R.id.txt_C);
        txtD = findViewById(R.id.txt_D);
        txtAnswer = findViewById(R.id.txt_answer);
        txtQuestion = findViewById(R.id.txt_question);



        btnAddQuestion = findViewById(R.id.btn_AddQuestion);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String A = txtA.getText().toString();
                String B = txtB.getText().toString();
                String C = txtC.getText().toString();
                String D = txtD.getText().toString();
                String question = txtQuestion.getText().toString();

                if(CheckEmptyInfor() == false){
                    Toast.makeText(Add_Question.this, "Please insert full information",Toast.LENGTH_SHORT).show();
                }
                else if(isNumeric(txtAnswer.getText().toString()) == false){
                    Toast.makeText(Add_Question.this, "Answer must be int and value between(1-4)",Toast.LENGTH_SHORT).show();
                }
                else if((isNumeric(txtAnswer.getText().toString()) == true) && ((Integer.valueOf(txtAnswer.getText().toString()) > 4)
                        || (Integer.valueOf(txtAnswer.getText().toString()) < 1))){
                    Toast.makeText(Add_Question.this, "Answer must be int and value between(1-4)",Toast.LENGTH_SHORT).show();
                }
                else if((spinnerCategory.getSelectedItemPosition() == -1) || (spinnerTEST_ID.getSelectedItemPosition() == -1)){
                    Toast.makeText(Add_Question.this, "U have to choose Category and Test",Toast.LENGTH_SHORT).show();
                }
                else{
                    int answer = Integer.parseInt(txtAnswer.getText().toString());

                    int indexOfCategoryChoosen =  listCategory.indexOf(spinnerCategory.getSelectedItem());
                    String category = listCAT_ID.get(indexOfCategoryChoosen);
                    String test = spinnerTEST_ID.getSelectedItem();

                    postQuestion_ToFirestore(A,B,C,D,question,answer,test,category);
                }
            }
        });

        loadSpinnerCategory();

    }
    private void clearText(){
        txtA.setText("");
        txtB.setText("");
        txtC.setText("");
        txtD.setText("");
        txtAnswer.setText("");
        txtQuestion.setText("");
    }

    // hàm kiểm tra dữ liệu nhập vào có phải là số hay không
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
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

    private void loadSpinnerCategory(){
        firestore.collection("QUIZ")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("NAME") != null)
                                {
                                    listCategory.add(document.getString("NAME"));
                                    spinnerCategory.setItem(listCategory);
                                    listCAT_ID.add( document.getId());// lưu lại CAT_ID của Category
                                    listNO_OF_TESTS.add(document.get("NO_OF_TESTS").toString());
                                }
                                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                                        Toast.makeText(Add_Question.this, listCategory.get(position),Toast.LENGTH_SHORT).show();
//                                        Log.d("Debug", "Choose Category: " + position +", "+listCategory.get(position));
//                                        Log.d("Debug", "Choose Category: " + listCAT_ID.get(position));
//                                        Log.d("Debug", "Choose Category: " + listNO_OF_TESTS.get(position));
                                        loadSpinnerTESTS_INFO(listCAT_ID.get(position),Integer.parseInt(listNO_OF_TESTS.get(position)));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void loadSpinnerTESTS_INFO(String id_cat,int no_of_tests) {
        listTEST_ID = new ArrayList<>();
        DocumentReference docRef = firestore.collection("QUIZ").document(id_cat).collection("TESTS_LIST").document("TESTS_INFO");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.exists()) {
                        for(int i =1; i<= no_of_tests;i++){
                            String id = document.getString("TEST" + String.valueOf(i) + "_ID");
                            listTEST_ID.add(id);
                        }
                        spinnerTEST_ID.setItem(listTEST_ID);
//                        Log.d("Debug", "Document CAT(i)_ID:" + id_cat);
//                        Log.d("Debug", "Document NO_OF_TESTS :" + no_of_tests);
//                        Log.d("Debug", "Document TEST_ID :" + listTEST_ID);
                    } else {
                        Log.d("Debug", "No such document");
                    }
                } else {
                    Log.d("Debug", "get failed with ", task.getException());
                }
            }
        });
    }

    // đẩy dữ liệu lên FireStore
    private void postQuestion_ToFirestore(String A, String B,String C,String D,String question,int answer,String test,String category){

        Map<String, Object> data = new HashMap<>();
        data.put("A",A);
        data.put("B",B);
        data.put("C",C);
        data.put("D",D);
        data.put("ANSWER",answer);
        data.put("QUESTION",question);
        data.put("TEST",test);
        data.put("CATEGORY",category);
        firestore.collection("Questions") //add category vào QUIZ
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.w("Debug", "Add Success");
                        Toast.makeText(Add_Question.this, "Add New QuestionAdd Success",Toast.LENGTH_SHORT).show();
                        clearText();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error adding document", e);
                        Toast.makeText(Add_Question.this, "Add New QuestionAdd Fail",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}