package com.givee.demo.server.config;

import com.givee.demo.server.HasLogger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Класс для логирования (протоколирования) информации
 */
@Component
public class LoggingFilter implements Filter, HasLogger {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Добавление фильтра логирования в запросе
     *
     * @param servletRequest запрос от клиента
     * @param servletResponse ответ клиенту на запрос
     * @param chain организация цепочки фильтров
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest
                && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            // кэшируем запрос и ответ для разбора
            HttpServletRequest requestToCache = new ContentCachingRequestWrapper(request);
            HttpServletResponse responseToCache = new ContentCachingResponseWrapper(response);
            // продолжение цепочки фильтров
            chain.doFilter(requestToCache, responseToCache);
            // получение информации из запроса
            String requestData = getRequestData(requestToCache);
            getLogger().debug("Request method: " + requestToCache.getMethod() +
                    "; Request URI: " + requestToCache.getRequestURI() +
                    "; Request parameters: " + requestToCache.getQueryString());
            getLogger().debug("REQUEST -> " + requestData);
            // получение информации из ответа
            String responseData = getResponseData(responseToCache);
            getLogger().debug("Response code : " + responseToCache.getStatus() +
                    "; Response encoding : " + responseToCache.getCharacterEncoding());
            getLogger().debug("RESPONSE -> " + responseData);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Получение информации из запроса
     *
     * @param request запрос
     * @return тело запроса
     * @throws UnsupportedEncodingException
     */
    private static String getRequestData(final HttpServletRequest request) throws UnsupportedEncodingException {
        String payload = null;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        byte[] buf = wrapper.getContentAsByteArray();
        if (buf.length > 0) {
			payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
		}
        return payload;
    }

    /**
     * Получение информации из ответа
     *
     * @param response ответ
     * @return тело ответа
     * @throws IOException
     */
    private static String getResponseData(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        byte[] buf = wrapper.getContentAsByteArray();
        if (buf.length > 0) {
			payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
			wrapper.copyBodyToResponse();
		}
        return payload;
    }

    @Override
    public void destroy() {

    }
}
