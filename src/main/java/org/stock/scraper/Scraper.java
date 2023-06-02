package org.stock.scraper;

import org.stock.model.Company;
import org.stock.model.ScrapResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);

    ScrapResult scrap(Company company);
}
