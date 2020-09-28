package com.clientui.proxies;

import com.clientui.beans.BookBean;
import com.clientui.beans.CopyBean;
import com.clientui.beans.EmpruntBean;
import com.clientui.beans.ReservationBean;
import com.clientui.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "zuul-server",contextId = "microserviceBooksProxy",
            configuration= FeignConfig.class)
@RequestMapping("/microservice-books")
public interface MicroserviceBooksProxy {
    /**
     * method to get  a list of books from microservice-books
     * @param mc keyword  for research function
     * @return a list of books
     */
    @GetMapping(value = "/livres")
    List<BookBean> bookList(@RequestParam(name = "mc", defaultValue = "")String mc);

    /**
     * method to get a book from microservice-books
     * @param id id of the book
     * @return a book object
     */
    @GetMapping( value = "/livre/{id}")
    BookBean recupererUnLivre(@PathVariable("id") Long id);

    /**
     * method to get  a list of available copies from microservice-books
     * @param id of the book
     * @return a list of copies
     */
    @GetMapping(value = "/livre/{id}/copies")
    List<CopyBean> CopiesDispo(@PathVariable("id") Long id);

    /**
     * method to get  a list of emprunts from microservice-books
     * @param id idi oof the borrower
     * @return a list of emprunts
     */
    @GetMapping(value = "/utilisateur/{id}/emprunts")
    Set<EmpruntBean> empruntList(@PathVariable(value = "id")Long id);

    /**
     * method to give extra time to an emprunt
     * @param idE id of the emprunt
     * @param idUser id of the borrower
     */
    @GetMapping(value = "/utilisateur/{idUser}/emprunt/{idE}/prolonger")
    void prolongerEmprunt(@PathVariable(value = "idE")Long idE, @PathVariable(value = "idUser") Long idUser);

    /**
     * method to get  a list of reservations from microservice-books
     * @param id id of the user
     * @return a list of reservations
     */
    @GetMapping(value = "/utilisateur/{id}/reservations")
    Set<ReservationBean> reservationsByUser(@PathVariable(value = "id")Long id);

    /**
     * Does the user as already an emprunt on a book to denied more reservation
     * @param idUser id of the user
     * @param idBook id of the book
     * @return
     */
    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reservable")
    boolean livreReservable(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook);

    /**
     * reserver un livre
     * @param idUser id de l'emetteur de la reservation
     * @param idBook id du livre a reserver
     * @return
     */
    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reserver")
    void reserverLivre(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook);


    /**
     * annuler une  reservation
     * @param idUser id de  l'emetteur de la reservation
     * @param id id de la reservation à annuler
     */
    @PutMapping(value = "/utilisateur/{idUser}/reservation/{id}/annuler")
    void annulerReservation(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "id")Long id);

    @GetMapping(value = "/livre/{idBook}/reservations")
    List<ReservationBean> reservationsByBook(@PathVariable(value = "idBook")Long idBook);
}
