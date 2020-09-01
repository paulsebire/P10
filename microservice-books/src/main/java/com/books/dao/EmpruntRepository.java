package com.books.dao;



import com.books.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(long idUser);
    List<Emprunt> findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(long id);
    Set<Emprunt> findByCloturerFalseAndDateRetourBefore(Date date);
}
