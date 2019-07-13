package com.alok328.raj.unesquo.Model;

public class Quiz {
    private String Question;
    private String Answer;

    public Quiz() {
    }

    public Quiz(String question, String answer) {
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
