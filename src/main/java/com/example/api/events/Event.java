package com.example.api.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Data data 애너테이션을 사용하는 것은 권하지 않는다.
//아래 작성된 EqualsAndHashCode 에서 of 값으로 id 속성을 쓰고 있다.
//그런데 Data 애너테이션을 쓰면 of 를 모든 속성에 대해서 사용 한 것처럼 된다. 이러면 스택오버플로우가 발생할 여지가 있다고 한다.
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id") // 애너테이션이 굉장히 많은데. 원래 Lombok 의 애너테이션을 제외한 다른 애너테이션들은 메타 애너테이션을 활용해서 줄일 수 있다.
//롬복의 애너테이션들은 아직 줄일 수 있는 방법이 없다. 이렇게 다 작성해야 한다.
@Entity
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Enumerated(EnumType.STRING) // 기본 타입은 EnumType.Ordinal 인데 String 으로 바꿔주는 것이 좋다.
    private EventStatus eventStatus = EventStatus.DRAFT; // Enum 클래스 안의 데이터의 순서가 바뀌게 되면 데이터에 문제가 생긴다.
    //그래서 순서가 바뀌어도 문제 되지 않는 String 을 사용한다.

    public void update(){
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        }else{
            this.free = false;
        }
        if (this.location == null || this.location.isBlank()) { // isBlank() java 11 에서 추가된 기능 , 공백문자열을 확인해서 비어있는지 체크한다.
            this.offline = false;
        }
        else {
            this.offline = true;
        }
    }

}
