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

        // Получаем язык из куки
        String lang = "en";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lang".equals(cookie.getName())) {
                    lang = cookie.getValue();
                    System.out.println(lang);
                    break;
                }
            }
        }

        System.out.println("eeeeeeh((");

        // Устанавливаем атрибут для JSP
        request.setAttribute("userLocale", lang);
        chain.doFilter(request, res);
    }
}