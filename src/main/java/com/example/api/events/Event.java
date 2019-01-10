package com.example.api.events;

import lombok.*;

import java.time.LocalDateTime;

//@Data data 애너테이션을 사용하는 것은 권하지 않는다.
//아래 작성된 EqualsAndHashCode 에서 of 값으로 id 속성을 쓰고 있다.
//그런데 Data 애너테이션을 쓰면 of 를 모든 속성에 대해서 사용 한 것처럼 된다. 이러면 스택오버플로우가 발생할 여지가 있다고 한다.
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id") // 애너테이션이 굉장히 많은데. 원래 Lombok 의 애너테이션을 제외한 다른 애너테이션들은 메타 애너테이션을 활용해서 줄일 수 있다.
//롬복의 애너테이션들은 아직 줄일 수 있는 방법이 없다. 이렇게 다 작성해야 한다.
public class Event {
    private Integer id;
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
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;

}
