package ch.so.agi.stac.jackson;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ch.so.agi.stac.model.Interval;

public class IntervalSerializer extends StdSerializer<Interval> {
    public IntervalSerializer() {
        this(null);
    }

    protected IntervalSerializer(Class<Interval> t) {
        super(t);
    }

    @Override
    public void serialize(Interval value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String startInterval = value.getStartInterval()!=null?value.getStartInterval().format(DateTimeFormatter.ISO_INSTANT):null;
        String endInterval = value.getEndInterval()!=null?value.getEndInterval().format(DateTimeFormatter.ISO_INSTANT):null;
        
        String[] interval = {startInterval, endInterval};
        
        gen.writeStartObject();
        gen.writeArrayFieldStart("interval");
        gen.writeArray(interval, 0, 2);
        gen.writeEndArray();
        gen.writeEndObject();   
    }
}
