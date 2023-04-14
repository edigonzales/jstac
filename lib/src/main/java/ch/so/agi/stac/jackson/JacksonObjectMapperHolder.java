package ch.so.agi.stac.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ch.so.agi.stac.model.Bbox;

public class JacksonObjectMapperHolder {
    
    private static JacksonObjectMapperHolder INSTANCE;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JacksonObjectMapperHolder() {        
    }
    
    public static JacksonObjectMapperHolder getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new JacksonObjectMapperHolder();
        }
        
        return INSTANCE;
    }

    public final ObjectMapper getObjectMapper() {   
        {
            SimpleModule module = new SimpleModule();
            module.addSerializer(Bbox.class, new BboxSerializer());
            module.addDeserializer(Bbox.class, new BBoxDeserializer());
            objectMapper.registerModule(module);
        }
        
        {
            SimpleModule module = new SimpleModule();
            module.addSerializer(Interval.class, new IntervalSerializer());
            module.addDeserializer(Interval.class, new IntervalDeserializer());
            objectMapper.registerModule(module);
        }
        
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());


        return objectMapper;
    }

}
