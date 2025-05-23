package pProject.pPro.securityConfig;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pProject.pPro.User.DTO.UserDTO;
import pProject.pPro.securityConfig.exception.FilterErrorCode;
import pProject.pPro.securityConfig.exception.FilterException;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("access")) {
					token = cookie.getValue();
					break;
				}
			}
		}

		String requestURI = request.getRequestURI();
		// 허용할 경로는 무조건 필터를 통과시킴
		boolean isAllowedPath = isPublicPath(requestURI);
		if (token == null && isAllowedPath) {
			filterChain.doFilter(request, response);
			return;
		}
		// 여기서부터는 토큰이 필수적임
		if (token == null) {
			sendErrorResponse(response, "로그인이 필요합니다.", HttpServletResponse.SC_FORBIDDEN); // 403
			return;
		}

		try {
			jwtUtil.isExpired(token);
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			sendErrorResponse(response, "Access 토큰이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED); // 401
			return;
		}


		// 인증 정보 등록
		String email = jwtUtil.getEmail(token);
		String role = jwtUtil.getRole(token);

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		userDTO.setRole(role);
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
				customOAuth2User.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

	private boolean isPublicPath(String requestURI) {
		return requestURI.startsWith("/post") || requestURI.startsWith("/uploads/") || requestURI.equals("/signup")
				|| requestURI.equals("/signup/confirm") || requestURI.startsWith("/login") 
				|| requestURI.startsWith("/find") || requestURI.equals("/auth/getToken")
				|| requestURI.equals("/ws-stomp") || requestURI.startsWith("/ws-stomp")
				|| requestURI.equals("/chatRoom/search");
	}
	private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
		response.setStatus(statusCode);
		response.setContentType("application/json;charset=UTF-8");
		String json = new com.fasterxml.jackson.databind.ObjectMapper()
				.writeValueAsString(pProject.pPro.global.CommonResponse.fail(message));

		response.getWriter().write(json);
	}

}
