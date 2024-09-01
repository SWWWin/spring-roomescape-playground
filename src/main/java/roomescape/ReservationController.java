package roomescape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final QueryingDAO queryingDAO;

    // 생성자 주입 방식 사용
    @Autowired
    public ReservationController(QueryingDAO queryingDAO) {
        this.queryingDAO = queryingDAO;
    }

    // 만약 이 메소드가 "/reservation" 경로로 매핑된다면, 아래처럼 어노테이션 추가
    @GetMapping("/reservation")
    public ResponseEntity<String> reservation() {
        return ResponseEntity.ok("new-reservation");
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation request) {
        // 요청 바디 검증
        if (request.getName() == null || request.getName().isBlank() ||
                request.getDate() == null || request.getDate().isBlank() ||
                request.getTime() == null || request.getTime().getId() == null) {
            throw new IllegalArgumentException("데이터가 비어있습니다.");
        }

        // 예약 생성 및 ID 반환
        Long newId = queryingDAO.createReservation(request);
        Reservation newReservation = new Reservation(newId, request.getName(), request.getDate(), request.getTime());

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId()))
                .body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Reservation reservation = queryingDAO.findReservationById(id);
        if (reservation == null) {
            throw new IllegalArgumentException("예약이 존재하지 않습니다.");
        }

        queryingDAO.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = queryingDAO.findReservationById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = queryingDAO.findAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

