package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {

        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public void updateQuestion(final QuestionEntity updatedQuestionEntity) {
        entityManager.merge(updatedQuestionEntity);
    }

    public AnswerEntity getAnswerById(String id) {
        try {
            return entityManager.createNamedQuery("answerById", AnswerEntity.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity updateAnswer(final AnswerEntity updatedAnswerEntity) {
       return entityManager.merge(updatedAnswerEntity);
    }

    @Transactional
    public String deleteAnswerById(String answerUuId) {
        try {

            Query query=entityManager.createQuery("DELETE FROM AnswerEntity u WHERE u.uuid = :answerUuId");
            query.setParameter("answerUuId", answerUuId);
            int result= query.executeUpdate();



        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return answerUuId;
    }
    public List<AnswerEntity> getAllAnswerByQuestionID(long uuid){

        try {
            TypedQuery<AnswerEntity> query = entityManager.createNamedQuery("getAllAnswerQuestionId", AnswerEntity.class);
            query.setParameter("id", uuid);
            List<AnswerEntity> resultList = query.getResultList();

            return resultList;
        }
        catch (NoResultException e){
            return null;
        }
    }

}
