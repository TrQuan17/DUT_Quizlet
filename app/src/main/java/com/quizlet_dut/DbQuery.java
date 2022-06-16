package com.quizlet_dut;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.quizlet_dut.Models.CategoryModel;
import com.quizlet_dut.Models.QuestionModel;
import com.quizlet_dut.Models.RankModel;
import com.quizlet_dut.Models.TestModel;
import com.quizlet_dut.Models.ProfileModel;

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

    public static List<RankModel> g_userList = new ArrayList<>();
    public static int g_userCount = 0;
    public static boolean isMeOnTopList = false;

    public static RankModel myPerformance = new RankModel("NULL",0, -1);

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

                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        myPerformance.setName(documentSnapshot.getString("NAME"));
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

<<<<<<< HEAD
    public static void loadMyScores(MyCompeleteListenner compeleteListenner) {
        g_firestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_testList.size(); i++) {
                            int top = 0;
                            if(documentSnapshot.get(g_testList.get(i).getTestID()) != null) {
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }

                            g_testList.get(i).setTopScore(top);
                        }

                        compeleteListenner.onSuccess();
=======
    public static void getTopUsers(final MyCompeleteListenner compeleteListenner) {
        g_userList.clear();

        String myUID = FirebaseAuth.getInstance().getUid();
        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots
                             ) {
                            g_userList.add(new RankModel(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));
                            if(myUID.compareTo(doc.getId()) == 0) {
                                isMeOnTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;
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
    public static void getUsersCount(final MyCompeleteListenner compeleteListenner) {
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_userCount = documentSnapshot.getLong("COUNT").intValue();
                        compeleteListenner.onSuccess();

>>>>>>> develop
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
<<<<<<< HEAD
=======

>>>>>>> develop
                        compeleteListenner.onFailure();
                    }
                });
    }

    public static void saveResult(int score, MyCompeleteListenner compeleteListenner) {
        WriteBatch batch = g_firestore.batch();

        DocumentReference userDoc = g_firestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid());

        batch.update(userDoc, "TOTAL_SCORE", score);

        if(score > g_testList.get(g_selected_test_index).getTopScore()) {

            DocumentReference scoreDoc = userDoc.collection("USER_DATA")
                    .document("MY_SCORES");
            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(),score);
            batch.set(scoreDoc, testData, SetOptions.merge());
        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(score > g_testList.get(g_selected_test_index).getTopScore()) {
                            g_testList.get(g_selected_test_index).setTopScore(score);
                        }

                        myPerformance.setScore(score);
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
                getUserData(new MyCompeleteListenner() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(myCompeleteListenner);
                    }

                    @Override
                    public void onFailure() {

                        myCompeleteListenner.onFailure();
                    }
                });
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
