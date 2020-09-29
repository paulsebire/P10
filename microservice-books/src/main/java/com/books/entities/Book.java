package com.books.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;


import java.util.Date;
import java.util.List;

@Entity
public class Book{
    @Id @GeneratedValue
    @Column(name = "id_book")
    private long id;
    private String name;
    private String author;
    private String coverUrl;
    private Date prochainRetour;
    private Integer nbTotalCopys;
    @JsonBackReference
    @OneToMany(mappedBy = "book",fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<Copy> copies;

    public Book() {super();}

    public Book(String name, String author, String coverUrl) {
        this.prochainRetour=new Date();
        this.name = name;
        this.author = author;
        this.coverUrl = coverUrl;
    }

    public Integer getNbTotalCopys() {
        return nbTotalCopys;
    }

    public void setNbTotalCopys(Integer nbTotalCopys) {
        this.nbTotalCopys = nbTotalCopys;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }


    public Date getProchainRetour() {
        return prochainRetour;
    }

    public void setProchainRetour(Date prochainRetour) {
        this.prochainRetour = prochainRetour;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
