package cringe.lab3.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@Named
@SessionScoped
public class LocaleChanger implements Serializable {
    private Locale locale;

    @PostConstruct
    public void init() {
        String lang = getCookieValue("lang");

        this.locale = lang != null ? new Locale(lang) : Locale.getDefault();

        // this.locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public void setEnglish() {
        setLocale("en");
    }

    public void setRussian() {
        setLocale("ru");
    }

    private void setLocale(String lang) {
        this.locale = new Locale(lang);
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getViewRoot() != null) {
            context.getViewRoot().setLocale(locale);
        }

        ExternalContext ec = context.getExternalContext();
        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(60 * 60);
        cookie.setPath(ec.getRequestContextPath());
        ec.addResponseCookie(cookie.getName(), cookie.getValue(), null);
    }

    private String getCookieValue(String name) {
        Map<String, Object> cookies = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap();
        Cookie cookie = (Cookie) cookies.get(name);
        return cookie != null ? cookie.getValue() : null;
    }

    public Locale getLocale() {
        return locale;
    }
}