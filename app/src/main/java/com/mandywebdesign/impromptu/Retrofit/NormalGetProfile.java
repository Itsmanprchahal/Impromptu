package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NormalGetProfile {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Question {

        @SerializedName("question_id: ")
        @Expose
        private Integer questionId;
        @SerializedName("question: ")
        @Expose
        private String question;
        @SerializedName("answer: ")
        @Expose
        private String answer;

        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

    }
    public class Datum {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("age")
        @Expose
        private String age;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @SerializedName("gender")
        @Expose
        private String gender;

        @SerializedName("rating_points")
        @Expose
        private  String rating_points;

        public String getRating_points() {
            return rating_points;
        }

        public void setRating_points(String rating_points) {
            this.rating_points = rating_points;
        }

        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("question")
        @Expose
        private List<Question> question = null;
        @SerializedName("image")
        @Expose
        private String image;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public List<Question> getQuestion() {
            return question;
        }

        public void setQuestion(List<Question> question) {
            this.question = question;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

}
