package co.com.bancolombia.usecase.emitevent;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EmitEventUseCase {

    private final EventsGateway eventsGateway;
    public Mono<Void> emitEvent(int floor, String room){
       return eventsGateway.emit(floor, room);
    }
}
