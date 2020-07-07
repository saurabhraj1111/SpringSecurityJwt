package com.jocata.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jocata.utils.CustomUserDetailsService;
import com.jocata.utils.JWTUtils;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		

		String header = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		// get User Name by token
		if (header != null) {
			token = header.substring(6);
			userName = jwtUtils.getUsernameFromToken(token);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// get User details
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
			// validate the tokens
			if (jwtUtils.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken userNamePassAuthToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				userNamePassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userNamePassAuthToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
