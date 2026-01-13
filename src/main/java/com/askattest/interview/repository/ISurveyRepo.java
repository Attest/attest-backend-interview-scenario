package com.askattest.interview.repository;

import com.askattest.interview.models.Survey;

import java.util.List;
import java.util.Optional;

public interface ISurveyRepo {
    Optional<Survey> surveyById(int surveyId);
    List<Survey> listSurveys();
}
