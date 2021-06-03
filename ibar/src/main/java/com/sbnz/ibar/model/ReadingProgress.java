package com.sbnz.ibar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Reader reader;

    private long progress = 0;

    private Instant lastOpened;

    public ReadingProgress(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
        lastOpened = Instant.now();
    }

    public double getPercentage() {
        return (double) this.progress / this.book.getQuantity() * 100;
    }
}
