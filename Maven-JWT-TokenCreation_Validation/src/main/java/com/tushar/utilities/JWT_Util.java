package com.tushar.utilities;

import java.util.Date;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT_Util{

	private static String issuer="Tushar";
	
	public static String getToken(String id, String subject, String secretKey) {
		return Jwts.builder()
				.setId(id)
				.setSubject(subject)
				.setIssuer(issuer)
				.setIssuedAt(new Date(System.currentTimeMillis()))//Token issue time
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)   ))//Token expiry time
				.signWith(SignatureAlgorithm.HS256,  Base64.getEncoder().encode(secretKey.getBytes()))
				.compact();
	}
	
	public static Claims getClaims(String secretKey, String token) {
		return Jwts.parser().setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static boolean isvalidToken(String secretKey, String token) {
		return (new Date().before(getClaims(secretKey, token).getExpiration()));
	}
	

}
