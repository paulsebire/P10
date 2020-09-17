package com.books;

import com.books.dao.*;
import com.books.entities.*;
import com.books.exceptions.CustomErrorDecoder;
import com.books.services.BibliServiceImpl;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootApplication
@EnableFeignClients("com.books")
@EnableDiscoveryClient
@EnableScheduling
public class BooksApplication {

	@Autowired
	private EmpruntRepository empruntRepository;
	@Autowired
	private BibliServiceImpl bibliService;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CopiesRepository copiesRepository;
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private ReservationRepository reservationRepository;

	public static void main(String[] args) {
		SpringApplication.run(BooksApplication.class, args);
	}

	@PostConstruct
	private void postConstruct() {

			Book book1 = new Book("Le petit prince", "Antoine de Saint-Exupéry",
					"https://images-na.ssl-images-amazon.com/images/I/51WZzUKfHnL._SX330_BO1,204,203,200_.jpg");
			bookRepository.save(book1);

			Book book2 = new Book("Le chat botté", "Charles Perrault",
					"https://images-na.ssl-images-amazon.com/images/I/513GI6j3dIL._SX360_BO1,204,203,200_.jpg");
			bookRepository.save(book2);

			Book book3 = new Book("Les contes de Grimm", "Jacob et Wilhelm Grimm",
					"https://images-na.ssl-images-amazon.com/images/I/51QtHScO4zL._SX258_BO1,204,203,200_.jpg");
			bookRepository.save(book3);

			Book book4 = new Book("Le capital", "Karl Marx",
					"https://images-na.ssl-images-amazon.com/images/I/51gxl55u98L._SX306_BO1,204,203,200_.jpg");
			bookRepository.save(book4);

			Book book5 = new Book("1984", "George Orwell",
					"https://images-na.ssl-images-amazon.com/images/I/91SZSW8qSsL.jpg");
			bookRepository.save(book5);

			Book book6 = new Book("Le meilleur des mondes", "Aldous Huxley",
					"https://images-na.ssl-images-amazon.com/images/I/41C-TnHVegL._SX303_BO1,204,203,200_.jpg");
			bookRepository.save(book6);


			Copy copy1 = new Copy("SN001", book1);
			copiesRepository.save(copy1);
			Copy copy2 = new Copy("SN002", book1);
			copiesRepository.save(copy2);
			Copy copy3 = new Copy("SN003", book1);
			copiesRepository.save(copy3);

			Copy copy4 = new Copy("SN001", book2);
			copiesRepository.save(copy4);
			Copy copy5 = new Copy("SN002", book2);
			copiesRepository.save(copy5);
			Copy copy6 = new Copy("SN003", book2);
			copiesRepository.save(copy6);

			Copy copy7 = new Copy("SN001", book3);
			copiesRepository.save(copy7);
			Copy copy8 = new Copy("SN002", book3);
			copiesRepository.save(copy8);
			Copy copy9 = new Copy("SN003", book3);
			copiesRepository.save(copy9);

			Copy copy10 = new Copy("SN001", book4);
			copiesRepository.save(copy10);
			Copy copy11 = new Copy("SN002", book4);
			copiesRepository.save(copy11);
			Copy copy12 = new Copy("SN003", book4);
			copiesRepository.save(copy12);

			Copy copy13 = new Copy("SN001", book5);
			copiesRepository.save(copy13);
			Copy copy14 = new Copy("SN002", book5);
			copiesRepository.save(copy14);
			Copy copy15 = new Copy("SN003", book5);
			copiesRepository.save(copy15);

			/*Copy copy16 = new Copy("SN001", book6);
			copiesRepository.save(copy16);
			Copy copy17 = new Copy("SN002", book6);
			copiesRepository.save(copy17);*/
			Copy copy18 = new Copy("SN003", book6);
			copiesRepository.save(copy18);

			
			Emprunt emprunt1 = new Emprunt(copy1, new GregorianCalendar(2020, Calendar.FEBRUARY, 24).getTime());
			emprunt1.setDateRetour(bibliService.ajouter4semaines(emprunt1.getDateEmprunt()));
			emprunt1.setIdUtilisateur(3L);
			copy1.setDispo(false);
			copiesRepository.save(copy1);
			empruntRepository.save(emprunt1);

			Emprunt emprunt2 = new Emprunt(copy8, new GregorianCalendar(2020, Calendar.JANUARY, 11).getTime());
			emprunt2.setDateRetour(bibliService.ajouter4semaines(emprunt2.getDateEmprunt()));
			emprunt2.setIdUtilisateur(3L);
			copy8.setDispo(false);
			copiesRepository.save(copy8);
			empruntRepository.save(emprunt2);

			Emprunt emprunt3 = new Emprunt(copy10, new GregorianCalendar(2020, Calendar.MARCH, 16).getTime());
			emprunt3.setDateRetour(bibliService.ajouter4semaines(emprunt3.getDateEmprunt()));
			emprunt3.setIdUtilisateur(3L);
			copy10.setDispo(false);
			copiesRepository.save(copy10);
			empruntRepository.save(emprunt3);

			Emprunt emprunt4 = new Emprunt(copy14, new GregorianCalendar(2020, Calendar.MARCH, 21).getTime());
			emprunt4.setDateRetour(bibliService.ajouter4semaines(emprunt4.getDateEmprunt()));
			emprunt4.setIdUtilisateur(1L);
			copy14.setDispo(false);
			copiesRepository.save(copy14);
			empruntRepository.save(emprunt4);

			Emprunt emprunt5 = new Emprunt(copy18, new GregorianCalendar(2020, Calendar.FEBRUARY, 02).getTime());
			emprunt5.setDateRetour(bibliService.ajouter4semaines(emprunt5.getDateEmprunt()));
			emprunt5.setIdUtilisateur(1L);
			copy18.setDispo(false);
			copiesRepository.save(copy18);
			empruntRepository.save(emprunt5);

			Reservation reservation1= new Reservation(book6);
			reservation1.setIdUtilisateur(3L);
			reservation1.setNotified(true);
			reservation1.setDateNotification(new GregorianCalendar(2020, Calendar.SEPTEMBER, 14,15,30,00).getTime());
			reservationRepository.save(reservation1);

			Reservation reservation2= new Reservation(book6);
			reservation2.setIdUtilisateur(2L);
			reservationRepository.save(reservation2);


			Email email1 = new Email();
		email1.setName("relance");
		email1.setObjet("relance pour livre non rendu");
		email1.setContenu("Bonjour [USERNAME], \n "+
					"\n"+
					"\tVous deviez rendre le livre [LIVRE_TITRE] à la blibliothèque au plus tard à la date : [DATE_FIN].\n" +
					"à ce jour nous n'avons toujours pas enregistré le retour de ce livre.\n" +
					"Nous vous invitons à régulariser la situation dès à présent.\n" +
					"\n"+
					"Cordialement.");

			emailRepository.save(email1);
		Email email2 = new Email();
		email2.setName("notification");
		email2.setObjet("notification de disponiblité");
		email2.setContenu("Bonjour [USERNAME], \n "+
				"\n"+
				"\tBonjour, le livre [LIVRE_TITRE] que vous avez réservé est de nouveau disponible à la blibliothèque .\n" +
				"Vous disposez de 48h à partir du [DATE_RENDU] pour venir retirer votre exemplaire, passé ce délai vous sortirez de la liste d'attente.\n" +
				"Dans l'attente de votre visite.\n" +
				"\n"+
				"Cordialement.");

		emailRepository.save(email2);
	}



}
