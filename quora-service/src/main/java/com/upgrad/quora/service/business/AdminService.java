package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserAuthTokenDao userAuthTokenDao;

    @Autowired
    private UserDao userDao;

    public UserEntity getUser(final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userAuthTokenDao.getUserAuthTokenByAccessToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt()!=null ){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        } else {
            UserEntity userEntity=userDao.getUserById(id);
            if(userEntity==null) {
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            } else {
                return userEntity;
            }

        }

    }

    public String deleteUser(final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException{

        UserAuthTokenEntity userAuthTokenEntity = userAuthTokenDao.getUserAuthTokenByAccessToken(authorization);
        String deletedUserUuid=userAuthTokenEntity.getUuid();
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt()!=null ){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        } else {


            UserEntity userEntity=userDao.getUserById(id);

            if(userEntity==null) {
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            } else {
                if(userEntity.getRole().equals("admin")){

                    userDao.deleteUserById(id);

                }else{

                    throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");

                }
            }

        }

        return deletedUserUuid;

    }

    public UserEntity getUserMethod(final String authorization,String methodName) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userAuthTokenDao.getUserAuthTokenByAccessToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("createAnswer")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("editAnswer")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("deleteAnswer")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("getAllAnswertoQuestion")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("createQuestion")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("getAllQuestions")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("editQuestionContent")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("deleteQuestion")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        else if(userAuthTokenEntity.getLogoutAt()!=null &&methodName.equals("getAllQuestionsByUser")){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }
        else {

            return userAuthTokenEntity.getUser();
        }

    }

}