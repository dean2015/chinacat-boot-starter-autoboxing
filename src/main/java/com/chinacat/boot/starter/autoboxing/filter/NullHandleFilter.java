package com.chinacat.boot.starter.autoboxing.filter;


import com.chinacat.boot.model.response.RestResponse;
import com.chinacat.boot.starter.autoboxing.config.property.AutoboxingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author chinacat
 */
public class NullHandleFilter extends OncePerRequestFilter {

    @Autowired
    private AutoboxingProperties autoboxingProperties;

    protected PathMatcher pathMatcher = new AntPathMatcher();

    private boolean isUrlIncluded(List<String> patterns, String path) {
        for (String pattern : patterns) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        return super.shouldNotFilter(request) || isUrlIncluded(autoboxingProperties.getDefaultPatterns(),
                requestPath) || isUrlIncluded(autoboxingProperties.getPatterns(), requestPath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ResponseWrapper responseNullWrapper = new ResponseWrapper(response);
        filterChain.doFilter(request, responseNullWrapper);
        byte[] bytes = responseNullWrapper.getBytes();
        if (bytes.length <= 0) {
            bytes = RestResponse.<Void>newInstance().success("").toString().getBytes(Charset.defaultCharset());
            response.setCharacterEncoding("utf-8");
            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        response.getOutputStream().write(bytes);
    }


}
