package roomescape;

public class Reservation {
    private Long id;
    private String name;
    private String date;

    public Reservation(Long id, String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Reservation() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

