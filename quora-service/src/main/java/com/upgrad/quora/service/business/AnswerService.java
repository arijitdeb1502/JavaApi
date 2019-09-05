package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserAuthTokenDao userAuthTokenDao;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private AdminService adminService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String questionId,AnswerEntity answerEntity,String accessToken) throws UserNotFoundException,AuthorizationFailedException, InvalidQuestionException {
            AnswerEntity answer =null;
            UserEntity userEntity = adminService.getUserMethod(accessToken,"createAnswer");
            QuestionEntity questionEntity=questionService.getQuestion(questionId);

            if(questionEntity == null){
                throw new InvalidQuestionException("QUES-001","The question entered is invalid");
            } else {
                answerDao.updateQuestion(questionEntity);
                final ZonedDateTime now = ZonedDateTime.now();
                answerEntity.setQuestionID(questionEntity.getId());
                answerEntity.setAnswerAt(now);
                answerEntity.setUserId(userEntity.getId());
                answer=answerDao.createAnswer(answerEntity);

            }
             return answer;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerResp(String ansId,String accessToken,String newAnswerContent) throws UserNotFoundException,AuthorizationFailedException,AnswerNotFoundException{

        UserEntity userEntity = adminService.getUserMethod(accessToken,"editAnswer");
        AnswerEntity answer=answerDao.getAnswerById(ansId);
        AnswerEntity editedAns=null;

         if(answer==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        } else{

            if(userEntity.getId()!=answer.getUserId()){
                throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
            } else if(userEntity.getId()==answer.getUserId()){
                answer.setAnswer(newAnswerContent);
                editedAns=answerDao.updateAnswer(answer);

            }


        }

        return editedAns;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAnswer(String answerId,String accessToken) throws UserNotFoundException,AuthorizationFailedException,AnswerNotFoundException{

        UserEntity userEntity = adminService.getUserMethod(accessToken,"deleteAnswer");
        AnswerEntity answer = answerDao.getAnswerById(answerId);
        String deletedAnswerUuid=null;
        if(answer==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }  else if(!userEntity.getRole().equals("ADMIN") && answer.getUserId()!=userEntity.getId()){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
        }else{
            deletedAnswerUuid=answerDao.deleteAnswerById(answer.getUuid());
        }

        return deletedAnswerUuid;
    }

    public List<AnswerEntity> getAllAnswerByQuestionID(String uuid) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionService.getQuestion(uuid);

        return answerDao.getAllAnswerByQuestionID(questionEntity.getId());



    }
}
