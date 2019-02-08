package com.example.api.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//BeanSerializer : 필드 이름을 사용한다. 지금 필드에는 event가 있으므로 이렇게 하면 리턴하는 json 값들이 event: 의 하위 항목으로 들어간다.
//event의 하위 항목인 것처럼 json을 리턴하고 싶지 않다면 => JsonUnwrapped 를 사용한다.
public class EventResource extends Resource<Event> { // Hateoas 적용에서 추가된 코드 ( HAL 적용 )
    //@JsonUnwrapped // 있으면 리턴 값이 아래와 같다. - JsonUnwrapped 를 쓴게 파싱하기 더 편하다
    //{"id":1,"name":"Spring","description":"REST API Development with Spring","beginEnrollmentDateTime":"2018-11-23T14:21:00","closeEnrollmentDateTime":"2018-11-24T14:21:00","beginEventDateTime":"2018-11-25T14:21:00","endEventDateTime":"2018-11-26T14:21:00","location":"강남역 D2 스타텁 팩토리","basePrice":100,"maxPrice":200,"limitOfEnrollment":100,"offline":true,"free":false,"eventStatus":"DRAFT","_links":{"query-events":{"href":"http://localhost/api/events"},"self":{"href":"http://localhost/api/events/1"},"update-event":{"href":"http://localhost/api/events/1"}}}
    //없으면 아래와 같다.
    //{"event":{"id":1,"name":"Spring","description":"REST API Development with Spring","beginEnrollmentDateTime":"2018-11-23T14:21:00","closeEnrollmentDateTime":"2018-11-24T14:21:00","beginEventDateTime":"2018-11-25T14:21:00","endEventDateTime":"2018-11-26T14:21:00","location":"강남역 D2 스타텁 팩토리","basePrice":100,"maxPrice":200,"limitOfEnrollment":100,"offline":true,"free":false,"eventStatus":"DRAFT"},"_links":{"query-events":{"href":"http://localhost/api/events"},"self":{"href":"http://localhost/api/events/1"},"update-event":{"href":"http://localhost/api/events/1"}}}

    public EventResource(Event event, Link... links){
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
