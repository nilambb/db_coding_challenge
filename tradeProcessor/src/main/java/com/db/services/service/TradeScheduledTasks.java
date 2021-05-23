package com.db.services.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.db.services.repository.TradeInfoRepository;

@Component
@Transactional
@Profile("!test")
public class TradeScheduledTasks {
	
	@Autowired
	TradeInfoRepository tradeInfoRepository;
	
	private static final Logger log = LoggerFactory.getLogger(TradeScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron="0 0 6 * * *")
	//This job will run everyday at 6 am
	public void markPastJobsAsExpire() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		tradeInfoRepository.expireOldTradeInfo(LocalDate.now());
		log.info("Marked the old trades as expired");
	}
}
