package com.mandywebdesign.impromptu.Models;

public class PojoQuestion {

    String Question,Answer;

    public PojoQuestion(String question, String answer) {
        Question = question;
        Answer = answer;

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
