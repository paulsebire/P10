package com.books.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;


import java.util.List;

@Entity
public class Book{
    @Id @GeneratedValue
    private long id;
    private String name;
    private String author;
    private String coverUrl;

    @JsonBackReference
    @OneToMany(mappedBy = "book",fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<Copy> copies;

    public Book() {super();}

    public Book(String name, String author, String coverUrl) {
        this.name = name;
        this.author = author;
        this.coverUrl = coverUrl;
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
