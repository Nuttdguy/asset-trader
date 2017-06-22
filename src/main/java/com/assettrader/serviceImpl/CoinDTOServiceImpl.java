package com.assettrader.serviceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.assettrader.model.DTO.CoinDTO;
import com.assettrader.model.DTO.CoinResultDTO;
import com.assettrader.model.coin.Coin;
import com.assettrader.service.CoinDTOService;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Service
public class CoinDTOServiceImpl implements CoinDTOService {

	private static final String PUBLIC_URL = "https://bittrex.com/api/v1.1/public/getmarkets";
	
	public List<Coin> refreshCoinMarketsURLImpl() {

		List<Coin> coinList = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

		try {
			URL url = new URL(PUBLIC_URL);
			List<CoinResultDTO> result = mapper.readValue(url, 
					new TypeReference<List<CoinResultDTO>>() {
			});
			
			coinList = new ArrayList<>();
			for (CoinDTO coinDTO : result.get(0).getResult()) {
				coinList.add(coinDTOToCoin(coinDTO)); 
			}
			return coinList;

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}

		return coinList;

	}

	Coin coinDTOToCoin(CoinDTO coinDTO) {

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
}
