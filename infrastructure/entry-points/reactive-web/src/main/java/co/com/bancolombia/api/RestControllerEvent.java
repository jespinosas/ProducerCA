package co.com.bancolombia.api;

import co.com.bancolombia.usecase.emitevent.EmitEventUseCase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class RestControllerEvent {


    private EmitEventUseCase useCase;

    @GetMapping("/search/{floor}/{room}")
    public Mono<Void> sendEvent(@PathVariable int floor, @PathVariable String room){
        return useCase.emitEvent(floor, room);
    }
}
