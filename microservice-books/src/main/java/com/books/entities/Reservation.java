package com.books.entities;

import com.books.beans.UtilisateurBean;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Reservation {
    @Id@GeneratedValue
    @Column(name = "id_reservation")
    private Long id;

    private Long idUtilisateur;
    private Date dateEmprunt;
    private Date dateRetour;
    private boolean prolonger=false;
    private boolean cloturer=false;
    @ManyToOne
    @JoinColumn(name ="ID_COPY" )
    private Copy copy;

    public Reservation() {
        super();
    }

    public Reservation(Copy copy, Date dateEmprunt) {
        this.dateEmprunt=dateEmprunt;
        this.prolonger=false;
        this.cloturer=false;
        this.copy=copy;
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

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public boolean isProlonger() {
        return prolonger;
    }

    public void setProlonger(boolean prolonger) {
        this.prolonger = prolonger;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetour=" + dateRetour +
                ", prolonger=" + prolonger +
                ", copy=" + copy +
                '}';
    }
}
