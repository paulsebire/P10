package com.books.web.controller;


import com.books.dao.CopiesRepository;
import com.books.dao.EmpruntRepository;
import com.books.entities.Copy;
import com.books.entities.Emprunt;
import com.books.services.BibliServiceImpl;
import com.books.web.exceptions.EmpruntNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class EmpruntController {

    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private BibliServiceImpl bibliService;
    @Autowired
    private CopiesRepository copiesRepository;

    /**
     * find emprunts in Db for a specific user
     * @param id id of the user
     * @return alist of emprunts
     */
    @GetMapping(value = "/utilisateur/{id}/emprunts/")
    public List<Emprunt> empruntList (@PathVariable(value = "id")Long id){
        List<Emprunt> emprunts = empruntRepository.findAllByIdUtilisateurAndCloturerFalseOrderByIdAsc(id);
        if (emprunts.isEmpty()) throw new EmpruntNotFoundException("Aucun emprunt n'est disponible");
        return emprunts;
    }

    /**
     * method to give credit time to an emprunt
     * @param idE id of the emprunt
     * @param idUser id of the borrower
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/emprunt/{idE}/prolonger")
    void prolongerEmprunt(@PathVariable(value = "idE")Long idE, @PathVariable(value = "idUser") Long idUser){
        Optional<Emprunt> r= empruntRepository.findById(idE);
        Date dateDujour = new Date();
        if (r.isPresent()){
            Emprunt emprunt =r.get();
            if (emprunt.getIdUtilisateur()==idUser && emprunt.isProlonger()==false){
                emprunt.setProlonger(true);
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateRetour()));
                empruntRepository.save(emprunt);
            }
        }
    }

    /**
     * method to create an emprunt
     * @param idUser id of the borrower
     * @param idCopy if of the copy borrowed
     * @return a response entity depending on the scenario
     */
    @PostMapping(value = "/utilisateur/{idUser}/copie/{idCopy}/emprunt/ouvrir")
     ResponseEntity reserverCopy(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idCopy")Long idCopy){

        Optional<Copy> c   = copiesRepository.findById(idCopy);

        if (c.isPresent()){
             Copy copy=c.get();
            if (copy.isDispo()){
                Emprunt emprunt =new Emprunt(copy,new Date());
                emprunt.setDateRetour(bibliService.ajouter4semaines(emprunt.getDateEmprunt()));
                emprunt.setIdUtilisateur(idUser);
                copy.setDispo(false);
                copiesRepository.save(copy);
                empruntRepository.save(emprunt);
                return new ResponseEntity<>("emprunt effectué", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt introuvable", HttpStatus.BAD_REQUEST);
    }

    /**
     * method to close a emprunt
     * @param idE id of the emprunt
     * @return  a response entity depending on the scenario
     */
    @PutMapping(value = "/emprunt/{idE}/emprunt/cloturer")
    ResponseEntity cloturerEmprunt(@PathVariable(value = "idE")Long idE){

        Emprunt emprunt = empruntRepository.findById(idE).get();

        if (emprunt !=null){
            if (!emprunt.isCloturer()){
                Copy copy = copiesRepository.findById(emprunt.getCopy().getId()).get();
                emprunt.setCloturer(true);
                copy.setDispo(true);
                copiesRepository.save(copy);
                empruntRepository.save(emprunt);
                return new ResponseEntity<>("emprunt cloturée", HttpStatus.OK);
            }
        }return new ResponseEntity<>("emprunt introuvable", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/utilisateur/{idUser}/livre/{idBook}/reservable")
    boolean livreReservable(@PathVariable(value = "idUser")Long idUser,@PathVariable(value = "idBook")Long idBook){
        List<Emprunt> emprunts = empruntRepository.livreDejaEmprunteParUtilisateur(idUser,idBook);
        if (emprunts.isEmpty()){
            return true;
        } else return false;
    }
}
