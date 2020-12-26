package ru.job4j.todo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class AuthFilter.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 28.12.2020
 */
@WebFilter("*.do")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        String uri = req.getRequestURI();
        if (uri.endsWith("auth.do") || uri.endsWith("reg.do")) {
            chain.doFilter(sreq, sresp);
            return;
        }
        if (req.getSession().getAttribute("user") == null && req.getMethod().equals("POST")) {
            resp.getWriter().println("http://localhost:8080/todo/auth/login.jsp");
            return;
        }
        chain.doFilter(sreq, sresp);
    }
}
