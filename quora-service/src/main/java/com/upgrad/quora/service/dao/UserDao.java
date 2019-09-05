package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {



        TypedQuery<UserEntity> query=entityManager.createNamedQuery("getAllUsers",UserEntity.class);
        List<UserEntity> resultList = query.getResultList();

        for (UserEntity user:resultList ) {

            if(user.getEmail().equals(userEntity.getEmail())){
                throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
            } else if(user.getUserName().equals(userEntity.getUserName())){
                throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
            }

        }

        entityManager.persist(userEntity);

        return  userEntity;
    }

    public UserEntity getUserByUsername(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUname" , UserEntity.class).setParameter("username" , userName).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public UserEntity getUserById(final String uuid) {
        try {
            return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("id", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    @Transactional
    public void deleteUserById(final String userId) {

        UserEntity userEntity;
        try {

            Query query=entityManager.createQuery("DELETE FROM UserEntity u WHERE u.uuid = :ids");
            query.setParameter("ids", userId);
            int result= query.executeUpdate();



        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }
}