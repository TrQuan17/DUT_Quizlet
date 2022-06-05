package com.quizlet_dut.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quizlet_dut.Add_Question;
import com.quizlet_dut.R;

import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    FirebaseFirestore firestore;

    EditText txtNumberOfTest,txtCategory;
    Button btnAddCategory,btnOpenAddQuestion;;
    String id_category = "";
    static int count = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        firestore = FirebaseFirestore.getInstance();

        txtNumberOfTest = view.findViewById(R.id.txt_NoOfTest);
        txtCategory = view.findViewById(R.id.txt_category);

        count = GetCountFromCategories();

        btnOpenAddQuestion = view.findViewById(R.id.btn_openAddQuestion);
        btnOpenAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Add_Question.class);
                view.getContext().startActivity(intent);
            }
        });

        btnAddCategory = view.findViewById(R.id.btn_addCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckEmptyInfor() == false){
                    Toast.makeText(view.getContext(), "Please insert full information",Toast.LENGTH_SHORT).show();
                }
                else if(isNumeric(txtNumberOfTest.getText().toString()) == false){
                    Toast.makeText(getActivity(), "Number of test must be int ",Toast.LENGTH_SHORT).show();
                }
                else{
                    postDataToFirestore(txtCategory.getText().toString(), Integer.parseInt(txtNumberOfTest.getText().toString()));
                    clearText();
                }

            }
        });
        return view;
    }

    // đẩy dữ liệu lên FireStore
    private void postDataToFirestore(String category, int no_of_quest){
        Map<String, Object> data = new HashMap<>();
        data.put("NAME",category);
        data.put("NO_OF_TESTS",no_of_quest);
        firestore.collection("QUIZ") //add category vào QUIZ
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        firestore.collection("QUIZ").document(documentReference.getId()).update("CAT_ID",documentReference.getId());
                        id_category = String.valueOf(documentReference.getId());
//                        Log.d("Debug", "DocumentSnapshot added with ID: " + id_category);
                        Toast.makeText(getActivity(), "Add Success",Toast.LENGTH_SHORT).show();
                        UpdateNewCategoryAndCountToCategories(id_category);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Add Fail",Toast.LENGTH_SHORT).show();
                        Log.w("Debug", "Error adding document", e);
                    }
                });
    }

    //ép một Object trở thành kiểu int
    public static int objectToInt(Object obj)
    {
        int x = Integer.parseInt(obj.toString());
        return x;
    }

    //lấy Field COUNT trong Categories rồi gán cho biến count
    public int GetCountFromCategories(){
        DocumentReference docRef = firestore.collection("QUIZ").document("Categories");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.exists()) {
                        count = objectToInt(document.getData().get("COUNT"));
//                        Log.d("Debug", "DocumentSnapshot Count of Categories: " + document.getData().get("COUNT"));
                    } else {
                        Log.d("Debug", "No such document");
                    }
                } else {
                    Log.d("Debug", "get failed with ", task.getException());
                }
            }
        });
        return count;
    }
    
    //update lại CAT_ID của category mới và tăng COUNT lên
    public void UpdateNewCategoryAndCountToCategories(String id_Cat){
        count++;
        String CAT_ID = "CAT"+String.valueOf(count)+"_ID";
//        Log.d("Debug", "DocumentSnapshot CAT_ID: "+CAT_ID);
//        Log.d("Debug", "DocumentSnapshot id_cat: "+id_Cat);
        firestore.collection("QUIZ").document("Categories").update("COUNT",count);
        firestore.collection("QUIZ").document("Categories").update(CAT_ID,id_Cat);
    }

    private void clearText(){
        txtCategory.setText("");
        txtNumberOfTest.setText("");
    }

    private boolean CheckEmptyInfor(){

        String empty = "";
        if( txtCategory.getText().toString().equals(empty) || txtNumberOfTest.getText().toString().equals(empty)) {

            return false;
        }
        return true;
    }

    // hàm kiểm tra dữ liệu nhập vào có phải là số hay không
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}