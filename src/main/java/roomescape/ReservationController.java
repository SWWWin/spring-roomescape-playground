package roomescape;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";  // reservation.html 템플릿을 반환
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservations);  // JSON 형식으로 예약 목록 반환
    }

    // 예약 추가
    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        if (reservation.getName() == null || reservation.getName().isEmpty() ||
                reservation.getDate() == null || reservation.getDate().isEmpty() ||
                reservation.getTime() == null || reservation.getTime().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약 정보가 부족합니다.");
        }
        reservation.setId(counter.incrementAndGet());
        reservations.add(reservation);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    // 예약 삭제
    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> reservationToDelete = reservations.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if (!reservationToDelete.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약을 찾을 수 없습니다.");
        }

        reservations.removeIf(r -> r.getId().equals(id));
        return ResponseEntity.noContent().build();
    }

    // 예외 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleBadRequest(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getReason());
    }
}
