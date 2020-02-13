package com.mandywebdesign.impromptu.Models;

import java.util.ArrayList;

public class PojoQuestion {

    String Question,Answer;
    ArrayList<String> quesID = new ArrayList<>();



    public PojoQuestion(String question, String answer, ArrayList<String> quesID1) {
        Question = question;
        Answer = answer;
        quesID = quesID1;

    }

    public ArrayList<String> getQuesID() {
        return quesID;
    }

    public void setQuesID(ArrayList<String> quesID) {
        this.quesID = quesID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
