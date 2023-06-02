package org.stock.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stock.model.Company;
import org.stock.model.ScrapResult;
import org.stock.model.constants.CacheKey;
import org.stock.persist.CompanyRepository;
import org.stock.persist.DividendRepository;
import org.stock.persist.entity.CompanyEntity;
import org.stock.persist.entity.DividendEntity;
import org.stock.scraper.Scraper;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

//    @Scheduled(fixedDelay = 1000)
//    public void test1() throws InterruptedException{
//        Thread.sleep(10000);
//        System.out.println("test 1 -> " + Thread.currentThread().getName()+LocalDateTime.now());
//
//
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void test2() throws InterruptedException{
//        System.out.println("test 2 -> " + Thread.currentThread().getName()+LocalDateTime.now());
//    }

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping is started ");
        //저장되어 있는 회사 목록 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();
        //회사마다 배당금 정보를 새로 스크랩핑
        for(var company : companies){
            log.info("scraped company -> " +company.getName());
            ScrapResult scrapResult = this.yahooFinanceScraper.scrap(new Company( company.getName(), company.getTicker()));


            //스크래핑한 배당금 정보중 데이터 베이스에 없는 경우 저장
            scrapResult.getDividends().stream()
                    //디비든 모델을 디비든 엔티티로 매핑
                    .map(e->new DividendEntity(company.getId(),e))
                    //엘리먼트를 하나씩 디비든 레파지토리에 삽입(존재하지 않을경우에만)
                    .forEach(e->{
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists){
                            this.dividendRepository.save(e);
                            log.info("insert new dividend -> "+ e.toString());
                        }
                    });
            //연속적인 스크랩핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000);//3 초
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
