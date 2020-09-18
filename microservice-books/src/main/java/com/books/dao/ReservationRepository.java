package com.books.dao;



import com.books.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

public List<Reservation> findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(long idUser);

Set<Reservation> findByCloturerFalseAndDateRetourBefore(Date date);
}
