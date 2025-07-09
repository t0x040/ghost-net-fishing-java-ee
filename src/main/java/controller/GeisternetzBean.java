package controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import model.BergendePerson;
import model.Geisternetz;
import model.MeldendePerson;
import util.JpaUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("geisternetzBean")
@ViewScoped
public class GeisternetzBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rolle = "meldend";

    private double latitude;
    private double longitude;
    private double groesse;
    private boolean anonym;
    private String name;
    private String telefonnummer;

    private String bergenderName;
    private String bergenderTelefon;

    // Netze laden
    public List<Geisternetz> getNetze() {
        System.out.println("getNetze() aufgerufen");
        EntityManager em = JpaUtil.getEntityManager();
        List<Geisternetz> result = new ArrayList<>();
        try {
            result = em.createQuery("SELECT g FROM Geisternetz g", Geisternetz.class).getResultList();
            System.out.println("Anzahl gefundener Netze: " + result.size());
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Netze: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    // Neues Netz melden
    public void addGeisternetz() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            MeldendePerson meldendePerson = new MeldendePerson();
            meldendePerson.setAnonym(anonym);

            if (anonym) {
                meldendePerson.setName("Anonym");
                meldendePerson.setTelefonnummer("");
            } else {
                meldendePerson.setName(name != null ? name : "Unbekannt");
                meldendePerson.setTelefonnummer(telefonnummer != null ? telefonnummer : "");
            }

            Geisternetz netz = new Geisternetz();
            netz.setLatitude(latitude);
            netz.setLongitude(longitude);
            netz.setGroesse(groesse);
            netz.setStatus(Geisternetz.Status.GEMELDET);
            netz.setMeldendePerson(meldendePerson);

            em.persist(meldendePerson);
            em.persist(netz);

            em.getTransaction().commit();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Geisternetz erfolgreich gespeichert."));
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        latitude = 0;
        longitude = 0;
        groesse = 0;
        anonym = false;
        name = null;
        telefonnummer = null;
    }

    // Zur Bergung eintragen
    public void eintragenZurBergung(Geisternetz netz) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Geisternetz persistiertesNetz = em.find(Geisternetz.class, netz.getId());

            if (persistiertesNetz != null && persistiertesNetz.getBergendePerson() == null) {
                BergendePerson person = new BergendePerson();
                person.setName(bergenderName);
                person.setTelefonnummer(bergenderTelefon);

                em.persist(person);
                persistiertesNetz.setBergendePerson(person);
                persistiertesNetz.setStatus(Geisternetz.Status.BERGUNG_BEVORSTEHEND);
                em.merge(persistiertesNetz);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Als geborgen markieren
    public void markiereAlsGeborgen(Geisternetz netz) {
        if (!"bergend".equals(rolle)) return;

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Geisternetz persistiertesNetz = em.find(Geisternetz.class, netz.getId());

            if (persistiertesNetz != null) {
                persistiertesNetz.setStatus(Geisternetz.Status.GEBORGEN);
                em.merge(persistiertesNetz);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Getter & Setter
    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getGroesse() {
        return groesse;
    }

    public void setGroesse(double groesse) {
        this.groesse = groesse;
    }

    public boolean isAnonym() {
        return anonym;
    }

    public void setAnonym(boolean anonym) {
        this.anonym = anonym;
    }

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

    public String getBergenderName() {
        return bergenderName;
    }

    public void setBergenderName(String bergenderName) {
        this.bergenderName = bergenderName;
    }

    public String getBergenderTelefon() {
        return bergenderTelefon;
    }

    public void setBergenderTelefon(String bergenderTelefon) {
        this.bergenderTelefon = bergenderTelefon;
    }
}
