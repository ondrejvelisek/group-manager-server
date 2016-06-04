package cz.ondrejvelisek.oauth.client.model;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Group extends PerunBean {

    private int id;
    private String shortName;
    private String name;
    private String description;
    private int voId;
    private int parentGroupId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVoId() {
        return voId;
    }

    public void setVoId(int voId) {
        this.voId = voId;
    }

    public int getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(int parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", voId=" + voId +
                ", parentGroupId=" + parentGroupId +
                '}';
    }
}
