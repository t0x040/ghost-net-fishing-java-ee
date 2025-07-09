package model;

import jakarta.persistence.*;

@Entity
public class MeldendePerson extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean anonym;

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAnonym() {
        return anonym;
    }

    public void setAnonym(boolean anonym) {
        this.anonym = anonym;
    }
}
