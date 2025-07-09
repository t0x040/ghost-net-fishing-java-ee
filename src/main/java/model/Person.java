package model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Person {

    @Column(nullable = false)
    private String name;

    @Column
    private String telefonnummer;

    // Getter / Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }
}
