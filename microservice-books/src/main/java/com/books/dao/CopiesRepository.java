package com.books.dao;


import com.books.entities.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CopiesRepository extends JpaRepository<Copy, Long> {

    public List<Copy> findCopiesByBookIdAndDispoTrue(Long id);

    @Query(value = "select c from Copy c inner join fetch c.book b where b.id=:id and c.dispo=true",
            countQuery = "select count (c) from Copy c inner join c.book b where b.id=:id and c.dispo=true")
    List<Copy> ListCopyDispoByBook(@Param("id") Long id);


}
