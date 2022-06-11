package com.quizlet_dut;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.quizlet_dut.fragment.ProfileModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    // Access a Cloud Firestore instance from your Activity
    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel> g_catList = new ArrayList<>();
    public static int g_selected_cat_index = 0;

    public static List<TestModel> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;

    public static List<QuestionModel> g_quesList = new ArrayList<>();

    public static ProfileModel myProfileModel = new ProfileModel("NA", null, null);

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;

    public static void createUserData(String email, String name, MyCompeleteListenner compeleteListenner) {
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);
        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc, userData);
        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        compeleteListenner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        compeleteListenner.onFailure();
                    }
                });
    }

    public static void saveProfileData(String name, String phone, MyCompeleteListenner compeleteListenner) {
        Map<String, Object> profileData = new ArrayMap<>();
        profileData.put("NAME", name);
        if(phone != null) {
            profileData.put("PHONE", phone);
        }
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).update(profileData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                myProfileModel.setName(name);
                if(phone!= null) {
                    myProfileModel.setPhone(phone);
                }
                compeleteListenner.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                compeleteListenner.onFailure();
            }
        });
    }

    public static void getUserData(MyCompeleteListenner myCompeleteListenner){
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfileModel.setName(documentSnapshot.getString("NAME"));
                        myProfileModel.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        if(documentSnapshot.getString("PHONE") != null) {
                            myProfileModel.setPhone(documentSnapshot.getString("PHONE"));
                        }

                        myCompeleteListenner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompeleteListenner.onFailure();
                    }
                });
    }

    public static void loadCategories(final MyCompeleteListenner myCompeleteListenner){
        g_catList.clear();

        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc :queryDocumentSnapshots){
                            docList.put(doc.getId(),  doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long catCount = catListDoc.getLong("COUNT");


                        for (int i = 1; i <= catCount; i++) {
                            String catID = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catID);

                            int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();


                            String catName = catDoc.getString("NAME");

                            g_catList.add(new CategoryModel(catID, catName, noOfTest));
                        }
                        myCompeleteListenner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompeleteListenner.onFailure();
                    }
                });
    }

    public static void loadQuestions(final MyCompeleteListenner compeleteListenner) {
        g_quesList.clear();
        g_firestore.collection("Questions")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocID())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_quesList.add(new QuestionModel(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    NOT_VISITED
                            ));
                        }

                        compeleteListenner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        compeleteListenner.onFailure();
                    }
                });
    }

    public static void loadData(final MyCompeleteListenner myCompeleteListenner){
        loadCategories(new MyCompeleteListenner() {
            @Override
            public void onSuccess() {
                getUserData(myCompeleteListenner);
            }

            @Override
            public void onFailure() {
                myCompeleteListenner.onFailure();
            }
        });
    }

    public static void loadTestData(final MyCompeleteListenner myCompeleteListenner){
        g_testList.clear();

        g_firestore.collection("QUIZ").document(g_catList.get(g_selected_cat_index).getDocID())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTest = g_catList.get(g_selected_cat_index).getNoOfTests();

                        for (int i = 1;  i <= noOfTest; i++){
                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()
                            ));
                        }


                        myCompeleteListenner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompeleteListenner.onFailure();
                    }
                });
    }


}
