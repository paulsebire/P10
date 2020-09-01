package com.clientui.beans;

import java.util.Date;

public class ReservationBean {

    private Long id;
    private Long idUtilisateur;
    private Date dateReservation;
    private Date dateNextRetour;
    private BookBean book;
    private Integer position;
    private boolean enCours;

    public ReservationBean() {
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

    public boolean isEnCours() {
        return enCours;
    }

    public void setEnCours(boolean enCours) {
        this.enCours = enCours;
    }

    public Date getDateNextRetour() {
        return dateNextRetour;
    }

    public void setDateNextRetour(Date dateNextRetour) {
        this.dateNextRetour = dateNextRetour;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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
