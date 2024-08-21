package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeRepository timeRepository;

    public TimeController(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @PostMapping
    public ResponseEntity<Time> addTime(@RequestBody Time time) {
        Time savedTime = timeRepository.save(time);
        return ResponseEntity
                .created(URI.create("/times/" + savedTime.getId()))
                .body(savedTime);
    }


    @GetMapping
    public List<Time> getAllTimes() {
        return timeRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        if (timeRepository.existsById(id)) {
            timeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
