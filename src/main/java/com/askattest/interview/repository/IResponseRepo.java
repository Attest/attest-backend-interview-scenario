package com.askattest.interview.repository;

import com.askattest.interview.models.Response;

import java.util.List;

public interface IResponseRepo {
    List<Response> responsesByRespondent(int respondentId);
    List<Response> responsesByRespondentAndQuestion(int respondentId, int questionId);
    List<Response> listResponses();
}
