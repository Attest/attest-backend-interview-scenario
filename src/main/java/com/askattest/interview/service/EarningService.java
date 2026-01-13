package com.askattest.interview.service;

import com.askattest.interview.models.RespondentEarning;
import com.askattest.interview.repository.IQuestionCacheRepo;
import com.askattest.interview.repository.IResponseRepo;
import java.util.HashMap;
import java.util.Map;

public class EarningService implements IEarningService {
    private final IResponseRepo responseRepo;
    private final IQuestionCacheRepo questionCache;

    public EarningService(IResponseRepo responseRepo, IQuestionCacheRepo questionCache) {
        this.responseRepo = responseRepo;
        this.questionCache = questionCache;
    }

    @Override
    public Map<Integer, RespondentEarning> calculateEarningsByRespondent() {
        // Load questions into cache if not already loaded
        questionCache.loadQuestionsFromSurveys();

        Map<Integer, RespondentEarning> respondentEarnings = new HashMap<>();

        responseRepo.listResponses().forEach(response -> {
            questionCache.getQuestion(response.question).ifPresent(question -> {
                respondentEarnings.computeIfAbsent(response.respondent, RespondentEarning::new)
                        .increment(question.payout);
            });
        });

        return respondentEarnings;
    }

    @Override
    public RespondentEarning calculateEarningsForRespondent(int respondentId) {
        questionCache.loadQuestionsFromSurveys();

        RespondentEarning respondentEarning = new RespondentEarning(respondentId);

        responseRepo.responsesByRespondent(respondentId).forEach(response -> {
            questionCache.getQuestion(response.question).ifPresent(question -> {
                respondentEarning.increment(question.payout);
            });
        });

        return respondentEarning;
    }
}