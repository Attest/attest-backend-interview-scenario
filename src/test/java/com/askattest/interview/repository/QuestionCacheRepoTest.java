package com.askattest.interview.repository;

import com.askattest.interview.models.Question;
import com.askattest.interview.models.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionCacheRepoTest {

    @Mock
    private ISurveyRepo surveyRepo;

    private QuestionCacheRepo questionCacheRepo;

    @BeforeEach
    void setUp() {
        questionCacheRepo = new QuestionCacheRepo(surveyRepo);
    }

    @Test
    void getQuestion_WhenCacheEmpty_ReturnsEmpty() {
        // When
        Optional<Question> result = questionCacheRepo.getQuestion(1);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void cacheQuestion_StoresQuestionInCache() {
        // Given
        Question question = createQuestion(1, "What is Java?", 100);

        // When
        questionCacheRepo.cacheQuestion(question);
        Optional<Question> result = questionCacheRepo.getQuestion(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().id);
        assertEquals("What is Java?", result.get().text);
        assertEquals(100, result.get().payout);
    }

    @Test
    void loadQuestionsFromSurveys_LoadsAllQuestions() {
        // Given
        Survey survey1 = createSurvey(1, Arrays.asList(
                createQuestion(1, "Q1", 100),
                createQuestion(2, "Q2", 200)
        ));
        Survey survey2 = createSurvey(2, Arrays.asList(
                createQuestion(3, "Q3", 300)
        ));
        when(surveyRepo.listSurveys()).thenReturn(Arrays.asList(survey1, survey2));

        // When
        questionCacheRepo.loadQuestionsFromSurveys();

        // Then
        assertTrue(questionCacheRepo.getQuestion(1).isPresent());
        assertTrue(questionCacheRepo.getQuestion(2).isPresent());
        assertTrue(questionCacheRepo.getQuestion(3).isPresent());
        verify(surveyRepo, times(1)).listSurveys();
    }

    @Test
    void loadQuestionsFromSurveys_OnlyLoadsOnce() {
        // Given
        Survey survey = createSurvey(1, Arrays.asList(createQuestion(1, "Q1", 100)));
        when(surveyRepo.listSurveys()).thenReturn(Arrays.asList(survey));

        // When
        questionCacheRepo.loadQuestionsFromSurveys();
        questionCacheRepo.loadQuestionsFromSurveys();
        questionCacheRepo.loadQuestionsFromSurveys();

        // Then - should only call surveyRepo once
        verify(surveyRepo, times(1)).listSurveys();
    }

    @Test
    void clear_RemovesAllCachedQuestions() {
        // Given
        Question question = createQuestion(1, "Q1", 100);
        questionCacheRepo.cacheQuestion(question);
        assertTrue(questionCacheRepo.getQuestion(1).isPresent());

        // When
        questionCacheRepo.clear();

        // Then
        assertFalse(questionCacheRepo.getQuestion(1).isPresent());
    }

    // Helper methods
    private Question createQuestion(int id, String text, int payout) {
        Question q = new Question();
        q.id = id;
        q.text = text;
        q.payout = payout;
        return q;
    }

    private Survey createSurvey(int id, List<Question> questions) {
        Survey s = new Survey();
        s.id = id;
        s.questions = questions;
        return s;
    }
}