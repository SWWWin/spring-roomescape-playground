package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();

    // reservation 템플릿을 반환
    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }

    // 예약 목록 반환
    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservations);
    }
}
