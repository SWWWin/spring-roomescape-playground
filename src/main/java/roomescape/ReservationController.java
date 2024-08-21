package roomescape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public String reservation() {
        return "new-reservation";
    }

    @Autowired
    private QueryingDAO queryingDAO;

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation request) {
        // Validate the request body
        if (request.getName() == null || request.getName().isBlank() ||
                request.getDate() == null || request.getDate().isBlank() ||
                request.getTime() == null || request.getTime().getId() == null) {
            throw new IllegalArgumentException("데이터가 비어있습니다.");
        }

        // Create the reservation and get the new ID
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
