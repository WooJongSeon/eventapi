package com.example.api.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class EventDto {
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // location 값이 없다면 온라인 모임
    private int basePrice; // optional
    private int maxPrice; // optional
    private int limitOfEnrollment;
}
