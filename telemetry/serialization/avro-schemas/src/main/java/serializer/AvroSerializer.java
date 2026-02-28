package serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory encoderFactory;
    private BinaryEncoder encoder;

    public AvroSerializer() {
        this.encoderFactory = EncoderFactory.get();
    }

    public AvroSerializer(EncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
    }

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (data != null) {
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());
                encoder = encoderFactory.binaryEncoder(outputStream, encoder);
                writer.write(data, encoder);
                encoder.flush();
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сериализации " + data, e);
        }
    }
}
