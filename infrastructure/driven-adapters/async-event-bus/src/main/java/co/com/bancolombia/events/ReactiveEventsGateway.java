package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.placetohide.PlaceToHide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String SOME_EVENT_NAME = "place.searched";
    private final DomainEventBus domainEventBus;


    @Override
    public Mono<Void> emit(int floor, String room) {
        PlaceToHide placeToHide = new PlaceToHide();
        placeToHide.setFloor(floor);
        placeToHide.setRoom(room);
        ObjectMapper om = new ObjectMapper();

        CloudEvent attributes = null;
        try {
            String objectClass = om.writeValueAsString(placeToHide);
            attributes = CloudEventBuilder.v1() //
                    .withId(UUID.randomUUID().toString()) //
                    .withSource(URI.create("https://spring.io/foos"))//
                    .withType("event") //
                    .withDataContentType("application/json")
                    .withTime(OffsetDateTime.now())
                    .withData("text/plain", objectClass.getBytes("UTF-8"))
                    .build();



        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR -> " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("ERROR JSON -> " + e.getMessage());
        }
        byte[] serialized = EventFormatProvider
                .getInstance()
                .resolveFormat(JsonFormat.CONTENT_TYPE)
                .serialize(attributes);
        return from(domainEventBus.emit(new DomainEvent<byte[]>(SOME_EVENT_NAME, UUID.randomUUID().toString(), serialized)));
    }
}
