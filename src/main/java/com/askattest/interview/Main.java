package com.askattest.interview;

import com.askattest.interview.models.Question;
import com.askattest.interview.models.Response;
import com.askattest.interview.models.Summary;
import com.askattest.interview.models.Survey;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        SurveyRepo surveys;
        ResponseRepo responses;
        HashMap<Integer, Summary> summaries = new HashMap<>();
        try {
            surveys = new SurveyRepo();
            responses = new ResponseRepo();

            HashMap<Integer, Question> questions = new HashMap<>();
            for(Survey survey: surveys.listSurveys()){
                for(Question question: survey.questions){
                    questions.put(question.id, question);
                }
            }
            for(Response response: responses.listResponses()){
                Question question = questions.get(response.question);
                Summary summary =
                        summaries.getOrDefault(response.respondent, new Summary(response.respondent));
                summary.increment(question.payout);
                summaries.put(response.respondent, summary);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        Logger.getGlobal().log(Level.INFO, surveys.surveyById(200).toString());
        Logger.getGlobal().log(Level.INFO, responses.responsesByRespondent(300).toString());
        Logger.getGlobal().log(Level.INFO, summaries.get(300).toString());
    }
}