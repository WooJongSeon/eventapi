package com.example.api.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
//이렇게 위에 작성하면 아래에 uri 를 적지 않아도 된다.
//produces 부분에 따라서 아래에 있는 모든 메소드는 MediaType HAL JSON 타입의 값을 리턴하게 된다. -> HAL 이라는 스펙을 따르는 UTF8 인코딩 형식의 json 값을 리턴으로 주겠다.
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository , ModelMapper modelMapper, EventValidator eventValidator){ // AutoWired 대신에 이렇게 작성 할 수도 있다.
        //컨트롤러의 생성자를 만들고 객체를 담는다.
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){// 모델매퍼를 활용해서 EventDTO 를 Event 로 바꾼다.
        if (errors.hasErrors()) { // @Valid 애너테이션에 의해서 입력값이 걸러지면 여기에 걸린다.
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

//        Event event = Event.builder() 1. ModelMapper 를 사용하지 않는 방법
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
        //ModelMapper 를 사용하는 방법
        Event event = modelMapper.map(eventDto , Event.class); // 위에 사용하지 않는 방법은 많은 값을 입력한다. //ModelMapper 를 사용하면 이 1줄로 들어온 모든 값을 1세팅 할 수 있다.
        event.update();
        Event newEvent = eventRepository.save(event);
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());

        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        EventResource eventResource = new EventResource(newEvent); // 링크를 추가하기 위한 코드
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));

//        EventResource eventResource = new EventResource(event);
//        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());
//        eventResource.add(selfLinkBuilder.withRel("update-event"));

        return ResponseEntity.created(createUri).body(eventResource); // body()의 괄호 안에 event 대신에 link를 추가한 eventResource 를 담는다.
    }

}