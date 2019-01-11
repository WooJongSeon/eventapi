package com.example.api.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest//web 과 관련된 것만 만들기 때문에 슬라이싱 테스트라고 부른다. 테스트용 빈을 모두 만드는 것이 아니라 웹과 관련된 것만 만든다. 그래서 더 빠르다.
//WebMvc - 슬라이싱 테스트용 애너테이션
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 가짜 요청을 만들어서 DispatcherServlet 에 보내고 테스트 해볼 수 있다.
    //웹서버를 띄우는 테스트보다는 적게 걸리지만. 단위테스트 보다는 조금 더 걸린다.

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
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
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))) // HAL = Hypertext Application Language
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }
}