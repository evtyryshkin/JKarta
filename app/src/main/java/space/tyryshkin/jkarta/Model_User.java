package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;

public class Model_User {

    private String ID, image, email, login, city, sex, birthday, pin_code;

    public Model_User() {
    }

    public Model_User(String ID, String image, String email, String login, String city, String sex, String birthday, String pin_code) {
        this.ID = ID;
        this.image = image;
        this.email = email;
        this.login = login;
        this.city = city;
        this.sex = sex;
        this.birthday = birthday;
        this.pin_code = pin_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    @NonNull
    @Override
    public String toString() {
        return ID;
    }
}
