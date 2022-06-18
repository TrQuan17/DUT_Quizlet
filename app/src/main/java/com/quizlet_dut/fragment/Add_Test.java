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

public class Add_Test extends AppCompatActivity {
    FirebaseFirestore firestore;

    List<String> listCategory = new ArrayList<>();
    List<String> listCAT_ID = new ArrayList<>(); // lưu lại CAT_ID của Category
    List<String> listNO_OF_TESTS = new ArrayList<>(); // lưu lại NO_OF_TESTS của Category

    EditText txtTestID,txtTime;
    Button btnAddTest;
    private SmartMaterialSpinner<String> spinnerCategory1;

    static long number_of_test_InTestInfo = 0;
    static long NO_OF_TESTS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        firestore = FirebaseFirestore.getInstance();

        spinnerCategory1 = findViewById(R.id.spinner_category_1);
        txtTestID = findViewById(R.id.txt_TestID);
        txtTime = findViewById(R.id.txt_Time);

        loadSpinnerCategory1();

        btnAddTest = findViewById(R.id.btn_AddTest);
        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckEmptyInfor() == false){
                    Toast.makeText(Add_Test.this, "Please insert full information ",Toast.LENGTH_SHORT).show();
                }
                else if(isNumeric(txtTime.getText().toString()) == false){
                    Toast.makeText(Add_Test.this, "Time must be int ",Toast.LENGTH_SHORT).show();
                }
                else if(spinnerCategory1.getSelectedItemPosition() == -1){
                    Toast.makeText(Add_Test.this, "U have to choose Category",Toast.LENGTH_SHORT).show();
                }
                else{
                    int indexOfCategoryChoosen =  listCategory.indexOf(spinnerCategory1.getSelectedItem());
                    String category_id = listCAT_ID.get(indexOfCategoryChoosen);

                    String test_id = txtTestID.getText().toString();
                    long time = Integer.parseInt(txtTime.getText().toString());

                    postTest_ToFirestore(category_id,test_id,time);
                }
            }
        });
    }

    private void clearText(){
        txtTestID.setText("");
        txtTime.setText("");
    }

    // hàm kiểm tra dữ liệu nhập vào có phải là số hay không?
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean CheckEmptyInfor(){

        String empty = "";
        if( txtTestID.getText().toString().equals(empty) || txtTime.getText().toString().equals(empty)) {

            return false;
        }
        else{
            return true;
        }
    }


    // đẩy dữ liệu lên FireStore
    private void postTest_ToFirestore(String category_id,String test_id,long time){
        if(NO_OF_TESTS == 0){
            NO_OF_TESTS ++;
            String Test_ID = "TEST"+String.valueOf(NO_OF_TESTS)+"_ID";
            String Test_Time=  "TEST"+String.valueOf(NO_OF_TESTS)+"_TIME";
            Map<String, Object> data = new HashMap<>();
            data.put(Test_ID,test_id);
            data.put(Test_Time,time);

            //thêm Test mới vào TESTS_INFO
            firestore.collection("QUIZ").document(category_id).collection("TESTS_LIST").document("TESTS_INFO").set(data);

            //dòng tiếp theo update NO_OF_TESTS tăng thêm 1 tương ứng khi thêm 1 Test mới
            firestore.collection("QUIZ").document(category_id).update("NO_OF_TESTS",NO_OF_TESTS);

            clearText();
            Toast.makeText(Add_Test.this, "Add TEST Success",Toast.LENGTH_SHORT).show();
        }
        else{
            NO_OF_TESTS ++;
            String Test_ID = "TEST"+String.valueOf(NO_OF_TESTS)+"_ID";
            String Test_Time=  "TEST"+String.valueOf(NO_OF_TESTS)+"_TIME";
            Map<String, Object> data = new HashMap<>();
            data.put(Test_ID,test_id);
            data.put(Test_Time,time);

            //thêm Test mới vào TESTS_INFO
            firestore.collection("QUIZ").document(category_id).collection("TESTS_LIST").document("TESTS_INFO").update(data);

            //dòng tiếp theo update NO_OF_TESTS tăng thêm 1 tương ứng khi thêm 1 Test mới
            firestore.collection("QUIZ").document(category_id).update("NO_OF_TESTS",NO_OF_TESTS);

            clearText();
            Toast.makeText(Add_Test.this, "Add TEST Success",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpinnerCategory1(){
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
                                    spinnerCategory1.setItem(listCategory);
                                    listCAT_ID.add( document.getId());// lưu lại CAT_ID của Category
                                    listNO_OF_TESTS.add(document.get("NO_OF_TESTS").toString());
                                }
                                spinnerCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        Get_NumberOfTestInfo(listCAT_ID.get(position));
                                        NO_OF_TESTS =  Long.parseLong(listNO_OF_TESTS.get(position));
//                                        Log.d("Debug", "Choose Category: " + position +", "+listCategory.get(position));
//                                        Log.d("Debug", "Cat id: " + listCAT_ID.get(position));
//                                        Log.d("Debug", "NO_of_TESTS: " + listNO_OF_TESTS.get(position));

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

    //ép một Object trở thành kiểu int
    public static int objectToInt(Object obj)
    {
        int x = Integer.parseInt(obj.toString());
        return x;
    }

    private long Get_NumberOfTestInfo(String cat_id){
        DocumentReference docRef = firestore.collection("QUIZ").document(cat_id).collection("TESTS_LIST").document("TESTS_INFO");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.exists()) {
                        number_of_test_InTestInfo = objectToInt(document.getData().size())/2;
                    } else {
                        Log.d("Debug", "No such document");
                    }
                } else {
                    Log.d("Debug", "get failed with ", task.getException());
                }
            }
        });
        return number_of_test_InTestInfo;
    }
}