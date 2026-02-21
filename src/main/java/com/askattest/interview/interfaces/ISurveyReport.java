package com.askattest.interview.interfaces;

import java.util.Map;

public interface ISurveyReport {
    Map<Integer, Integer> getQuestionsAnsweredPerRespondent(int surveyId);
    Map<Integer, Integer> getTotalPayoutPerRespondent(int surveyId);
    String generateSurveyReport(int surveyId);
}