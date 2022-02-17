package com.ssafy.db.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoProfile {
	@JsonProperty("id")
	public Integer id;
	@JsonProperty("connected_at")
	public String connectedAt;
	@JsonProperty("properties")
	public Properties properties;
	@JsonProperty("kakao_account")
	public KakaoAccount kakaoAccount;

	@Getter
	@Setter
	@ToString
//	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class KakaoAccount {
		@JsonProperty("profile_nickname_needs_agreement")
		public Boolean profileNicknameNeedsAgreement;
		@JsonProperty("profile")
		public Profile profile;
		@JsonProperty("has_email")
		public Boolean hasEmail;
		@JsonProperty("email_needs_agreement")
		public Boolean emailNeedsAgreement;
		@JsonProperty("is_email_valid")
		public Boolean isEmailValid;
		@JsonProperty("is_email_verified")
		public Boolean isEmailVerified;
		@JsonProperty("email")
		public String email;

	}

	@Getter
	@Setter
	@ToString
//	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class Profile {
		@JsonProperty("nickname")
		public String nickname;
	}

	@Getter
	@Setter
	@ToString
//	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class Properties {
		@JsonProperty("nickname")
		public String nickname;
	}
}
