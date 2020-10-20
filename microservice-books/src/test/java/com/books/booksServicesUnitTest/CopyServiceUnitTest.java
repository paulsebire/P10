package com.books.booksServicesUnitTest;

import com.books.dao.CopiesRepository;
import com.books.entities.Book;
import com.books.entities.Copy;
import com.books.entities.Emprunt;
import com.books.entities.Reservation;
import com.books.services.implementations.CopyServiceImpl;
import com.books.services.interfaces.CopyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CopyServiceUnitTest {

    @Mock
    private CopiesRepository copiesRepository;

    @InjectMocks
    @Autowired
    private CopyServiceImpl copyService;


    private List<Copy> copiesDispo = new ArrayList<>();


    @Before
    public void setUp(){
        Book book1 = new Book("aa","bb","cc");
        book1.setId(1L);


        Book book2 = new Book("dd","ee","ff");
        book1.setId(2L);


        Copy copy1=new Copy("01",book1);
        copy1.setId(3L);
        copiesDispo.add(copy1);

        Copy copy2=new Copy("02",book1);
        copy2.setId(4L);
        copy2.setDispo(false);
        copiesDispo.add(copy2);

        Copy copy3=new Copy("03",book2);
        copy3.setId(5L);
        copy3.setEmprunts(new HashSet<>());
        copiesDispo.add(copy3);

        Copy copy4=new Copy("04",book2);
        copy4.setId(6L);
        copy4.setEmprunts(new HashSet<>());
        copiesDispo.add(copy4);

        book1.setCopies(copiesDispo.subList(0,2));
        book2.setCopies(copiesDispo.subList(2,4));


        Mockito.when(copiesRepository.ListCopyDispoByBook(1L)).thenReturn(copiesDispo.subList(0,1));
        Mockito.when(copiesRepository.ListCopyDispoByBook(2L)).thenReturn(copiesDispo.subList(2,4));

    }

    /**
     * test de la fonction list copyDispoByBook_ID
     * la dao est mocké
     * entrant: unh long, id du livre
     * sortant: une liste de copies
     * attendu: on recupère une liste pour chaque id de tailles différentes
     */
    @Test
    public void listCopyDispoByBookID_test(){
        List<Copy> copiesDispo_book1 = copyService.listCopyDispoByBookID(1L);
        assertThat(copiesDispo_book1.size()).isEqualTo(1);
        List<Copy> copiesDispo_book2 = copyService.listCopyDispoByBookID(2L);
        assertThat(copiesDispo_book2.size()).isEqualTo(2);
    }

}
