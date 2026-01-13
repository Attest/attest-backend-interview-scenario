package com.askattest.interview.service;

import com.askattest.interview.models.Question;
import com.askattest.interview.models.Response;
import com.askattest.interview.models.RespondentEarning;
import com.askattest.interview.repository.IQuestionCacheRepo;
import com.askattest.interview.repository.IResponseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarningServiceTest {

    @Mock
    private IResponseRepo responseRepo;

    @Mock
    private IQuestionCacheRepo questionCache;

    private EarningService earningService;

    @BeforeEach
    void setUp() {
        earningService = new EarningService(responseRepo, questionCache);
    }

    @Test
    void calculateEarningsByRespondent_ReturnsCorrectEarnings() {
        // Given
        List<Response> responses = Arrays.asList(
                createResponse(1, 101, 1), // Respondent 1, Question 101, Choice 1
                createResponse(1, 102, 2), // Respondent 1, Question 102, Choice 2
                createResponse(2, 101, 1)  // Respondent 2, Question 101, Choice 1
        );

        when(responseRepo.listResponses()).thenReturn(responses);
        when(questionCache.getQuestion(101)).thenReturn(Optional.of(createQuestion(101, 100)));
        when(questionCache.getQuestion(102)).thenReturn(Optional.of(createQuestion(102, 200)));

        // When
        Map<Integer, RespondentEarning> earnings = earningService.calculateEarningsByRespondent();

        // Then
        assertEquals(2, earnings.size());

        RespondentEarning earning1 = earnings.get(1);
        assertNotNull(earning1);
        assertEquals(300, earning1.amount()); // 100 + 200
        assertEquals(2, earning1.answers());

        RespondentEarning earning2 = earnings.get(2);
        assertNotNull(earning2);
        assertEquals(100, earning2.amount());
        assertEquals(1, earning2.answers());

        verify(questionCache, times(1)).loadQuestionsFromSurveys();
    }

    @Test
    void calculateEarningsByRespondent_SkipsResponsesWithMissingQuestions() {
        // Given
        List<Response> responses = Arrays.asList(
                createResponse(1, 101, 1),
                createResponse(1, 999, 1) // Question doesn't exist
        );

        when(responseRepo.listResponses()).thenReturn(responses);
        when(questionCache.getQuestion(101)).thenReturn(Optional.of(createQuestion(101, 100)));
        when(questionCache.getQuestion(999)).thenReturn(Optional.empty());

        // When
        Map<Integer, RespondentEarning> earnings = earningService.calculateEarningsByRespondent();

        // Then
        RespondentEarning earning = earnings.get(1);
        assertEquals(100, earning.amount()); // Only counted question 101
        assertEquals(1, earning.answers());
    }

    @Test
    void calculateEarningsByRespondent_WithNoResponses_ReturnsEmptyMap() {
        // Given
        when(responseRepo.listResponses()).thenReturn(Arrays.asList());

        // When
        Map<Integer, RespondentEarning> earnings = earningService.calculateEarningsByRespondent();

        // Then
        assertTrue(earnings.isEmpty());
    }

    @Test
    void calculateEarningsForRespondent_MultipleCallsToSameRespondent() {
        // Given
        int respondentId = 1;
        List<Response> responses = Arrays.asList(
                createResponse(1, 101, 1)
        );

        when(responseRepo.responsesByRespondent(respondentId)).thenReturn(responses);
        when(questionCache.getQuestion(101)).thenReturn(Optional.of(createQuestion(101, 100)));

        // When
        RespondentEarning earning1 = earningService.calculateEarningsForRespondent(respondentId);
        RespondentEarning earning2 = earningService.calculateEarningsForRespondent(respondentId);

        // Then
        assertEquals(earning1.amount(), earning2.amount());
        assertEquals(earning1.answers(), earning2.answers());
        verify(responseRepo, times(2)).responsesByRespondent(respondentId);
    }

    // Helper methods
    private Response createResponse(int respondentId, int questionId, int choiceId) {
        Response r = new Response();
        r.respondent = respondentId;
        r.question = questionId;
        r.choice = choiceId;
        return r;
    }

    private Question createQuestion(int id, int payout) {
        Question q = new Question();
        q.id = id;
        q.payout = payout;
        return q;
    }
}