package com.assettrader.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.assettrader.api.bittrex.model.accountapi.Balance;
import com.assettrader.api.bittrex.model.accountapi.DepositHistoryEntry;
import com.assettrader.api.bittrex.model.accountapi.OrderHistoryEntry;
import com.assettrader.api.bittrex.model.accountapi.WithdrawalHistoryEntry;
import com.assettrader.api.bittrex.model.accountapi.WithdrawalRequested;
import com.assettrader.entities.ids.AccountId;

@Entity
@Table(name = "ACCOUNTS")
@IdClass(AccountId.class)
public class Account {

	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ACCOUNT_ID", columnDefinition="serial")
	private Long id;

	@Id
	@Column(name = "EXCHANGE_NAME")
	private String exchangeName;

	@Id
	@Column(name = "CURRENCY")
	private String currency;

	@Id
	@ManyToOne
	@JoinColumn(name = "USER_PROFILE_ID")
	private UserProfile userProfile;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	private List<DepositHistoryEntry> depositHistoryEntry;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, mappedBy="account") private
	 * List<DepositAddress> depositAddresses; // CAN HAVE MANY DEPOSIT ADDRESSES
	 */

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	private List<Balance> balances;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private List<OrderHistoryEntry> orderHistory;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private List<WithdrawalHistoryEntry> withdrawHistoryEntry;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private List<WithdrawalRequested> withdrawalRequested;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<DepositHistoryEntry> getDepositHistoryEntry() {
		return depositHistoryEntry;
	}

	public void setDepositHistoryEntry(List<DepositHistoryEntry> depositHistoryEntry) {
		this.depositHistoryEntry = depositHistoryEntry;
	}

	public List<Balance> getBalances() {
		return balances;
	}

	public void setBalances(List<Balance> balances) {
		this.balances = balances;
	}

	public List<OrderHistoryEntry> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(List<OrderHistoryEntry> orderHistory) {
		this.orderHistory = orderHistory;
	}

	public List<WithdrawalHistoryEntry> getWithdrawHistoryEntry() {
		return withdrawHistoryEntry;
	}

	public void setWithdrawHistoryEntry(List<WithdrawalHistoryEntry> withdrawHistoryEntry) {
		this.withdrawHistoryEntry = withdrawHistoryEntry;
	}

	public List<WithdrawalRequested> getWithdrawalRequested() {
		return withdrawalRequested;
	}

	public void setWithdrawalRequested(List<WithdrawalRequested> withdrawalRequested) {
		this.withdrawalRequested = withdrawalRequested;
	}

}
