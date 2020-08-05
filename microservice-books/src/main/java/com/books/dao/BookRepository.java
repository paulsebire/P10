package com.books.dao;



import com.books.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b where lower(b.name) like lower(concat('%',:x,'%')) or lower(b.author) like lower(concat('%',:x,'%')) order by b.id")
    List<Book> chercherParTitre(@Param("x")String motCle);
}
