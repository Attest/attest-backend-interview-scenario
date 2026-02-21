package com.askattest.interview;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.askattest.interview.interfaces.ISurveyReport;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;
import com.askattest.interview.service.SurveyReport;

public class Main {
    public static void main(String[] args) {
        SurveyRepo surveys;
        ResponseRepo responses;
        try {
            surveys = new SurveyRepo();
            responses = new ResponseRepo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ISurveyReport surveyReport = new SurveyReport(surveys, responses);

        Logger.getGlobal().log(Level.INFO, surveys.surveyById(200).toString());
        Logger.getGlobal().log(Level.INFO, responses.responsesByRespondent(300).toString());
        Logger.getGlobal().log(Level.INFO, surveyReport.generateSurveyReport(200));
    }

}