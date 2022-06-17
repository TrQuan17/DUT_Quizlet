package com.quizlet_dut.Models;

public class QuestionAdd {
//    private int true_answer;
    private String test;
    private String question;
    private String category;
    private String  answer_A;
    private String  answer_B;
    private String  answer_C;
    private String  answer_D;
    private long true_answer;
    private String name_category;
    private String id_question;

    public QuestionAdd(String test, String question, String category, String answer_A, String answer_B, String answer_C, String answer_D, long true_answer, String name_category, String id_question) {
        this.test = test;
        this.question = question;
        this.category = category;
        this.answer_A = answer_A;
        this.answer_B = answer_B;
        this.answer_C = answer_C;
        this.answer_D = answer_D;
        this.true_answer = true_answer;
        this.name_category = name_category;
        this.id_question = id_question;
    }

    public String getId_question() {
        return id_question;
    }

    public void setId_question(String id_question) {
        this.id_question = id_question;
    }

    public String getAnswer_A() {
        return answer_A;
    }

    public void setAnswer_A(String answer_A) {
        this.answer_A = answer_A;
    }

    public String getAnswer_B() {
        return answer_B;
    }

    public void setAnswer_B(String answer_B) {
        this.answer_B = answer_B;
    }

    public String getAnswer_C() {
        return answer_C;
    }

    public void setAnswer_C(String answer_C) {
        this.answer_C = answer_C;
    }

    public String getAnswer_D() {
        return answer_D;
    }

    public void setAnswer_D(String answer_D) {
        this.answer_D = answer_D;
    }

    public long getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(long true_answer) {
        this.true_answer = true_answer;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
