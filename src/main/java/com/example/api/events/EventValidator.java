package com.example.api.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
            //errors.reject("wrongPrices","Values fo Prices are wrong"); // reject 는 global error
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong"); // reject value 는 field error
        }

        LocalDateTime beginEvnetDateTime = eventDto.getBeginEventDateTime();
        if (beginEvnetDateTime.isAfter(eventDto.getEndEventDateTime()) ||
        beginEvnetDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        beginEvnetDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("beginEventDateTime", "wrongValue", "beginEventDateTime is wrong");
        }

        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if (closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
        closeEnrollmentDateTime.isAfter(eventDto.getBeginEventDateTime()) ||
        closeEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime())){
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong");
        }
    }

}
