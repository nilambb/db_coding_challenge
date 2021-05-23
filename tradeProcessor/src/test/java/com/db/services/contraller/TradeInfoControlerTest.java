package com.db.services.contraller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.db.services.beans.ResonseCode;
import com.db.services.entity.TradeInfo;
import com.db.services.repository.TradeInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TradeInfoControlerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	TradeInfoRepository tradeInfoRepository;

	@Before
	public void setup() {

		TradeInfo trade = new TradeInfo();
		trade.setBookId("B1");
		trade.setVersion(1);
		trade.setTradeId("T1");
		trade.setCounterPartyId("CP-1");
		trade.setMaturityDate(LocalDate.parse("2021-06-20"));
		trade.setExpired('N');
		tradeInfoRepository.save(trade);

		trade = new TradeInfo();
		trade.setBookId("B1");
		trade.setVersion(5);
		trade.setTradeId("T1");
		trade.setCounterPartyId("CP-1");
		trade.setMaturityDate(LocalDate.parse("2021-06-20"));
		trade.setExpired('N');
		tradeInfoRepository.save(trade);

		trade = new TradeInfo();
		tradeInfoRepository.save(trade);
		trade.setBookId("B2");
		trade.setVersion(1);
		trade.setTradeId("T2");
		trade.setCounterPartyId("CP-2");
		trade.setMaturityDate(LocalDate.parse("2020-06-20"));
		trade.setExpired('N');
		tradeInfoRepository.save(trade);

		trade = new TradeInfo();
		tradeInfoRepository.save(trade);
		trade.setBookId("B3");
		trade.setVersion(1);
		trade.setTradeId("T3");
		trade.setCounterPartyId("CP-3");
		trade.setMaturityDate(LocalDate.parse("2022-12-20"));
		trade.setExpired('N');
		tradeInfoRepository.save(trade);

		trade = new TradeInfo();
		tradeInfoRepository.save(trade);
		trade.setBookId("B4");
		trade.setVersion(1);
		trade.setTradeId("T4");
		trade.setCounterPartyId("CP-4");
		trade.setMaturityDate(LocalDate.parse("2019-12-20"));
		trade.setExpired('N');
		tradeInfoRepository.save(trade);

	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void getTradeInfoApiTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/v1/api/tradeInfo").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
		JSONArray result = new JSONArray(content);
		assertTrue(result.length() == 5);
	}

	@Test
	public void storeTradeInfoApiTest() throws Exception {

		String inputJson = "{\"tradeId\":\"T5\",\"version\":1,\"counterPartyId\":\"CP-5\",\"bookId\":\"B5\","
				+ "\"maturityDate\":\"2021-06-20\",\"expired\":\"N\"}";

		MvcResult mvcResult = mockMvc.perform(post("/v1/api/tradeInfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);

	}

	@Test
	public void storeTradeInfoApiInvalidMaturityDateTest() throws Exception {

		String inputJson = "{\"tradeId\":\"T6\",\"version\":1,\"counterPartyId\":\"CP-5\","
				+ "\"bookId\":\"B5\",\"maturityDate\":\"2020-06-20\",\"expired\":\"N\"}";

		MvcResult mvcResult = mockMvc.perform(post("/v1/api/tradeInfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
		assertEquals(response.get("resonseCode"), ResonseCode.BAD_REQUEST.toString());

	}

	@Test
	public void storeTradeInfoApiInvalidVersionTest() throws Exception {

		String inputJson = "{\"tradeId\":\"T1\",\"version\":3,\"counterPartyId\":\"CP-5\","
				+ "\"bookId\":\"B5\",\"maturityDate\":\"2020-06-20\",\"expired\":\"N\"}";

		MvcResult mvcResult = mockMvc.perform(post("/v1/api/tradeInfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
		assertEquals(response.get("resonseCode"), ResonseCode.BAD_REQUEST.toString());

	}

	@Test
	public void getTradeInfoByIDTest() throws Exception {
		mockMvc.perform(get("/v1/api/tradeInfo/1").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

	@Test
	public void getTradeInfoByIDInvalidTest() throws Exception {
		mockMvc.perform(get("/v1/api/tradeInfo/10").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getTradeInfoByTradeIDAndVersionTest() throws Exception {
		mockMvc.perform(get("/v1/api/tradeInfo/T1/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

	@Test
	public void getTradeInfoByTradeIDAndVersionInvalidTest() throws Exception {
		mockMvc.perform(get("/v1/api/tradeInfo/T1/10").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateTradeInfoByIDTest() throws Exception {
		String inputJson = "{\"tradeId\":\"T1\",\"version\":5,\"counterPartyId\":\"CP-5\","
				+ "\"bookId\":\"B5\",\"maturityDate\":\"2021-06-30\",\"expired\":\"N\"}";

		Long id = new JSONObject(mockMvc.perform(get("/v1/api/tradeInfo/T1/5").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString().toString()).getLong("id");

		MvcResult mvcResult = mockMvc.perform(put("/v1/api/tradeInfo/" + id).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
		assertTrue(id.equals(response.getLong("id")));
	}

	@Test
	public void updateTradeInfoByIDInvalidVersionTest() throws Exception {
		String inputJson = "{\"tradeId\":\"T1\",\"version\":3,\"counterPartyId\":\"CP-5\","
				+ "\"bookId\":\"B5\",\"maturityDate\":\"2021-06-30\",\"expired\":\"N\"}";

		Long id = new JSONObject(mockMvc.perform(get("/v1/api/tradeInfo/T1/5").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString().toString()).getLong("id");

		MvcResult mvcResult = mockMvc.perform(put("/v1/api/tradeInfo/" + id).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());
		assertEquals(response.get("resonseCode"), ResonseCode.BAD_REQUEST.toString());
	}

	@Test
	public void deleteTradeInfoByIDTest() throws Exception {
		mockMvc.perform(get("/v1/api/tradeInfo/1").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());

		mockMvc.perform(delete("/v1/api/tradeInfo/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());

		mockMvc.perform(get("/v1/api/tradeInfo/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteTradeInfoByIDInvalidTest() throws Exception {

		mockMvc.perform(delete("/v1/api/tradeInfo/10").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is5xxServerError());

	}
	
	@Test
	public void testExpiryEnableForOldMaturityDateQueryTest() throws Exception {
		String expiredBefore = new JSONObject(mockMvc.perform(get("/v1/api/tradeInfo/T2/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString().toString()).getString("expired");
		
		System.out.println(expiredBefore);
		
		assertEquals(expiredBefore, "N");
		
		tradeInfoRepository.expireOldTradeInfo(LocalDate.now());
		
		String expiredAfter = new JSONObject(mockMvc.perform(get("/v1/api/tradeInfo/T2/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString().toString()).getString("expired");
		
		System.out.println(expiredAfter);
		
		assertEquals(expiredAfter, "Y");
		
	}
}
