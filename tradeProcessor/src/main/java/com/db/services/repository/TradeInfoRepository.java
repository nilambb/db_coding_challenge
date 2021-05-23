package com.db.services.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.db.services.entity.TradeInfo;

@Transactional
public interface TradeInfoRepository extends PagingAndSortingRepository<TradeInfo, Long> {

	@Query(value = "select max(version) FROM trade_info where tradeId = :tradeId")
	Integer getMaxVersionForTradeId(@Param("tradeId") String tradeId);

	Optional<TradeInfo> findByTradeIdAndVersion(String tradeId, int version);
	
	//User findFirstByIdOrderByPointPointsDesc(int userId)
	
	@Modifying
	@Query("update trade_info u set u.expired = 'Y' where u.maturityDate < :date")
	void expireOldTradeInfo(@Param("date") LocalDate date);

	List<TradeInfo> findByTradeId(String tradeId);
}
