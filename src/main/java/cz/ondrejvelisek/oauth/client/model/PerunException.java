package cz.ondrejvelisek.oauth.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.MalformedURLException;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerunException extends Exception {

    private String errorId;
    private String name;
    private String message;

    // Has to have default constructor because of Jackson JSON library
    public PerunException() {
        super();
    }

    public PerunException(String name, String message) {
        super(message);
        this.name = name;
        this.message = message;
    }

    public PerunException(String s, Throwable throwable) {
        super(s, throwable);
        this.name = throwable.getClass().getSimpleName();
        this.message = s;
    }

    public PerunException(Throwable throwable) {
        super(throwable);
        this.name = throwable.getClass().getSimpleName();
        this.message = throwable.getMessage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
}
