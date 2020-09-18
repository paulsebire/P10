package com.clientui.beans;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;


 public class BookBean {
    private long id;
    private String name;
    private String author;
    private String coverUrl;
    private Collection<CopyBean> copies;

     public BookBean() {

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

     public Collection<CopyBean> getCopies() {
         return copies;
     }

     public void setCopies(Collection<CopyBean> copies) {
         this.copies = copies;
     }

     @Override
    public String toString() {
        return "BookBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", copiesBean=" + copies +
                '}';
    }
}
