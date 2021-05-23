package com.db.services.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.db.services.entity.TradeInfo;
import com.db.services.exceptions.InvalidTradeInfoException;
import com.db.services.exceptions.TradeInfoNotFoundException;
import com.db.services.repository.TradeInfoRepository;

@Transactional
@Service
public class TradeInfoService {
	@Autowired
	TradeInfoRepository tradeInfoRepository;

	public List<TradeInfo> getTradeInfoList(int pageNum) {
		Pageable page = PageRequest.of(pageNum, 50);
		List<TradeInfo> tradeInfoList = tradeInfoRepository.findAll(page).toList();
		return tradeInfoList;
	}

	public TradeInfo getTradeInfoById(Long id) {
		Optional<TradeInfo> tradeInfo = tradeInfoRepository.findById(id);
		if(tradeInfo.isPresent()) {
			return tradeInfo.get();
		}else {
			throw new TradeInfoNotFoundException("Trade info with the id " + id + " is not present");
		}
	}
	
	public TradeInfo getTradeInfoByTradeIdAndVersion(String tradeId, int version) {
		Optional<TradeInfo> tradeInfo = tradeInfoRepository.findByTradeIdAndVersion(tradeId, version);
		if(tradeInfo.isPresent()) {
			return tradeInfo.get();
		}else {
			throw new TradeInfoNotFoundException("Trade info with the trade id " + tradeId + " and version " + version + " is not present");
		}
	}

	public void deleteTradeInfoById(Long id) {
		tradeInfoRepository.deleteById(id);
	}

	public TradeInfo createTradeInfoById(TradeInfo tradeInfo) {
		List<TradeInfo> trade = tradeInfoRepository.findByTradeId(tradeInfo.getTradeId());
		if (!trade.isEmpty()) {
			return modifyTradeInfoById(tradeInfo);
		} else {
			tradeInfo.setCreatedDate(LocalDate.now());
			if (tradeInfo.getCreatedDate().isAfter(tradeInfo.getMaturityDate())) {
				throw new InvalidTradeInfoException(
						"The maturity date can not be the past date. Can not proceed with processing of request");
			} else {
				return tradeInfoRepository.save(tradeInfo);

			}

		}
	}

	public TradeInfo modifyTradeInfoById(TradeInfo tradeInfo) {
		Integer latestVersion = tradeInfoRepository.getMaxVersionForTradeId(tradeInfo.getTradeId());
		if (null == latestVersion || latestVersion == 0) {
			throw new TradeInfoNotFoundException("The trade with the trade id = " + tradeInfo.getId() + " and version "
					+ tradeInfo.getVersion() + " not found. Can not update the tradeInfo");
		} else {
			if (latestVersion > tradeInfo.getVersion()) {
				throw new InvalidTradeInfoException("The trade with the trade id = " + tradeInfo.getTradeId()
						+ " and version " + tradeInfo.getVersion()
						+ " can not be modified, The trade with greater version " + latestVersion + " already present");
			}
			tradeInfo.setCreatedDate(LocalDate.now());
			if (tradeInfo.getCreatedDate().isAfter(tradeInfo.getMaturityDate())) {
				throw new InvalidTradeInfoException(
						"The maturity date can not be the past date. Can not proceed with processing of request");
			}
			if (latestVersion == tradeInfo.getVersion()) {
				Optional<TradeInfo> trade = tradeInfoRepository.findByTradeIdAndVersion(tradeInfo.getTradeId(),
						tradeInfo.getVersion());
				tradeInfo.setId(trade.get().getId());
			}
			return tradeInfoRepository.save(tradeInfo);
		}
	}
}
