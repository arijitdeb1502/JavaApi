package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class QuestionDao {


    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void createQuestion(QuestionEntity questionEntity){


            entityManager.persist(questionEntity);

    }

    public List<QuestionEntity> getAllQuetion(){


        TypedQuery<QuestionEntity> query =entityManager.createNamedQuery("getAllQuestions",QuestionEntity.class);
        List<QuestionEntity> resultList = query.getResultList();

        return  resultList;
    }
    @Transactional
    public boolean editQuestion(String questionId,String content) {

        Query query = entityManager.createQuery("UPDATE QuestionEntity q SET q.content =:content "
                + "WHERE q.uuid = :questionId");
        query.setParameter("content", content);
        query.setParameter("questionId", questionId);
        int rowsUpdated = query.executeUpdate();

        if(rowsUpdated>0){
            return true;
        }
        else{
            return false;

        }

    }

    public QuestionEntity getQuestion(String uuid){
        try {
            TypedQuery<QuestionEntity> query = entityManager.createQuery("SELECT q FROM QuestionEntity q WHERE q.uuid = :uuid", QuestionEntity.class);
            query.setParameter("uuid", uuid);
            return query.getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
    @Transactional
    public boolean deleteQuestion(String uuid){
        Query query=entityManager.createQuery("DELETE FROM  QuestionEntity u WHERE u.uuid = :ids");
        query.setParameter("ids", uuid);
        int result= query.executeUpdate();
        if(result>0){
            return true;
        }
        return false;
    }

    public List<QuestionEntity> getAllQuestionBYUser(long uuid){

        try {
            TypedQuery<QuestionEntity> query = entityManager.createNamedQuery("getAllQuestionByUSerId", QuestionEntity.class);
            query.setParameter("id", uuid);
            List<QuestionEntity> resultList = query.getResultList();

            return resultList;
        }
        catch (NoResultException e){
            return null;
        }
    }
}
