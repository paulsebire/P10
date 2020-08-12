package com.books.dao;



import com.books.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

public List<Emprunt> findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(long idUser);

Set<Emprunt> findByCloturerFalseAndDateRetourBefore(Date date);
}
