package com.askattest.interview;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import com.askattest.interview.models.RespondentEarning;
import com.askattest.interview.repository.*;
import com.askattest.interview.service.*;

public class Main {
    public static void main(String[] args) {
        MutablePicoContainer container = new DefaultPicoContainer();

        container.addComponent(ISurveyRepo.class, SurveyRepo.class);
        container.addComponent(IResponseRepo.class, ResponseRepo.class);
        container.addComponent(IQuestionCacheRepo.class, QuestionCacheRepo.class);
        container.addComponent(IEarningService.class, EarningService.class);
        IEarningService earningService = container.getComponent(IEarningService.class);

        Map<Integer, RespondentEarning> respondentEarnings = earningService.calculateEarningsByRespondent();
        respondentEarnings.values().forEach(respondentEarning ->
                Logger.getGlobal().log(Level.INFO, respondentEarning.toString())
        );
    }
}