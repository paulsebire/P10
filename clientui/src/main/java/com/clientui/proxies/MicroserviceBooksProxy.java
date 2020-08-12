package com.clientui.proxies;

import com.clientui.beans.BookBean;
import com.clientui.beans.CopyBean;
import com.clientui.beans.EmpruntBean;
import com.clientui.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "zuul-server",contextId = "microserviceBooksProxy",
            configuration= FeignConfig.class)
@Component
public interface MicroserviceBooksProxy {
    /**
     * method to get  a list of books from microservice-books
     * @param mc keyword  for research function
     * @return a list of books
     */
    @GetMapping(value = "/microservice-books/livres")
    List<BookBean> bookList(@RequestParam(name = "mc", defaultValue = "")String mc);

    /**
     * method to get a book from microservice-books
     * @param id id of the book
     * @return a book object
     */
    @GetMapping( value = "/microservice-books/livre/{id}")
    BookBean recupererUnLivre(@PathVariable("id") Long id);

    /**
     * method to get  a list of available copies from microservice-books
     * @param id of the book
     * @return a list of copies
     */
    @GetMapping(value = "/microservice-books/livre/{id}/copies")
    List<CopyBean> CopiesDispo(@PathVariable("id") Long id);

    /**
     * method to get  a list of emprunts from microservice-books
     * @param id idi oof the borrower
     * @return a list of emprunts
     */
    @GetMapping(value = "/microservice-books/utilisateur/{id}/emprunts/")
    List<EmpruntBean> empruntList(@PathVariable(value = "id")Long id);

    /**
     * method to give extra time to an emprunt
     * @param idE id of the emprunt
     * @param idUser id of the borrower
     */
    @PostMapping(value = "/microservice-books/utilisateur/{idUser}/emprunt/{idE}/prolonger")
    void prolongerEmprunt(@PathVariable(value = "idE")Long idE, @PathVariable(value = "idUser") Long idUser);
}
