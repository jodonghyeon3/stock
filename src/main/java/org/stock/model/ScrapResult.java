package org.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapResult {

    private Company company;
    private List<Dividend> dividends;

    public ScrapResult() {
        this.dividends = new ArrayList<>();
    }
}
