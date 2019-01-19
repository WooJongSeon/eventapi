package com.example.api.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> { // 이게 필요한 이유는 Event 객체는 json 으로 Serialize 해서 리턴 할 수 있다.
    //하지만 Errors 객체는 Serialize 가 안되기 때문에 이렇게 Serializer 클래스를 만들어서 Serialize 하고 리턴한다.
    //Json Serializer의 제네릭에 <Errors>를 명시했다.
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException { // serialize 함수를 override 한다.
        gen.writeStartArray(); // errors 안에는 에러가 여러개 있는데 이것을 배열로 담기 위해서 이렇게 작성한다.
        errors.getFieldErrors().forEach( e -> { // EventValidator 클래스에서 rejectValue 라고 작성되어 있다면 이 부분이 사용된다.
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("field" , e.getField());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeStringField("code", e.getCode());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null){
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }
                gen.writeEndObject();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        errors.getGlobalErrors().forEach(e -> { // EventValidator 클래스에서 reject 라고 작성되어 있다면 이 부분이 사용된다.
            try{
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        });
        gen.writeEndArray();// errors 안에는 에러가 여러개 있는데 이것을 배열로 담기 위해서 이렇게 작성한다.
    }
}