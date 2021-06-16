package net.suncaper.tag_backend.hbase.pojo;

public class UserProfile {
    private Long row;
    private String gender;

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "row=" + row +
                ", gender='" + gender + '\'' +
                '}';
    }
}
