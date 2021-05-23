package com.db.services.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.db.services.entity.TradeInfo;
import com.db.services.service.TradeInfoService;

@RestController
@RequestMapping("/v1/api/tradeInfo")
public class TradeInfoController {
	@Autowired
	TradeInfoService tradeInfoService;

	@GetMapping
	public ResponseEntity<List<TradeInfo>> getTradeInfo(@RequestParam(required = false) Integer pageNum) {
		if (pageNum == null) {
			pageNum = 0;
		}
		List<TradeInfo> tradeInfoList = tradeInfoService.getTradeInfoList(pageNum);
		ResponseEntity<List<TradeInfo>> response = new ResponseEntity<List<TradeInfo>>(tradeInfoList, HttpStatus.OK);
		return response;
	}

	@PostMapping
	public ResponseEntity<TradeInfo> storeTradeInfo(@RequestBody TradeInfo tradeInfo) {
		tradeInfo = tradeInfoService.createTradeInfoById(tradeInfo);
		ResponseEntity<TradeInfo> response = new ResponseEntity<TradeInfo>(tradeInfo, HttpStatus.CREATED);
		return response;
	}

	@PutMapping("/{id}")
	public ResponseEntity<TradeInfo> updateTradeInfo(@RequestBody TradeInfo tradeInfo, @PathVariable String id) {
		// tradeInfo.setId(id);
		tradeInfo = tradeInfoService.modifyTradeInfoById(tradeInfo);
		ResponseEntity<TradeInfo> response = new ResponseEntity<TradeInfo>(tradeInfo, HttpStatus.OK);
		return response;
	}

	@GetMapping("/{id}")
	public ResponseEntity<TradeInfo> getTradeInfoById(@PathVariable Long id) {
		TradeInfo tradeIfno = tradeInfoService.getTradeInfoById(id);
		ResponseEntity<TradeInfo> response = new ResponseEntity<TradeInfo>(tradeIfno, HttpStatus.OK);
		return response;

	}
	
	@GetMapping("/{tradeId}/{version}")
	public ResponseEntity<TradeInfo> getTradeInfoTradeIdAndVersion(@PathVariable String tradeId, @PathVariable int version) {
		TradeInfo tradeIfno = tradeInfoService.getTradeInfoByTradeIdAndVersion(tradeId, version);
		ResponseEntity<TradeInfo> response = new ResponseEntity<TradeInfo>(tradeIfno, HttpStatus.OK);
		return response;

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteTradeInfoById(@PathVariable Long id) {
		tradeInfoService.deleteTradeInfoById(id);
		ResponseEntity<Long> response = new ResponseEntity<Long>(HttpStatus.OK);
		return response;

	}
}
