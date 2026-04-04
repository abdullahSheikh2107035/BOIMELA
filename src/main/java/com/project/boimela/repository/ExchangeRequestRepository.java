package com.project.boimela.repository;

import com.project.boimela.entity.ExchangeRequest;
import com.project.boimela.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findByBuyer(User buyer);

    @Query("SELECT er FROM ExchangeRequest er WHERE er.book.seller = :seller")
    List<ExchangeRequest> findByBookSeller(@Param("seller") User seller);
}
