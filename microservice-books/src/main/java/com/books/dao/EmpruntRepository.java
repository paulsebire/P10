package com.books.dao;



import com.books.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(long idUser);
    List<Emprunt> findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(long id);
    Set<Emprunt> findByCloturerFalseAndDateRetourBefore(Date date);

    @Query(value = "select e from Emprunt e inner join fetch e.copy c where e.idUtilisateur=:idUser and e.cloturer=false and c.book.id=:idBook",
            countQuery = "select count (e) from Emprunt e inner join e.copy c where e.idUtilisateur=:idUser and e.cloturer=false and c.book.id=:idBook")
    List<Emprunt> livreDejaEmprunteParUtilisateur(@Param("idUser")long idUSer, @Param("idBook")long idBook);
}
