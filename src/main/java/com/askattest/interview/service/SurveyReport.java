package com.askattest.interview.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.askattest.interview.interfaces.ISurveyReport;
import com.askattest.interview.models.Response;
import com.askattest.interview.models.Survey;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;

public class SurveyReport implements ISurveyReport {

    private final SurveyRepo surveyRepo;
    private final ResponseRepo responseRepo;

    public SurveyReport(SurveyRepo surveyRepo, ResponseRepo responseRepo) {
        this.surveyRepo = surveyRepo;
        this.responseRepo = responseRepo;
    }

    @Override
    public Map<Integer, Integer> getQuestionsAnsweredPerRespondent(int surveyId) {
        return getResponsesForSurvey(surveyId).stream()
                .collect(Collectors.groupingBy(
                        r -> r.respondent,
                        Collectors.collectingAndThen(Collectors.toList(), List::size)
                ));
    }

    @Override
    public Map<Integer, Integer> getTotalPayoutPerRespondent(int surveyId) {
        Survey survey = getSurveyOrThrow(surveyId);
        Map<Integer, Integer> questionPayoutMap = survey.questions.stream()
                .collect(Collectors.toMap(q -> q.id, q -> q.payout));

        return getResponsesForSurvey(surveyId).stream()
                .collect(Collectors.groupingBy(
                        r -> r.respondent,
                        Collectors.summingInt(r -> questionPayoutMap.getOrDefault(r.question, 0))
                ));
    }

    @Override
    public String generateSurveyReport(int surveyId) {
        Map<Integer, Integer> questionsAnswered = getQuestionsAnsweredPerRespondent(surveyId);
        Map<Integer, Integer> payouts = getTotalPayoutPerRespondent(surveyId);

        StringBuilder report = new StringBuilder();

        questionsAnswered.forEach((respondentId, answered) -> {
            int earned = payouts.getOrDefault(respondentId, 0);
            report.append(String.format("Respondent %d: Answered %d questions, Earned %d pence%n",
                    respondentId, answered, earned));
        });

        return report.toString();
    }

    // Helper Methods 

    private Survey getSurveyOrThrow(int surveyId) {
        Survey survey = surveyRepo.surveyById(surveyId);
        if (survey == null) {
            throw new IllegalArgumentException("Survey not found with ID: " + surveyId);
        }
        return survey;
    }

    private List<Response> getResponsesForSurvey(int surveyId) {
        Survey survey = getSurveyOrThrow(surveyId);
        List<Integer> questionIds = survey.questions.stream()
                .map(q -> q.id)
                .toList();
        return responseRepo.listResponses().stream()
                .filter(r -> questionIds.contains(r.question))
                .toList();
    }
}