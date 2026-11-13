package com.example.testit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Status;
import com.example.testit.model.Member;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import com.example.testit.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock 
    private BookRepository bookRepository;

    @Mock 
    private MemberRepository memberRepository;

    @Mock
    private MailService mailService;
    
    @InjectMocks
    private BookService bookService;

    @Test
    public void testBorrowBookavailable() {

        Long idbook = 1L;
        Long idmember = 2L;

        Book book = new Book("Corbeau", "Jean");
        book.setId(idbook);
        
        Member member = new Member("Renard");
        member.setId(idmember);

    // Given : livre disponible
        book.setStatus(Status.DISPONIBLE);

    // When : emprenteur, statut et mail ok 
        when(bookService.findById(idbook))
            .thenReturn(java.util.Optional.of(book));
        when(memberRepository.findById(idmember))
            .thenReturn(java.util.Optional.of(member));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.borrowBook(idbook, idmember);

    // Then : le livre est emprunté, le statut est mis à jour et un mail est envoyé 
        assertEquals(Status.EMPRUNTE, result.getStatus());
        assertEquals(member, result.getBorrower());

        // Le livre a bien été sauvegardé 1 fois en base
        verify(bookRepository, times(1)).save(book);
        // Aucun mail n'est envoyé lors d'un emprunt (selon le code actuel)
        verifyNoInteractions(mailService);
    }
}
