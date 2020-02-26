package com.internousdev.rose.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.rose.dao.CartInfoDAO;
import com.internousdev.rose.dao.UserInfoDAO;
import com.internousdev.rose.dto.CartInfoDTO;
import com.internousdev.rose.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {

	private String userId;
	private String password;
	private boolean saveUserFlg;
	private Map<String,Object> session;
	private List<String> userIdErrorMessageList;
	private List<String> passwordErrorMessageList;
	private String isNotUserInfoMessage;
	private ArrayList<CartInfoDTO> cartItemList;
	private int totalPrice;

	public String execute() throws SQLException {
		String result = ERROR;
		UserInfoDAO userInfoDAO = new UserInfoDAO();

		session.remove("saveUserFlg");
		//  新規ユーザー登録で保持したユーザーIDを取得できた場合
		if(session.containsKey("createUserFlg")
				&& Integer.parseInt(session.get("createUserFlg").toString())==1){
//			新規ユーザー登録したユーザー名を代入
			userId = session.get("userIdForCreateUser").toString();

			session.remove("userIdForCreateUser");
			session.remove("createUserFlg");
		}else {
			//正規表現(未入力、文字種、文字数チェック)
			InputChecker inputChecker = new InputChecker();
			userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false);
			passwordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false);

			if(userIdErrorMessageList.size()>0
					|| passwordErrorMessageList.size()>0) {

				session.put("loginFlg", false);
				return result;
			}

			//ログイン認証処理
			if(!userInfoDAO.isExistsUserInfo(userId,password)) {
				isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";
				return result;
			}
		}

		//セッションタイムアウト
		if(!session.containsKey("proUserId")) {
			return "sessionTimeout";
		}

		//仮ユーザーIDに一致するカート情報紐づけ
		String proUserId = session.get("proUserId").toString();
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		//DAOのgetCartInfoメソッドに仮ユーザーID(proUserId)を引数で渡して実行
		ArrayList<CartInfoDTO> cartInfoListForProUser = new ArrayList<CartInfoDTO>();
		cartInfoListForProUser = cartInfoDAO.getCartInfo(proUserId);

		if(cartInfoListForProUser != null && cartInfoListForProUser.size()>0) {
			boolean cartResult = changeCartInfo(cartInfoListForProUser, proUserId);
			if(!cartResult) {

				return "DBerror";
			}

		}
//      ユーザーID、ログインフラグ保存、仮ユーザーID削除
		session.put("userId", userId);
		session.put("loginFlg", true);
		if(saveUserFlg) {
			session.put("saveUserFlg", true);
		}
		session.remove("proUserId");

		//		カートフラグありの場合、cart.jspに遷移
		if(session.containsKey("cartFlg")
				&& Integer.parseInt(session.get("cartFlg").toString())==1) {
			session.remove("cartFlg");
			cartItemList = cartInfoDAO.getCartInfo(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
			result = "cart";
		}else {
			result = SUCCESS;
		}

		return result;

	}

	//  本ユーザーIDに紐づくカート情報があるかどうか
	private boolean changeCartInfo(ArrayList<CartInfoDTO> cartInfoListForProUser, String proUserId) throws SQLException {
		int count = 0;
		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		boolean result = false;

		for(CartInfoDTO dto : cartInfoListForProUser) {
		//   あった際の処理(個数の更新)
			if(cartInfoDAO.isExistCartInfo(String.valueOf(dto.getProductId()),userId)) {
				count += cartInfoDAO.updateProductCount(String.valueOf(dto.getProductCount()),userId, String.valueOf(dto.getProductId()));
				//仮ユーザーID削除
				cartInfoDAO.deleteCartItem(String.valueOf(dto.getProductId()),proUserId);
				//  なかった際の処理(仮ユーザーIDをユーザーIDに書き換え
			}else {
				count += cartInfoDAO.linkToUserId(proUserId, userId, String.valueOf(dto.getProductId()));
			}
		}

		if(count == cartInfoListForProUser.size()) {
			result = true;
		}
		return result;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSaveUserFlg() {
		return saveUserFlg;
	}

	public void setSaveUserFlg(boolean saveUserFlg) {
		this.saveUserFlg = saveUserFlg;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}

	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}

	public List<String> getPasswordErrorMessageList() {
		return passwordErrorMessageList;
	}

	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList) {
		this.passwordErrorMessageList = passwordErrorMessageList;
	}

	public String getIsNotUserInfoMessage() {
		return isNotUserInfoMessage;
	}

	public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
		this.isNotUserInfoMessage = isNotUserInfoMessage;
	}

	public List<CartInfoDTO> getCartItemList() {
		return cartItemList;
	}

	public void setCartItemList(ArrayList<CartInfoDTO> cartItemList) {
		this.cartItemList = cartItemList;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
