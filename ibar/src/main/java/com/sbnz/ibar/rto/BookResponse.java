package com.sbnz.ibar.rto;

import com.sbnz.ibar.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Book book;
    private double points = 0;

    public void setPoints(double points) {
        if (points < 0) {
            this.points = 0;
        } else {
            this.points = points;
        }
    }
}
