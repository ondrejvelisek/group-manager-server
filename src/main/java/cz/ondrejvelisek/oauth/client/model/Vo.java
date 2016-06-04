package cz.ondrejvelisek.oauth.client.model;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Vo extends PerunBean {

    private int id;
    private String shortName;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vo{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
