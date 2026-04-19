package com.askattest.interview;

import com.askattest.interview.models.Response;
import com.askattest.interview.models.Survey;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;

import java.io.IOException;
import java.util.List;
import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        SurveyRepo surveysRepo;
        ResponseRepo responsesRepo;
        try {
            surveysRepo = new SurveyRepo();
            responsesRepo = new ResponseRepo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        Logger.getGlobal().log(Level.INFO, surveysRepo.surveyById(200).toString());
//        Logger.getGlobal().log(Level.INFO, responsesRepo.responsesByRespondent(300).toString());

        Survey survey1 = surveysRepo.surveyById(200);
        if (survey1 == null) {
            throw new RuntimeException("Survey with ID 200 not found");
        }

        List<Response> responses = responsesRepo.listResponses();

        Set<Integer> questionIds = survey1.questions.stream()
                .map(q -> q.id).collect(Collectors.toSet());

        var survey1Responses = responses.stream()
                .filter(r -> questionIds.contains(r.question))
                .toList();

        System.out.println("Task1");

        survey1Responses.stream()
                .collect(Collectors.groupingBy(r -> r.respondent, Collectors.counting()))
                .forEach((key, value) -> System.out.printf("Respondent %d: %d questions%n", key, value));

        System.out.println("\nTask2");

        var questionsPayout = survey1.questions.stream()
                .collect(Collectors.toMap(q -> q.id, q -> q.payout));

        survey1Responses.stream()
                .collect(Collectors.groupingBy(r -> r.respondent,
                        Collectors.summingInt(r -> questionsPayout.getOrDefault(r.question, 0))))
                .forEach((key, value) -> System.out.printf("Respondent %d: %d pence%n", key, value));
    }
}