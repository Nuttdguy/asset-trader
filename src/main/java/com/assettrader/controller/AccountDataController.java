package com.assettrader.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.assettrader.api.bittrex.BittrexClient;
import com.assettrader.api.bittrex.config.ApiCredentials;
import com.assettrader.api.bittrex.model.accountapi.Balance;
import com.assettrader.api.bittrex.model.accountapi.DepositAddress;
import com.assettrader.api.bittrex.model.accountapi.DepositHistoryEntry;
import com.assettrader.api.bittrex.model.accountapi.OrderHistoryEntry;
import com.assettrader.api.bittrex.model.accountapi.WithdrawalHistoryEntry;
import com.assettrader.api.bittrex.model.common.ApiResult;
import com.assettrader.model.coinmarket.Coin;
import com.assettrader.model.coinmarket.Ticker;
import com.assettrader.model.publicapi.DTO.CoinDTO;
import com.assettrader.model.rest.RWLoginDetail;
import com.assettrader.model.rest.ResWrapper;
import com.assettrader.model.utils.ExchangeName;
import com.assettrader.model.view.AccountBalanceView;
import com.assettrader.service.AccountDataService;
import com.assettrader.service.AccountInfoService;
import com.assettrader.service.CoinService;
import com.assettrader.service.DTO.AccountDataServiceDTO;
import com.assettrader.service.DTO.CoinServiceDTO;
import com.assettrader.utils.BittrexKeyUtil;


@CrossOrigin
@RestController
@RequestMapping(value ="/account")
public class AccountDataController {
	
	@Autowired
	private AccountDataServiceDTO accountDataServiceDTO;
	
	@Autowired
	private AccountDataService accountDataService;
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private CoinServiceDTO coinServiceDTO;
	
	//=======================================
	// GENERAL CONTROLLER IMPLEMENTATIONS
	//=======================================
	
	@RequestMapping(value ={ "/balances" }, method = RequestMethod.POST)
	public ApiResult<List<Balance>> getBalances(@RequestBody RWLoginDetail userDetail) {
		
		// KEY IS REQUIRED FOR JOIN TABLE OR CREATING NEW ACCOUNT
		Long userId = userDetail.getId();
		
		// NEED TO INCLUDE USER_PROFILE_ID
		BittrexKeyUtil keys = accountDataService.getApiKey(userDetail);
		
		ApiResult<List<Balance>> balanceApiDTO = 
				initBittrexClient(keys).getAccountApi().getBalances();
		
		return accountDataServiceDTO.saveAllAccountBalancesDTO(addLogoUrlToBalanceDTO(balanceApiDTO), userId);
	}
	
	@RequestMapping(value = "/orderhistory", method = RequestMethod.POST )
	public ApiResult<List<OrderHistoryEntry>> getOrderHistory(@RequestBody RWLoginDetail userDetail) {
		
		BittrexKeyUtil keys = accountDataService.getApiKey(userDetail);
		
		ApiResult<List<OrderHistoryEntry>> orderHistoryDTO =
				initBittrexClient(keys).getAccountApi().getOrderHistory();
		
		return accountDataServiceDTO.saveAllOrderHistoryEntry(addLogoUrlToOrderHistoryDTO(orderHistoryDTO));
	}
	
	
	@RequestMapping(value = "/withdrawalhistory", method = RequestMethod.POST )
	public ApiResult<List<WithdrawalHistoryEntry>> getWithdrawalHistory(@RequestBody RWLoginDetail userDetail) {
		
		BittrexKeyUtil keys = accountDataService.getApiKey(userDetail);
		
		ApiResult<List<WithdrawalHistoryEntry>> withdrawalHistoryDTO = 
				initBittrexClient(keys).getAccountApi().getWithdrawalHistory();
		
		return accountDataServiceDTO.saveAllWithdrawalHistory(addLogoUrlToWithdrawalDTO(withdrawalHistoryDTO));
	}
	
	
	@RequestMapping(value = "/deposithistory", method = RequestMethod.POST )
	public ApiResult<List<DepositHistoryEntry>> getDepositHistory(@RequestBody RWLoginDetail userDetail) {
		
		BittrexKeyUtil keys = accountDataService.getApiKey(userDetail);
		
		ApiResult<List<DepositHistoryEntry>> depositHistoryDTO =
				initBittrexClient(keys).getAccountApi().getDepositHistory();
		
		return accountDataServiceDTO.saveAllDepositHistory(addLogoUrlsToDTOResult(depositHistoryDTO));
	}
	
	// IS WORKING 
	@RequestMapping(value = "/depositaddress/{currency}", method = RequestMethod.GET)
	public ApiResult<DepositAddress> getDepositAddressByCurrency(@PathVariable String currency) {
		
//		ApiResult<DepositAddress> depositAddressDTO = 
//				initBittrexClient().getAccountApi().getDepositAddress(currency);
		
		// TODO -- ENABLE END-POINT FEATURE 
		
		return null; // accountDataServiceDTO.saveDepositAddressDTO(depositAddressDTO);
	}

	
	//=======================================
	// CUSTOM-SPECIFIC CONTROLLER IMPLEMENTATIONS
	//=======================================
	
	
	@RequestMapping(value = {"/balances/{btcprice}&{id}"}, method = RequestMethod.GET)
	public ResWrapper<AccountBalanceView> getPortfolioValue(@PathVariable Double btcprice, @PathVariable Long id) {
		// NEED TO MAKE AN INITIAL REQUEST TO API BALANCES, TO ENSURE THERE IS ALWAYS A DISPLAYED AMOUNT
		// TODO OR ELSE IMPLEMENT BUTTON TO DISPLAY VALUE
		
		// GET CURRENT BALANCE OF ALL COINS
		List<Balance> balances = accountDataService.getAccountBalances(id);

		// GET CURRENT ASK FROM TICKER; USE MARKET_NAME - WILL RETURN A SINGLE VALUE
		List<Ticker> tickerList = new ArrayList<>();
		Double portfolioValue = 0.00;		
		String market = "BTC-";		
		String exchangeName =  ExchangeName.BITTREX.name();
		
		for (Balance currencyName : balances) {		
			String marketName = market + currencyName.getCurrency();
			if (!marketName.equals("BTC-BTC") && currencyName.getCurrency() != null ) {
				Ticker tickerPrice = coinServiceDTO.getTicker(marketName, exchangeName );
				portfolioValue += multiplyValues(currencyName.getBalance(), tickerPrice.getAsk(), btcprice );
				tickerList.add(tickerPrice);
			}

		}
		
		// MULITPLY COIN BALANCE BY BTC PRICE		
		// TODO -- CREATE MODEL FOR STORING PORTFOLIO BALANCE
		ResWrapper<AccountBalanceView> res = new ResWrapper<>();
		AccountBalanceView balanceView = new AccountBalanceView();
		balanceView.setPortfolioValue(portfolioValue);
		
		res.setSuccess(true);
		res.setResult(balanceView);
		res.setMessage("Portfolio was calculated!");
		
		// PERSIST RESULT OF BALANCE TO EACH COIN TO DATABASE FOR GRAPHING INTENT
		// RETURN VALUE OF PORTFOLIO DISPLAY
		return res;
	}
	
	
	//========================================================================
	//   PRIVATE METHOD FOR ADDING LOGO URLS 
	//========================================================================
	
	private Double multiplyValues(Double balance, double ask, Double btcprice) {
		return balance * ask * btcprice;
	}

	private ApiResult<List<Balance>> addLogoUrlToBalanceDTO(ApiResult<List<Balance>> balanceApiDTO) {
		List<Coin> logoList = coinService.getAllCoinLogos();
		
		List<Balance> dtoList = balanceApiDTO.getResult();
		START: for (int i = 0; i < dtoList.size(); i++ ) {
			 for (Coin logo: logoList) {
				 
				String name = dtoList.get(i).getCurrency().toUpperCase();
				if (name.equals(logo.getMarketCurrency())) {
					dtoList.get(i).setLogoUrl(logo.getLogoUrl());
					continue START;
				}
			}
		}
		return balanceApiDTO;
	}
	
	
	private ApiResult<List<OrderHistoryEntry>> addLogoUrlToOrderHistoryDTO(ApiResult<List<OrderHistoryEntry>> orderHistoryDTO) {
		List<Coin> logoList = coinService.getAllCoinLogos();
		
		List<OrderHistoryEntry> dtoList = orderHistoryDTO.getResult();
		START: for (int i = 0; i < dtoList.size(); i++ ) {
			 for (Coin logo: logoList) {
				 
				String name = dtoList.get(i).getExchange().substring(4).toUpperCase();
				if (name.equals(logo.getMarketCurrency())) {
					dtoList.get(i).setLogoUrl(logo.getLogoUrl());
					continue START;
				}
			}
		}
		return orderHistoryDTO;
	}
	
	
	private ApiResult<List<WithdrawalHistoryEntry>> addLogoUrlToWithdrawalDTO(ApiResult<List<WithdrawalHistoryEntry>> withdrawalHistoryDTO) {
		List<Coin> logoList = coinService.getAllCoinLogos();
		
		List<WithdrawalHistoryEntry> dtoList = withdrawalHistoryDTO.getResult();
		START: for (int i = 0; i < dtoList.size(); i++ ) {
			 for (Coin logo: logoList) {
				 
				String name = dtoList.get(i).getCurrency().toUpperCase();
				if (name.equals(logo.getMarketCurrency())) {
					dtoList.get(i).setLogoUrl(logo.getLogoUrl());
					continue START;
				}
			}
		}
		return withdrawalHistoryDTO;
	}
	
	
	private ApiResult<List<DepositHistoryEntry>>  addLogoUrlsToDTOResult(ApiResult<List<DepositHistoryEntry>> depositHistoryDTO) {
		List<Coin> logoList = coinService.getAllCoinLogos();		
		List<DepositHistoryEntry> dtoList = depositHistoryDTO.getResult();
		START: for (int i = 0; i < dtoList.size(); i++ ) {
			 for (Coin logo: logoList) {
				 
				String name = dtoList.get(i).getCurrency().toUpperCase();
				if (name.equals(logo.getMarketCurrency())) {
					dtoList.get(i).setLogoUrl(logo.getLogoUrl());
					continue START;
				}
			}
		}
		return depositHistoryDTO;
	}
	
	
	
	
	//========================================================================
	//   PRIVATE METHOD FOR INITIALIZING KEY AND BITTREX CLIENT FOR ACCOUNT
	//========================================================================
	
	private BittrexClient initBittrexClient(BittrexKeyUtil keys) {
		
		ApiCredentials credentials = new ApiCredentials(
				keys.getKEY(), keys.getSECRET());
		
		return new BittrexClient(credentials);
	}
}