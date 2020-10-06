package com.books.booksServicesUnitTest;

import com.books.beans.UtilisateurBean;
import com.books.dao.*;
import com.books.entities.*;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.implementations.EmailServiceImpl;
import com.books.services.implementations.EmpruntServiceImpl;
import com.books.services.implementations.OutilServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.mail.MessagingException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EmpruntServiceUnitTest {
    @Mock
    private EmpruntRepository empruntRepository;
    @Mock
    private OutilServiceImpl bibliService;
    @Mock
    private CopiesRepository copiesRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private MicroserviceUtilisateurProxy utilisateurProxy;

    @InjectMocks
    @Autowired
    private EmpruntServiceImpl empruntService;

    private List<Book> livres = new ArrayList<>();
    private List<Copy> copies = new ArrayList<>();
    private Set<Emprunt> emprunts_user_set = new HashSet<>();
    private Set<Emprunt> emprunts_admin_set = new HashSet<>();
    private List<Emprunt> emprunts = new ArrayList<>();
    private List<Copy> copiesDispo = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    @Before
    public void setUp(){

        UtilisateurBean user = new UtilisateurBean();
        user.setIdUser(1L);
        user.setEmail("aa");
        user.setUsername("user");

        UtilisateurBean admin =  new UtilisateurBean();
        admin.setIdUser(2L);
        admin.setEmail("bb");
        admin.setUsername("admin");

        Book book1 = new Book("aa","bb","cc");
        book1.setId(1L);
        livres.add(book1);

        Book book2 = new Book("dd","ee","ff");
        book1.setId(2L);
        livres.add(book2);

        Copy copy1=new Copy("01",book1);
        copy1.setId(3L);
        copy1.setDispo(false);

        Copy copy2=new Copy("02",book1);
        copy2.setId(4L);


        Copy copy3=new Copy("03",book2);
        copy3.setId(5L);
        copy3.setDispo(false);

        Copy copy4=new Copy("04",book2);
        copy4.setId(6L);


        Date date1 = new GregorianCalendar(2020, Calendar.FEBRUARY, 24).getTime();
        Date date2 = new GregorianCalendar(2020, Calendar.OCTOBER, 5).getTime();

        Emprunt emprunt1=new Emprunt(copy1, date1);
        emprunt1.setId(1L);
        emprunt1.setDateRetour(ajouter4semaines(emprunt1.getDateEmprunt()));
        emprunt1.setIdUtilisateur(1L);
        copy1.setDispo(false);

        Emprunt emprunt2 = new Emprunt(copy3,date2 );
        emprunt2.setId(2L);
        emprunt2.setDateRetour(ajouter4semaines(emprunt2.getDateEmprunt()));
        emprunt2.setIdUtilisateur(2L);
        copy3.setDispo(false);

        Reservation reservation = new Reservation(book2);
        reservation.setIdUtilisateur(1L);
        reservation.setId(1L);
        reservation.setPosition(0);

        reservations.add(reservation);

        Email email = new Email();
        email.setName("notification");
        email.setObjet("notification de disponiblité");
        email.setContenu("Bonjour [USERNAME], \n "+
                "\n"+
                "\tBonjour, le livre [LIVRE_TITRE] que vous avez réservé est de nouveau disponible à la blibliothèque .\n" +
                "Vous disposez de 48h à partir du [DATE_RENDU] pour venir retirer votre exemplaire, passé ce délai vous sortirez de la liste d'attente.\n" +
                "Dans l'attente de votre visite.\n" +
                "\n"+
                "Cordialement.");

        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);

        copiesDispo.add(copy2);
        copiesDispo.add(copy4);

        book1.setCopies(copies.subList(0,2));
        book2.setCopies(copies.subList(2,4));

        emprunts_user_set.add(emprunt1);
        emprunts_admin_set.add(emprunt2);

        emprunts.add(emprunt1);
        emprunts.add(emprunt2);

        Mockito.when(utilisateurProxy.utilisateurById(1L)).thenReturn(user);
        Mockito.when(utilisateurProxy.utilisateurById(2L)).thenReturn(admin);


        Mockito.when(copiesRepository.ListCopyDispoByBook(1L)).thenReturn(copiesDispo.subList(0,1));
        Mockito.when(copiesRepository.ListCopyDispoByBook(2L)).thenReturn(copiesDispo.subList(1,2));

        Mockito.when(copiesRepository.findById(3L)).thenReturn(Optional.of(copy1));
        Mockito.when(copiesRepository.findById(4L)).thenReturn(Optional.of(copy2));
        Mockito.when(copiesRepository.findById(5L)).thenReturn(Optional.of(copy3));
        Mockito.when(copiesRepository.findById(6L)).thenReturn(Optional.of(copy4));

        Mockito.when(empruntRepository.findAllByIdUtilisateurAndCloturerFalseOrderByDateRetourAsc(1L)).thenReturn(emprunts_user_set);
        Mockito.when(empruntRepository.findAllByIdUtilisateurAndCloturerFalseOrderByDateRetourAsc(2L)).thenReturn(emprunts_admin_set);

        Mockito.when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt1));
        Mockito.when(empruntRepository.findById(2L)).thenReturn(Optional.of(emprunt2));


        Mockito.when(empruntRepository.save(emprunt1)).thenReturn(emprunt1);
        Mockito.when(empruntRepository.save(emprunt2)).thenReturn(emprunt2);

        Mockito.when(copiesRepository.save(copy1)).thenReturn(copy1);
        Mockito.when(copiesRepository.save(copy2)).thenReturn(copy2);
        Mockito.when(copiesRepository.save(copy3)).thenReturn(copy3);
        Mockito.when(copiesRepository.save(copy4)).thenReturn(copy4);


        Mockito.when(empruntRepository.livreDejaEmprunteParUtilisateur(1L,1L)).thenReturn(emprunts.subList(0,1));
        Mockito.when(empruntRepository.livreDejaEmprunteParUtilisateur(2L,2L)).thenReturn(emprunts.subList(1,2));

        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(1L,1L)).thenReturn(reservation);
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(2L,1L)).thenReturn(new Reservation());
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(1L,2L)).thenReturn(new Reservation());
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(2L,2L)).thenReturn(new Reservation());

        Mockito.when(emailRepository.findByName("notification")).thenReturn(email);

        Mockito.when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt1));
        Mockito.when(empruntRepository.findById(2L)).thenReturn(Optional.of(emprunt2));

        Mockito.when(reservationRepository.findAllByBookIdAndEnCoursIsTrueAndNotifiedIsFalseOrderByDateReservationAsc(2L)).thenReturn(new ArrayList<>());



    }

    @Test
    public void empruntByUSer_test(){
        Set<Emprunt> emprunts_user = empruntService.empruntByUSer(1L);
        assertThat(emprunts_user.size()).isEqualTo(1);
        Set<Emprunt> emprunts_admin = empruntService.empruntByUSer(2L);
        assertThat(emprunts_admin.size()).isEqualTo(1);

    }

    @Test
    public void prolongerEmprunt_test(){
        ResponseEntity response1 = empruntService.prolongerEmprunt(1L,1L);
        assertThat(response1.getStatusCodeValue()).isEqualTo(400);
        ResponseEntity response2 = empruntService.prolongerEmprunt(2L,2L);
        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void ouvrirEmprunt_test(){
        ResponseEntity response1 = empruntService.ouvrirEmprunt(1L,1L);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity response2 = empruntService.ouvrirEmprunt(1L,2L);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity response3 = empruntService.ouvrirEmprunt(2L,1L);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity response4 = empruntService.ouvrirEmprunt(2L,2L);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cloturerEmprunt_test() throws MessagingException {
        ResponseEntity response1 = empruntService.cloturerEmprunt(1L);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity response2 = empruntService.cloturerEmprunt(2L);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    public Date ajouter4semaines(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 28);
        return cal.getTime();
    }
}
