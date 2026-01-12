package com.askattest.interview.models;

public class RespondentEarning {
    private int respondent;
    private int amount;
    private int answers;
    public RespondentEarning(int respondent){
        this.respondent = respondent;
        this.amount = 0;
        this.answers = 0;
    }
    public void increment(int amount){
        this.amount += amount;
        this.answers++;
    }
    public int amount(){
        return this.amount;
    }
    public int answers(){
        return this.answers;
    }
    @Override
    public String toString() {
        return String.format("[Respondent: %d earned %d pences for their %d answers.]", this.respondent, this.amount, this.answers);
    }
}
