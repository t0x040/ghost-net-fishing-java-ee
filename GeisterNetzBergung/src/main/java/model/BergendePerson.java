package model;

import jakarta.persistence.*;

@Entity
public class BergendePerson extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
