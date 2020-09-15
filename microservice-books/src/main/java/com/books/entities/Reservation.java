package com.books.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Reservation {

    @Id@GeneratedValue
    @Column(name = "id_emprunt")
    private Long id;

    private Long idUtilisateur;
    private Date dateReservation;
    private Date dateNextRetour;

    @ManyToOne
    @JoinColumn(name ="ID_BOOK" )
    private Book book;

    private Integer position;
    private boolean enCours;
    private boolean notified;

    public Reservation() {
        super();
    }

    public Reservation(Book book) {
        this.dateReservation=new Date();
        this.enCours=true;
        this.book=book;
        this.notified=false;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Date getDateNextRetour() {
        return dateNextRetour;
    }

    public void setDateNextRetour(Date dateNextRetour) {
        this.dateNextRetour = dateNextRetour;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isEnCours() {
        return enCours;
    }

    public void setEnCours(boolean enCours) {
        this.enCours = enCours;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", idUtilisateur=" + idUtilisateur +
                ", dateReservation=" + dateReservation +
                ", book=" + book +
                '}';
    }
}
