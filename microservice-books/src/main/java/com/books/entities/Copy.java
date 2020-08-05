package com.books.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@Entity
public class Copy{
    @Id @GeneratedValue
    @Column(name = "id_copy")
    private Long id;
    private boolean dispo;
    private String serialNumber;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name ="id" )
    private Book book;

    @JsonIgnore
    @OneToMany(mappedBy = "copy",fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<Reservation> reservations;

    public Copy() { super();}

    public Copy(String serialNumber, Book book) {
        this.serialNumber = serialNumber;
        this.dispo=true;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDispo() {
        return dispo;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
