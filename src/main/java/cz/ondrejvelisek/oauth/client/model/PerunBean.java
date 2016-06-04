package cz.ondrejvelisek.oauth.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PerunBean {

    public abstract int getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerunBean group = (PerunBean) o;

        return getId() == group.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

}
