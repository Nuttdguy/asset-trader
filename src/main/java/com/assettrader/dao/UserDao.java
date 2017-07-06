package com.assettrader.dao;

import java.util.List;

import com.assettrader.model.Address;
import com.assettrader.model.Credential;
import com.assettrader.model.UserProfile;
import com.assettrader.model.coinmarket.Coin;
import com.assettrader.model.rest.RWApiCredential;
import com.assettrader.model.rest.RWFavorite;
import com.assettrader.model.rest.RWLoginDetail;


public interface UserDao {

	boolean saveCoinAsFavorite(RWFavorite userFav);
	boolean saveApiKey(RWApiCredential credential);
	
	void updateUsername(String newUsername);
	void updatePassword(String newPassword);
	void updateUserAddress(Address newAddress);
	void updateIsActive();
	
	void deleteUser(UserProfile userProfile);
	void deleteFavoriteCoin(int favoriteId);
	
	UserProfile registerUser(UserProfile newUser);
	RWLoginDetail loginUser(String username, String password);
	UserProfile getUser(int profileId);
	UserProfile getUser(String searchParam);
	Address getUserAddress(int profileId);
	
	List<Coin> getUserFavoriteCoins(int profileId);
	
	boolean checkIfUserExists(String username);
	
}
