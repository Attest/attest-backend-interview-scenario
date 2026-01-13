package com.askattest.interview.repository;

import com.askattest.interview.models.Question;
import java.util.Optional;

public interface IQuestionCacheRepo {
    Optional<Question> getQuestion(int questionId);
    void cacheQuestion(Question question);
    void loadQuestionsFromSurveys();
    void clear();
}