package com.example.testit;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class BookRepositoryIT {

    @Autowired 
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test 
    void findByMemberAndStatus_ShouldReturn_1_Alice() {

        Member alice = new Member();
        alice.setUsername("Alice");
        alice = memberRepository.save(alice);

        Member bob = new Member();
        bob.setUsername("Bob");
        bob = memberRepository.save(bob);

        // Alice
        Book book1 = new Book("1984", "George Orwell");
        book1.setBorrower(alice);
        book1.setStatus(Status.EMPRUNTE);
        bookRepository.save(book1);

        Book book2 = new Book("Fondation", "Isaac Asimov");
        book2.setBorrower(alice);
        book2.setStatus(Status.RENDU);
        bookRepository.save(book2);

        // Bob
        Book book3 = new Book("Le Seigneur des Anneaux", "Tolkien");
        book3.setBorrower(bob);
        book3.setStatus(Status.EMPRUNTE);
        bookRepository.save(book3);

        // Nobody
        Book book4 = new Book("Clean Code", "Robert C. Martin");
        book4.setStatus(Status.DISPONIBLE);
        bookRepository.save(book4);

        List<Book> result = bookRepository.findByMemberAndStatus(alice.getId(), Status.EMPRUNTE);

        assertEquals(1, result.size());
        assertNotEquals(result, book2);
        assertNotEquals(result, book3);
        assertTrue(result.contains(book1));
        assertFalse(result.get(0).getBorrower().equals(bob));

    }
}
