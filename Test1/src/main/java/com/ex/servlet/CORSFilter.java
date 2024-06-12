package com.ex.servlet;

/*
 * import java.io.IOException;
 * 
 * import javax.servlet.Filter; import javax.servlet.FilterChain; import
 * javax.servlet.FilterConfig; import javax.servlet.ServletException; import
 * javax.servlet.ServletRequest; import javax.servlet.ServletResponse; import
 * javax.servlet.annotation.WebFilter; import
 * javax.servlet.http.HttpServletResponse;
 * 
 * @WebFilter("/*") public class CORSFilter implements Filter {
 * 
 * @Override public void init(FilterConfig filterConfig) throws ServletException
 * { }
 * 
 * @Override public void doFilter(ServletRequest request, ServletResponse
 * response, FilterChain chain) throws IOException, ServletException {
 * HttpServletResponse httpResponse = (HttpServletResponse) response;
 * httpResponse.addHeader("Access-Control-Allow-Origin", "*");
 * httpResponse.addHeader("Access-Control-Allow-Methods",
 * "GET, POST, PUT, DELETE, OPTIONS");
 * httpResponse.addHeader("Access-Control-Allow-Headers",
 * "Content-Type, Authorization");
 * 
 * chain.doFilter(request, response); }
 * 
 * @Override public void destroy() { } }
 */

//chat gpt wala cors filter
/*
 * import javax.servlet.Filter; import javax.servlet.FilterChain; import
 * javax.servlet.FilterConfig; import javax.servlet.ServletException; import
 * javax.servlet.annotation.WebFilter; import
 * javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.io.IOException;
 * 
 * @WebFilter("/*") public class CORSFilter implements Filter { public void
 * doFilter(javax.servlet.ServletRequest req, javax.servlet.ServletResponse res,
 * FilterChain chain) throws IOException, ServletException { HttpServletRequest
 * request = (HttpServletRequest) req; HttpServletResponse response =
 * (HttpServletResponse) res; response.setHeader("Access-Control-Allow-Origin",
 * "*"); response.setHeader("Access-Control-Allow-Methods",
 * "GET, POST, DELETE, PUT, OPTIONS");
 * response.setHeader("Access-Control-Allow-Headers",
 * "Content-Type, Authorization"); chain.doFilter(req, res); }
 * 
 * public void init(FilterConfig filterConfig) {}
 * 
 * public void destroy() {} }
 */


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
