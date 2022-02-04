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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OAuthToken {
	@JsonProperty
	private String accessToken;
	@JsonProperty
	private String tokenType;
	@JsonProperty
	private String refreshToken;
	@JsonProperty
	private int expresIn;
	@JsonProperty
	private String scope;
	@JsonProperty
	private int refreshTokenExpiresIn;
	
	@JsonCreator
	public OAuthToken(@JsonProperty("access_token") String accessToken, 
			@JsonProperty("token_type") String tokenType, 
			@JsonProperty("refresh_token") String refreshToken, 
			@JsonProperty("expires_in") int expresIn, 
			@JsonProperty("scope") String scope,
			@JsonProperty("refresh_token_expires_in") int refreshTokenExpiresIn) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.refreshToken = refreshToken;
		this.expresIn = expresIn;
		this.scope = scope;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
	
	
}
