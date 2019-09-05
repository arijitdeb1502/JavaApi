package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name ="ANSWER")
@NamedQueries(
        {
                @NamedQuery(name = "answerById", query = "select u from AnswerEntity u where u.uuid = :id"),
                @NamedQuery(name = "getAllAnswerQuestionId", query = "select u from AnswerEntity u where u.QuestionID = :id")

        })
public class AnswerEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "ANS")
    @NotNull
    @Size(max = 255)
    private String answer;

    @Column(name = "DATE")
    private ZonedDateTime answerAt;


    @NotNull
    @Column(name = "USER_ID")
    private long userId;

    @NotNull
    @Column(name = "QUESTION_ID")
    private long QuestionID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ZonedDateTime getAnswerAt() {
        return answerAt;
    }

    public void setAnswerAt(ZonedDateTime answerAt) {
        this.answerAt = answerAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(long questionID) {
        QuestionID = questionID;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
