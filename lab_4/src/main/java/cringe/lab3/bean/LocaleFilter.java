package cringe.lab3.bean;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class LocaleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        // Получаем язык из куки или используем en по умолчанию
        String lang = "en";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lang".equals(cookie.getName())) {
                    lang = cookie.getValue();
                    break;
                }
            }
        }

        // Устанавливаем атрибут запроса
        request.setAttribute("userLocale", lang);

        // Продолжаем цепочку фильтров
        chain.doFilter(request, res);
    }
}