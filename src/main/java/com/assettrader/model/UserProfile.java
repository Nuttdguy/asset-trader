package com.assettrader.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "USER_PROFILE")
public class UserProfile extends Person {

	@Id
	@Column(name = "USER_PROFILE_ID" )
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;

	@Column(name = "USERNAME", unique= true)
	private String username;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETE_DATE")
	private Date deletedDate;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "userProfile")
	private Credential credential;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile")
	private List<ApiCredential> apiCredentials;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<Account> Account;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<SocialNetwork> socialNetwork;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<UserCoinFavorite> userCoinFavorite;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<Address> address;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<BalanceHistory> balanceHistories;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<ReturnValue> returnHistories;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userProfile")
	private List<EmailProvider> emailProvider;

	public UserProfile() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public List<ApiCredential> getApiCredentials() {
		return apiCredentials;
	}

	public void setApiCredentials(List<ApiCredential> apiCredentials) {
		this.apiCredentials = apiCredentials;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<Account> getAccount() {
		return Account;
	}

	public void setAccount(List<Account> account) {
		Account = account;
	}

	public List<EmailProvider> getEmailProvider() {
		return emailProvider;
	}

	public void setEmailProvider(List<EmailProvider> emailProvider) {
		this.emailProvider = emailProvider;
	}

	public List<SocialNetwork> getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(List<SocialNetwork> socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public List<UserCoinFavorite> getUserCoinFavorite() {
		return userCoinFavorite;
	}

	public void setUserCoinFavorite(List<UserCoinFavorite> userCoinFavorite) {
		this.userCoinFavorite = userCoinFavorite;
	}

	public List<BalanceHistory> getBalanceHistories() {
		return balanceHistories;
	}

	public void setBalanceHistories(List<BalanceHistory> balanceHistories) {
		this.balanceHistories = balanceHistories;
	}

	public List<ReturnValue> getReturnHistories() {
		return returnHistories;
	}

	public void setReturnHistories(List<ReturnValue> returnHistories) {
		this.returnHistories = returnHistories;
	}

}
