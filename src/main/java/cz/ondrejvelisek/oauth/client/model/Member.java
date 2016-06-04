package cz.ondrejvelisek.oauth.client.model;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Member extends PerunBean {

    private int userId;
    private int voId;
    private String membershipType;
    private String status;
    private int id;
    private String beanName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoId() {
        return voId;
    }

    public void setVoId(int voId) {
        this.voId = voId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String toString() {
        return "Member{" +
                "userId=" + userId +
                ", voId=" + voId +
                ", membershipType='" + membershipType + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", beanName='" + beanName + '\'' +
                '}';
    }
}
