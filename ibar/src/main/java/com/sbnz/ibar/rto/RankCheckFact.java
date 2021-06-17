package com.sbnz.ibar.rto;

import com.sbnz.ibar.model.Rank;
import com.sbnz.ibar.model.Reader;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankCheckFact {
    private Reader reader;
    private Rank rank;
    private boolean isHigher;
}
