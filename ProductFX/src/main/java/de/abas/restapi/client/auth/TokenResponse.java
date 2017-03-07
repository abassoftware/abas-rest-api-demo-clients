package de.abas.restapi.client.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
{
	access_token=a3e4b370-a13c-482f-9088-2f8a8a47582e, 
	token_type=bearer, 
	refresh_token=f395ad08-dd8e-4164-9365-3cf046d56afe, 
	expires_in=599, 
	scope=abas-fusion openid, 
	id_token=eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6ODA4MCIsInN1YiI6IkpQIiwiaWF0IjoxNDQ3MzIxNjIxLCJsaWMiOiJkNjM4MGVkMS05M2JjLTQ2ZGUtYWM2OC1hYTY5YWZkYTEyZWYifQ.Dm7r2DQ_N0PRfjD6tWpow-DpYsCD4lD97idwt8QKkfyHhZXMyeL5fYuSiu0-b4IaTT2g1DL9mSiwbdxEZ_Bxd_8Nj_0YEyqHDZwp-_yYyKzLMZ5WdbGU5PHGB1TFN4bphADAg51214dFx6rKBT6t-hOd8HH8-Wy_gPOBujIH2wY
}
*/

/**
 * The OAuth Token
 * @author aoezenir
 *
 */
public class TokenResponse {
	@JsonProperty("access_token")
	private String token;

	@JsonProperty("token_type")
	private String bearer;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private String expiry;

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("id_token")
	private String idToken;

	public TokenResponse() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBearer() {
		return bearer;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(".: OAUTH Token information :.").append(System.getProperty("line.separator"));
		sb.append("#############################").append(System.getProperty("line.separator"));
		sb.append("Token: ").append(getToken()).append(System.getProperty("line.separator"));
		sb.append("Expires in: ").append(getExpiry()).append(System.getProperty("line.separator"));
		
		return sb.toString();
	}

}
