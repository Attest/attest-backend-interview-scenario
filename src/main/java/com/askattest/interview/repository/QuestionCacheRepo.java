package com.askattest.interview.repository;

import com.askattest.interview.models.Question;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QuestionCacheRepo implements IQuestionCacheRepo {
    private final Map<Integer, Question> cache = new HashMap<>();
    private final ISurveyRepo surveyRepo;

    public QuestionCacheRepo(ISurveyRepo surveyRepo) {
        this.surveyRepo = surveyRepo;
    }

    @Override
    public Optional<Question> getQuestion(int questionId) {
        return Optional.ofNullable(cache.get(questionId));
    }

    @Override
    public void cacheQuestion(Question question) {
        cache.put(question.id, question);
    }

    @Override
    public void loadQuestionsFromSurveys() {
        if (cache.isEmpty()) {
            surveyRepo.listSurveys().stream()
                    .flatMap(survey -> survey.questions.stream())
                    .forEach(question -> cache.put(question.id, question));
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }
}