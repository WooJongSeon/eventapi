package com.example.api.events;

import com.example.api.common.RestDocsConfiguration;
import com.example.api.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest//web 과 관련된 것만 만들기 때문에 슬라이싱 테스트라고 부른다. 테스트용 빈을 모두 만드는 것이 아니라 웹과 관련된 것만 만든다. 그래서 더 빠르다.
//WebMvc - 슬라이싱 테스트용 애너테이션
@SpringBootTest // 웹에 관련된 테스트는 주로 SpringBootTest 로 작성한다. 이것을 슬라이싱 테스트로 안하는 이유는 Mock 로 만들어줘야 하는 항목이 너무 많아지기 때문이다.
//이렇게 SpringBootTest 애너테이션을 붙이면 메인 클래스의 @SpringBootApplication 애너테이션을 찾는다.
//그렇게 찾은 후에는 모든 @Bean을 등록해준다. 그럼 테스트 코드에서 @Autowired 나 객체 사용시 문제가 없게 된다.
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 가짜 요청을 만들어서 DispatcherServlet 에 보내고 테스트 해볼 수 있다.
    //웹서버를 띄우는 테스트보다는 적게 걸리지만. 단위테스트 보다는 조금 더 걸린다.

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-event",
                    links(
                            linkWithRel("self").description("link to self"),
                            linkWithRel("query-events").description("link to query events"),
                            linkWithRel("update-event").description("link to update an existing event")
                    ),
                        requestHeaders(headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event")
                        ),
                        responseHeaders(
                            headerWithName(HttpHeaders.LOCATION).description("Location header"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin event time"),
                                fieldWithPath("endEventDateTime").description("date time of end event time"),
                                fieldWithPath("location").description("location"),
                                fieldWithPath("basePrice").description("base price"),
                                fieldWithPath("maxPrice").description("max price"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("free"),
                                fieldWithPath("offline").description("it tells if this event offline"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("self link"),
                                fieldWithPath("_links.query-events.href").description("query event link"),
                                fieldWithPath("_links.update-event.href").description("update event link")
                        )
                ));

//                .andExpect(jsonPath("id").exists())
//                .andExpect(header().exists(HttpHeaders.LOCATION))
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("free").value(false))
//                .andExpect(jsonPath("offline").value(true))
//                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
//                .andExpect(jsonPath("_link.self").exists()) // 링크 정보 추가
//                .andExpect(jsonPath("_link.query-events").exists()) // 링크 정보 추가
//                .andExpect(jsonPath("_link.update-events").exists()); // 링크 정보 추가
                //.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));



        /*EventDto event = EventDto.builder()
                            .name("Spring")
                            .description("REST API Development with Spring")
                            .beginEnrollmentDateTime(LocalDateTime.of(2018,11,1,10,0))
                            .closeEnrollmentDateTime(LocalDateTime.of(2018,11,5,10,0))
                            .beginEventDateTime(LocalDateTime.of(2018,11,25,10,0))
                            .endEventDateTime(LocalDateTime.of(2018,11,26,10,0))
                            .basePrice(100)
                            .maxPrice(200)
                            .limitOfEnrollment(100)
                            .location("강남역 D2 스타텁 팩토리")
                            .build();

        mockMvc.perform(post("/api/events/") //perform 안에는 요청 uri 를 적는다.
                    .contentType(MediaType.APPLICATION_JSON_UTF8) //요청 content type
                    .accept(MediaTypes.HAL_JSON) //응답 Media 타입
                    .content(objectMapper.writeValueAsString(event))) // HAL = Hypertext Application Language , 응답 내용을 ObjectMapper 로 작성한다.(JSON)
                .andDo(print()) // 요청 정보를 모두 출력한다.
                .andExpect(status().isCreated()) // 응답이 어떤지 확인한다. - andExpect()
                //.andExpect(jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT.name())))
                .andExpect(jsonPath("_link.self").exists())
                .andExpect(jsonPath("_link.query-events").exists())
                .andExpect(jsonPath("_link.update-event").exists());*/
    }


    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,1,10,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,5,10,0))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,10,0))
                .endEventDateTime(LocalDateTime.of(2018,11,26,10,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("로케이션")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);
        mockMvc.perform(post("/api/events/") //perform 안에는 요청 uri 를 적는다.
                .contentType(MediaType.APPLICATION_JSON_UTF8) //요청 content type
                .accept(MediaTypes.HAL_JSON) //응답 Media 타입
                .content(objectMapper.writeValueAsString(event))) // HAL = Hypertext Application Language , 응답 내용을 ObjectMapper 로 작성한다.(JSON)
                .andDo(print()) // 요청 정보를 모두 출력한다.
                .andExpect(status().isBadRequest()); // 응답이 어떤지 확인한다. - andExpect()
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,20,10,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,21,10,0))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,10,0))
                .endEventDateTime(LocalDateTime.of(2018,11,24,10,0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("로케이션")
                .build();

        mockMvc.perform(post("/api/events/") //perform 안에는 요청 uri 를 적는다.
                .contentType(MediaType.APPLICATION_JSON_UTF8) //요청 content type
                .content(objectMapper.writeValueAsString(eventDto))) // HAL = Hypertext Application Language , 응답 내용을 ObjectMapper 로 작성한다.(JSON)
                .andDo(print()) // 요청 정보를 모두 출력한다.
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists()); // 응답이 어떤지 확인한다. - andExpect()

    }
}