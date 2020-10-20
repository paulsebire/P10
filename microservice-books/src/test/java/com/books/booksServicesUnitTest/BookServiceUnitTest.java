package com.books.booksServicesUnitTest;

import com.books.dao.BookRepository;
import com.books.dao.CopiesRepository;
import com.books.entities.Book;
import com.books.entities.Copy;
import com.books.services.implementations.BookServiceImpl;
import com.books.web.exceptions.BookNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CopiesRepository copiesRepository;

    @Autowired
    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> livres = new ArrayList<>();
    private List<Copy> copies = new ArrayList<>();

    @Before
    public void setUp(){

        Book book1 = new Book("aa","bb","cc");
        book1.setId(1L);
        livres.add(book1);

        Book book2 = new Book("dd","ee","ff");
        book1.setId(2L);
        livres.add(book2);

        Copy copy1=new Copy("01",book1);
        copy1.setId(3L);
        copies.add(copy1);

        Copy copy2=new Copy("02",book1);
        copy2.setId(4L);
        copy2.setDispo(false);
        copies.add(copy2);

        Copy copy3=new Copy("03",book2);
        copy3.setId(5L);
        copies.add(copy3);

        Copy copy4=new Copy("04",book2);
        copy4.setId(6L);
        copy4.setDispo(false);
        copies.add(copy4);

        book1.setCopies(copies.subList(0,2));
        book2.setCopies(copies.subList(2,4));

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(bookRepository.findById(2L)).thenReturn(Optional.of(book2));

        Mockito.when(copiesRepository.findAllByBook_Id(1L)).thenReturn(book1.getCopies());
        Mockito.when(copiesRepository.findAllByBook_Id(2L)).thenReturn(book2.getCopies());

    }

    /**
     * test de la fonction findBookbyID
     * la dao est mocké
     * entrant:un long (id)
     * sortant: on récupère les caractéristique du livre cherché
     * attendu: on recupere bien le livre
     */
    @Test
    public void findBookByID_test(){
        Book book1_test = bookService.findBookByID(1L);
        assertThat(book1_test.getName()).isEqualTo("aa");
        assertThat(book1_test.getAuthor()).isEqualTo("bb");
        assertThat(book1_test.getCoverUrl()).isEqualTo("cc");
        assertThat(book1_test.getCopies().size()).isEqualTo(2);
        assertThat(book1_test.getNbTotalCopys()).isEqualTo(2);

        Book book2_test = bookService.findBookByID(2L);
        assertThat(book2_test.getName()).isEqualTo("dd");
        assertThat(book2_test.getAuthor()).isEqualTo("ee");
        assertThat(book2_test.getCoverUrl()).isEqualTo("ff");
        assertThat(book2_test.getCopies().size()).isEqualTo(2);
        assertThat(book2_test.getNbTotalCopys()).isEqualTo(2);

    }

    /**
     * test de la fonction findBookbyID
     * la dao est mocké
     * entrant:un long (id)
     * sortant: on récupère les caractéristique du livre cherché
     * attendu: le livre est introuvable (id inconnu)
     */
    @Test(expected = BookNotFoundException.class)
    public void findBookById_test_unknownID(){
        Book book = bookService.findBookByID(3L);
        Assertions.assertThrows(BookNotFoundException.class, () -> {bookService.findBookByID(3L);});
    }


}
