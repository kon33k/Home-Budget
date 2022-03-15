package com.kon.budget.validator.filters;

import com.kon.budget.enums.FilterSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class FilterStrategy {

     /*
     Strategia to behawioralny wzorzec projektowy pozwalający
       zdefiniować rodzinę algorytmów, umieścić je w osobnych
      klasach i uczynić obiekty tych klas wymienialnymi
     */

    private final Map<String, FilterParametersValidator> allValidators;


    public void checkFilterForSpecification(Map<String, String> filter, FilterSpecification filterSpecification) {
        allValidators.get(filterSpecification.getForValidator())
                .assertFilter(filter);
    }
}
