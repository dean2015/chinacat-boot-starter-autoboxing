package com.chinacat.boot.starter.autoboxing.response;

import com.chinacat.boot.model.response.RestResponse;
import com.chinacat.boot.starter.autoboxing.config.property.AutoboxingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chinacat
 */
@RestControllerAdvice(annotations = {RestController.class})
public class ResponseAdvisor implements ResponseBodyAdvice<Object> {

    @Autowired
    private AutoboxingProperties autoboxingProperties;

    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

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
    public Object beforeBodyWrite(final Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        String requestPath = request.getURI().getPath();
        if (isUrlIncluded(autoboxingProperties.getDefaultPatterns(), requestPath) ||
                isUrlIncluded(autoboxingProperties.getPatterns(), requestPath)) {
            return body;
        }
        if (body instanceof RestResponse) {
            return body;
        }
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (body instanceof String) {
            //同时只能返回字符串,否则会抛出异常ExecutionResult cannot be cast to java.lang.String
            return RestResponse.newInstance().success(body).toString();
        }
        return RestResponse.newInstance().success(body);
    }

}