package com.example.api.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    @Parameters({
            "1,2","12,2","1,20","14,2","15,2","1,25","12,2","11,2","11,2","1,62","14,2","1,22","1,42","13,2","31,2","1,52","51,2","1,62","61,2","1,2"
    })
    public void parameterTest(int num1, int num2){
        System.out.println(num1 < num2);
    }


    @Test
    public void builder(){
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }



    @Test
    public void javaBean(){
        //Given
        String name = "Event";
        String description = "Spring";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
    public void testFree(){
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isTrue();
    }

    @Test
    public void testOffline(){
        //Given
        Event event = Event.builder()
                .location("강남역 네이버")
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();
    }
}