package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;

public class Model_User {

    private String ID, email, login, city, sex, birthday;
    private int avatar;

    public Model_User() {
    }

    public Model_User(String ID, String email, String login, String city, String sex, String birthday, int avatar) {
        this.ID = ID;
        this.email = email;
        this.login = login;
        this.city = city;
        this.sex = sex;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    @NonNull
    @Override
    public String toString() {
        return ID;
    }
}
