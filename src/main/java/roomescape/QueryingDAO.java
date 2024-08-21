package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class QueryingDAO {

    private final JdbcTemplate jdbcTemplate;

    public QueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        Long reservationId = resultSet.getLong("reservation_id");
        String name = resultSet.getString("name");
        String date = resultSet.getString("date");
        Long timeId = resultSet.getLong("time_id");
        String timeValue = resultSet.getString("time_value");

        Time time = new Time(timeId, timeValue);
        return new Reservation(reservationId, name, date, time);
    };

    public List<Reservation> findAllReservations() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.time as time_value " +
                "FROM reservation as r INNER JOIN time as t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findReservationById(Long id) {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.time as time_value " +
                "FROM reservation as r INNER JOIN time as t ON r.time_id = t.id " +
                "WHERE r.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Long createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().getId());  // Set time_id
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();  // Retrieve the generated ID
    }

    public void deleteReservation(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
