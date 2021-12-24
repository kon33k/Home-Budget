package com.kon.budget.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class ValidatorMessage {
/*
    klasy IncomeDataValidator AmountValidator  Interface Validator    słuza do wyswietlenia wszytkich blednie wpsanych
    w formularzu
    */

    private List<String> message = new ArrayList<>();
    private List<String> code = new ArrayList<>();

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}
