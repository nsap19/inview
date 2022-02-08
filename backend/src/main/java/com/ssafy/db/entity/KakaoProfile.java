package com.ssafy.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class KakaoProfile {
	@SerializedName("id")
	@Expose
	public Integer id;
	@SerializedName("connected_at")
	@Expose
	public String connectedAt;
	@SerializedName("properties")
	@Expose
	public Properties properties;
	@SerializedName("kakao_account")
	@Expose
	public KakaoAccount kakaoAccount;

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public class KakaoAccount {
		@SerializedName("profile_nickname_needs_agreement")
		@Expose
		public Boolean profileNicknameNeedsAgreement;
		@SerializedName("profile")
		@Expose
		public Profile profile;
		@SerializedName("has_email")
		@Expose
		public Boolean hasEmail;
		@SerializedName("email_needs_agreement")
		@Expose
		public Boolean emailNeedsAgreement;
		@SerializedName("is_email_valid")
		@Expose
		public Boolean isEmailValid;
		@SerializedName("is_email_verified")
		@Expose
		public Boolean isEmailVerified;
		@SerializedName("email")
		@Expose
		public String email;

	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public class Profile {
		@SerializedName("nickname")
		@Expose
		public String nickname;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public class Properties {
		@SerializedName("nickname")
		@Expose
		public String nickname;
	}
}
