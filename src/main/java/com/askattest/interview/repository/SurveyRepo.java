package com.askattest.interview.repository;

import com.askattest.interview.models.Survey;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SurveyRepo {
    public List<Survey> surveys;

    public SurveyRepo() throws IOException {
        this.surveys = Arrays.asList(SurveyRepo.loadSurvey("survey.json"));
    }

    public Survey surveyById(int surveyId) {
        return surveys.stream().filter(survey -> survey.id == surveyId)
                .findFirst()
                .orElse(null);
    }

    public List<Survey> listSurveys() {
        return this.surveys;
    }

    public static Survey loadSurvey(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL filenameUrl = SurveyRepo.class.getResource("/survey-data/" + filename);
            return objectMapper.readValue(filenameUrl, Survey.class);
        } catch (IOException e) {
            throw new IOException("Could not load survey from file: " + filename, e);
        }
    }
}
