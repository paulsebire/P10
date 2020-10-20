package com.books.booksServicesUnitTest;

import com.books.beans.UtilisateurBean;
import com.books.dao.*;
import com.books.entities.*;
import com.books.poxies.MicroserviceUtilisateurProxy;
import com.books.services.implementations.OutilServiceImpl;
import com.books.services.implementations.ReservationServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceUnitTest {
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
    private ReservationServiceImpl reservationService;

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
        book2.setId(2L);
        livres.add(book2);

        Book book3 = new Book("gg","hh","ii");
        book3.setId(3L);
        livres.add(book3);

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
        copy4.setDispo(false);

        Copy copy5=new Copy("05",book3);
        copy5.setId(7L);
        copy5.setDispo(false);

        Copy copy6=new Copy("06",book3);
        copy6.setId(8L);
        copy6.setDispo(false);

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

        Reservation reservation1 = new Reservation(book2);
        reservation1.setIdUtilisateur(1L);
        reservation1.setId(1L);
        reservation1.setPosition(0);

        Reservation reservation2 = new Reservation(book1);
        reservation2.setIdUtilisateur(2L);
        reservation2.setId(2L);
        reservation2.setPosition(0);

        reservations.add(reservation1);
        reservations.add(reservation2);

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
        copies.add(copy5);
        copies.add(copy6);

        copiesDispo.add(copy2);
        copiesDispo.add(copy4);

        book1.setCopies(copies.subList(0,2));
        book2.setCopies(copies.subList(2,4));
        book3.setCopies(copies.subList(4,6));

        emprunts_user_set.add(emprunt1);
        emprunts_admin_set.add(emprunt2);

        emprunts.add(emprunt1);
        emprunts.add(emprunt2);

        Mockito.when(copiesRepository.ListCopyDispoByBook(1L)).thenReturn(copiesDispo.subList(0,1));
        Mockito.when(copiesRepository.ListCopyDispoByBook(2L)).thenReturn(new ArrayList<>());

        Mockito.when(empruntRepository.livreDejaEmprunteParUtilisateur(1L,1L)).thenReturn(emprunts.subList(0,1));
        Mockito.when(empruntRepository.livreDejaEmprunteParUtilisateur(2L,2L)).thenReturn(emprunts.subList(1,2));

        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(1L,1L)).thenReturn(null);
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(2L,1L)).thenReturn(reservation1);
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(1L,2L)).thenReturn(reservation2);
        Mockito.when(reservationRepository.findByBookIdAndIdUtilisateurAndEnCoursTrue(2L,2L)).thenReturn(null);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation2));

        Mockito.when(reservationRepository.findAllByIdUtilisateurAndEnCoursIsTrueOrderByDateReservationAsc(1L)).thenReturn(reservations.subList(0,1));
        Mockito.when(reservationRepository.findAllByIdUtilisateurAndEnCoursIsTrueOrderByDateReservationAsc(2L)).thenReturn(reservations.subList(1,2));

        Mockito.when(reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(1L)).thenReturn(reservations.subList(1,2));
        Mockito.when(reservationRepository.findAllByBookIdAndEnCoursIsTrueOrderByDateReservationAsc(2L)).thenReturn(reservations.subList(0,1));


        Mockito.when(empruntRepository.findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(1L)).thenReturn(emprunts.subList(0,1));
        Mockito.when(empruntRepository.findAllByCopy_BookIdAndCloturerIsFalseOrderByDateRetourAsc(2L)).thenReturn(emprunts.subList(1,2));

        Mockito.when(bibliService.findBook(1L)).thenReturn(book1);
        Mockito.when(bibliService.findBook(2L)).thenReturn(book2);
        Mockito.when(bibliService.findBook(3L)).thenReturn(book3);
    }

    /**
     * test de la méthode reservationsByUSer
     * entrant: un long, id de l'utilisateur
     * sortant: un Set de reservations
     * attendu: les utilisateurs 1 et 2 ont chacun une réservation
     */
    @Test
    public void reservationByUser_test(){
        Set<Reservation> reservations_user = reservationService.reservervationByUser(1L);
        assertThat(reservations_user.size()).isEqualTo(1);
        Set<Reservation> reservations_admin = reservationService.reservervationByUser(2L);
        assertThat(reservations_admin.size()).isEqualTo(1);
    }

    /**
     * test de la méthode livreReservable
     * entrant: 2 long, l'id de l'utilisateur, l'id du livre
     * sortant: un booléen indiquant si le livre est réservable
     * attendu: aucun livre empruntable car:
     * user a une reservation sur le livre2 et a un emprunt sur le livre1
     * admin a une reservation sur le livre1 et a un emprunt sur le livre2
     */
    @Test
    public  void livreReservable_test(){
        boolean reservable1 = reservationService.livreReservable(1L,1L);
        assertThat(reservable1).isEqualTo(false);
        boolean reservable2 = reservationService.livreReservable(1L,2L);
        assertThat(reservable2).isEqualTo(false);
        boolean reservable4 = reservationService.livreReservable(2L,1L);
        assertThat(reservable4).isEqualTo(false);
        boolean reservable5 = reservationService.livreReservable(2L,2L);
        assertThat(reservable5).isEqualTo(false);

    }
    /**
     * test de la méthode reserverLivre
     * entrant: deux long, l'id de l'utilisateur, l'id du livre
     * sortant: une responseEntity (statut 200 ou 400)
     * attendu: aucun des deux utilisateur ne peut reserver le livre 1 ou 2
     * l'utilisateur 1 peut reserver le livre 3
     */
    @Test
    public  void reserverLivre_test(){
        ResponseEntity response1 = reservationService.reserverLivre(1L,1L);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("reserverLivre_test_response1: "+response1.getBody());

        ResponseEntity response2 = reservationService.reserverLivre(1L,2L);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("reserverLivre_test_response2: "+response2.getBody());

        ResponseEntity response3 = reservationService.reserverLivre(2L,1L);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("reserverLivre_test_response3: "+response3.getBody());

        ResponseEntity response4 = reservationService.reserverLivre(2L,2L);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("reserverLivre_test_response4: "+response4.getBody());

        ResponseEntity response5 = reservationService.reserverLivre(1L,3L);
        assertThat(response5.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("reserverLivre_test_response5: "+response5.getBody());
    }

    /**
     * test de la méthode annuler une réservation
     * entrant:deux long, l'id de l'utilisateur, l'id de la reservation
     * sortant: une responseEntity (statut 200 ou 400)
     * attendu: les utilisateurs peuvent annuler leur reservation,
     * mais ne peuvent pas annuler la reservation de l'autre
     */
    @Test
    public void annulerReservation_test(){
        ResponseEntity response1 = reservationService.annulerReservation(1L,2L);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("annulerReservation_test_response1: "+response1.getBody());

        ResponseEntity response2 = reservationService.annulerReservation(1L,1L);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("annulerReservation_test_response2: "+response2.getBody());

        ResponseEntity response3 = reservationService.annulerReservation(2L,1L);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println("annulerReservation_test_response3: "+response3.getBody());

        ResponseEntity response4 = reservationService.annulerReservation(2L,2L);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("annulerReservation_test_response4: "+response4.getBody());

    }

    /**
     * test de la méthode reservationsByBook
     * entrant: un long, l'id du livre
     * sortant: une liste de reservation
     * attendu: chaque livre possède une reservation
     */
    @Test
    public void reservationsByBook_test(){
        List<Reservation> reservations1 = reservationService.reservationsByBook(1L);
        assertThat(reservations1.size()).isEqualTo(1);
        List<Reservation> reservations2 = reservationService.reservationsByBook(2L);
        assertThat(reservations2.size()).isEqualTo(1);
    }

    /**
     * méthode permettant d'ajouter 4 semaines à une date
     * @param date
     * @return
     */
    public Date ajouter4semaines(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 28);
        return cal.getTime();
    }
}
