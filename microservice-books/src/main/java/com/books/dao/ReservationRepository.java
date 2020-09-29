package com.books.dao;

import com.books.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByIdUtilisateurAndEnCoursIsTrueOrderByDateReservationAsc(long idUser);

    List<Reservation> findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(long id);

    Reservation findByBookIdAndIdUtilisateurAndEnCoursTrue(long  id, long idUtilisateur);

    List<Reservation> findAllByBookIdAndEnCoursIsTrueAndNotifiedIsFalseOrderByDateReservationAsc(long id);

    List<Reservation> findAllByBookIdAndEnCoursIsTrueAndNotifiedIsTrueOrderByDateReservationAsc(long id);
}
