package com.kon.budget.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
class ValidatorMessage {

    /**
    * tworzy wiadomosci i kod errora
    */

    private List<String> message = new ArrayList<>();
    private List<String> code = new ArrayList<>();
}
