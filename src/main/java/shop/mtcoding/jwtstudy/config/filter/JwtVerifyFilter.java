package shop.mtcoding.jwtstudy.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import shop.mtcoding.jwtstudy.config.auth.JwtProvider;
import shop.mtcoding.jwtstudy.config.auth.LoginUser;

public class JwtVerifyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String prefixJwt = req.getHeader(JwtProvider.HEADER);
        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");

        try {
            DecodedJWT decodedJwt = JwtProvider.verify(jwt);
            int id = decodedJwt.getClaim("id").asInt();
            String role = decodedJwt.getClaim("role").asString();

            HttpSession session = req.getSession();
            LoginUser loginUser = LoginUser.builder().id(id).role(role).build();
            session.setAttribute("loginUser", loginUser);
            chain.doFilter(req, resp);

        } catch (SignatureVerificationException e) {
            resp.setStatus(401);
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("로그인 다시!");
        } catch (TokenExpiredException e2) {
            resp.setStatus(401);
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("로그인 다시!");

        }

    }

}
