package com.clientui.beans;


import java.util.Set;

public class CopyBean {
    private Long id;
    private String serialNumber;
    private BookBean book;
    private Set<ReservationBean> reservations;
    private boolean dispo;
    public CopyBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BookBean getBook() {
        return book;
    }

    public boolean isDispo() {
        return dispo;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }

    public Set<ReservationBean> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationBean> reservations) {
        this.reservations = reservations;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "CopyBean{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
