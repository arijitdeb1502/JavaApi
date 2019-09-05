package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    AdminService adminService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, QuestionRequest questionRequest) throws UserNotFoundException, AuthorizationFailedException {
        QuestionResponse questionResponse = new QuestionResponse();
        final UserEntity userEntity = adminService.getUserMethod(authorization,"createQuestion");
        String questionId = questionService.createQuestion(questionRequest.getContent(), userEntity);
        questionResponse.setStatus("QUESTION CREATED");
        questionResponse.setId(questionId);
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionEntity>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {
        ArrayList<QuestionDetailsResponse> questionResponse = new ArrayList<QuestionDetailsResponse>();
        final UserEntity userEntity = adminService.getUserMethod(authorization,"getAllQuestions");
        List<QuestionEntity> questionEntities = questionService.getAllQuestion();
        return new ResponseEntity<List<QuestionEntity>>(questionEntities, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@PathVariable("questionId") String questionId, @RequestHeader("authorization") final String authorization, QuestionEditRequest questionEditRequest) throws UserNotFoundException, AuthorizationFailedException, InvalidQuestionException {
        QuestionEditResponse questionEditResponse = new QuestionEditResponse();
        final UserEntity userEntity = adminService.getUserMethod(authorization,"editQuestionContent");
        QuestionEntity questionEntity = questionService.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (questionEntity.getUserId() != userEntity.getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        boolean isUpdated = questionService.editQuestion(questionId, questionEditRequest.getContent());
        if (!isUpdated) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        questionEditResponse.setStatus("QUESTION EDITED");
        questionEditResponse.setId(questionId);
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") String questionId, @RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException, InvalidQuestionException {
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        final UserEntity userEntity = adminService.getUserMethod(authorization,"deleteQuestion");
        QuestionEntity questionEntity = questionService.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (questionEntity.getUserId() != userEntity.getId() || userEntity.getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        questionService.deleteQuestion(questionId);
        questionDeleteResponse.setId(questionId);
        questionDeleteResponse.setStatus("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionEntity>> getAllQuestions(@PathVariable("userId")String userId,@RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {
        ArrayList<QuestionDetailsResponse> questionResponse = new ArrayList<QuestionDetailsResponse>();
        final UserEntity userEntity = adminService.getUserMethod(authorization,"getAllQuestionsByUser");
        List<QuestionEntity> questionEntities = questionService.getAllQuestionBYUser(userEntity.getId());
        return new ResponseEntity<List<QuestionEntity>>(questionEntities, HttpStatus.OK);
    }
}
