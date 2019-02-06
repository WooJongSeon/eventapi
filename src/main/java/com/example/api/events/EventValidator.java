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

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime(); // 이벤트 끝나는 날
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||  // 이벤트 시작
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||  // 예약 종료
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) { // 예약 시작
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong"); // reject value 는 field error
        }

        LocalDateTime beginEvnetDateTime = eventDto.getBeginEventDateTime();
        if (beginEvnetDateTime.isAfter(eventDto.getEndEventDateTime()) ||
        beginEvnetDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        beginEvnetDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("beginEventDateTime", "wrongValue", "beginEventDateTime is wrong");
        }

        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if (closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) || // 예약 종료일이 예약 시작일 이후
        closeEnrollmentDateTime.isAfter(eventDto.getBeginEventDateTime()) || // 예약 종료일이 이벤트 시작일 이후
        closeEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime())){ // 예약 종료일이 이벤트 종료일 이후
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong");
        }
    }

}
