package com.assettrader.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties( {"success", "message" })
public class CoinResultDTO  {
	
	
	private CoinDTO[] result;

	public CoinDTO[] getResult() {
		return result;
	}

	public void setResult(CoinDTO[] result) {
		this.result = result;
	}
	
}
