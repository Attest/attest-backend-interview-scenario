package com.askattest.interview.service;

import com.askattest.interview.models.RespondentEarning;
import java.util.Map;

public interface IEarningService {
    Map<Integer, RespondentEarning> calculateEarningsByRespondent();
    RespondentEarning calculateEarningsForRespondent(int respondentId);
}