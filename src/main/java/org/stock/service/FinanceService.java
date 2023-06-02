package org.stock.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.stock.exception.imple.NoCompanyException;
import org.stock.model.Company;
import org.stock.model.Dividend;
import org.stock.model.ScrapResult;
import org.stock.model.constants.CacheKey;
import org.stock.persist.CompanyRepository;
import org.stock.persist.DividendRepository;
import org.stock.persist.entity.CompanyEntity;
import org.stock.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;


    @Cacheable(key="#companyName",value= CacheKey.KEY_FINANCE)
    public ScrapResult getDividendByCompanyName(String companyName){
        log.info("search Company -> " +companyName);
        //1. 회사명을 기준으로 회사정보 조회
        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(()->new NoCompanyException());

        //2. 조회된 회사 ID로 배당금 조회
        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        //3. 조회된 회사 정보/배당급 정보 조합하여 Scrap Result 로 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e->new Dividend(e.getDate(),e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapResult(new Company(company.getTicker(),company.getName()),dividends);

    }
}
