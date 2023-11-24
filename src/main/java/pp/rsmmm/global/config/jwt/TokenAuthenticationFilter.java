package pp.rsmmm.global.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    public final static String HEADER_AUTHOR = "Authorization"; // JWT는 HTTP 요청의 Authorization 헤더에 포함
    public final static String TOKEN_PREFIX = "Bearer "; //JWT 토큰은 "Bearer"라는 프리픽스와 함께 전송

    /**
     * HTTP 요청을 필터링하고 토큰을 검증하여 인증 정보를 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤터의 AUTHORIZATION 키 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHOR);

        if (authorizationHeader != null && !authorizationHeader.equalsIgnoreCase("")) {
            // 가져온 값에서 TOKEN_PREFIX "Bearer "를 제거한 실제 토큰값
            String token = getAccessToken(authorizationHeader);

            if (tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header의 인증 토큰을 확인하여 TOKEN_PREFIX 제거하고 토큰 반환
     */
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
