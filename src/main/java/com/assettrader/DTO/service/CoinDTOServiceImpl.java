package com.assettrader.DTO.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assettrader.DTO.CoinDTO;
import com.assettrader.DTO.CoinResultDTO;
import com.assettrader.DTO.CurrencyDTO;
import com.assettrader.DTO.CurrencyResultDTO;
import com.assettrader.DTO.dao.CoinDTODao;
import com.assettrader.model.coin.Coin;
import com.assettrader.model.coin.Currency;
import com.assettrader.model.coin.MarketHistory;
import com.assettrader.model.coin.MarketSummary;
import com.assettrader.model.coin.OrderBook;
import com.assettrader.model.coin.Ticker;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Service
public class CoinDTOServiceImpl implements CoinDTOService {

	private static final String PUBLIC_URL = "https://bittrex.com/api/v1.1/public";
	private static final String GET_MARKETS = "/getmarkets";
	private static final String GET_CURRENCIES = "/getcurrencies";
	private static final String GET_MARKET_SUMMARIES = "/getmarketsummaries";
	private static final String GET_TICKER = "/getticker";
	private static final String GET_MARKET_SUMMARY = "/getmarketsummary";
	private static final String GET_ORDER_BOOK = "/getorderbook";
	
	@Autowired
	CoinDTODao coinDTODao;
	
	//==||  METHODS :: TO REQUEST DATA FROM API END-POINT
	//================================================
	public List<Coin> getMarkets() {

		List<Coin> coinList = null;
		ObjectMapper mapper = initMapper();

		try {
			URL url = new URL(PUBLIC_URL + GET_MARKETS);
			List<CoinResultDTO> result = mapper.readValue(url, 
					new TypeReference<List<CoinResultDTO>>() {
			});
			
			coinList = new ArrayList<>();
			for (CoinDTO coinDTO : result.get(0).getResult()) {
				coinList.add(coinDTOToCoin(coinDTO)); 
			}
			
			coinDTODao.saveGetMarkets(coinList); // PERSIST DATA TO DATABASE
			return coinList; // RETURN MAPPED RESULT TO UI TO DISPLAY

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} finally {
			
		}

		return coinList;

	}

	@Override
	public List<Currency> getCurrencies() {
		List<Currency> currencyList = null;
		ObjectMapper mapper = initMapper();
		
		try {
			URL url = new URL(PUBLIC_URL + GET_CURRENCIES);
			List<CurrencyResultDTO> jsonList = mapper.readValue(
					url, new TypeReference<List<CurrencyResultDTO>>() {				
			});
			
			currencyList = new ArrayList<>();
			for (CurrencyDTO currency : jsonList.get(0).getResult()) {
				currencyList.add(currencyDTOToCoin(currency));
			}
			
			coinDTODao.saveGetCurrencies(currencyList);
			return currencyList;
		} catch(IOException io) {
			System.out.println("IOException getting currencyList " + io.getMessage());
		}
		
		
		return null;
	}

	@Override
	public List<MarketSummary> getMarketSummaries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketSummary getMarketSummary(String marketName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ticker getTicker(String marketName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MarketHistory> getMarketHistory(String marketName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderBook> getOrderBook(String marketName, String buyOrSell) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//==||  METHODS :: FOR DATA-TRANSFER  
	//================================================
	
	private ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		return mapper;
	}
	
	private Coin coinDTOToCoin(CoinDTO coinDTO) {

		Coin coin = new Coin();
		coin.setMarketName(coinDTO.getMarketName());
		coin.setBaseCurrency(coinDTO.getBaseCurrency());
		coin.setBaseCurrencyLong(coinDTO.getBaseCurrencyLong());
		coin.setMarketCurrency(coinDTO.getMarketCurrency());
		coin.setMarketCurrencyLong(coinDTO.getMarketCurrencyLong());
		coin.setActive(coinDTO.isIsActive());
		coin.setCreateDate(coinDTO.getCreateDate());
		coin.setLogoUrl(coinDTO.getLogoUrl());
		coin.setMinTradeSize(coinDTO.getMinTradeSize());
		coin.setCreated(coinDTO.getCreated());
		coin.setNotice(coinDTO.getNotice());
		return coin;
	}

	private Currency currencyDTOToCoin(CurrencyDTO currencyDTO) {
		
		Currency currency = new Currency();
		currency.setCurrency(currencyDTO.getCurrency());
		currency.setCurrencyLong(currencyDTO.getCurrencyLong());
		currency.setMinConfirmation(currencyDTO.getMinConfirmation());
		currency.setTxFee(currencyDTO.getTxFee());
		currency.setActive(currencyDTO.isIsActive());
		currency.setCoinType(currencyDTO.getCoinType());
		currency.setBaseAddress(currencyDTO.getBaseAddress());
		return currency;
	}
	
	
}
