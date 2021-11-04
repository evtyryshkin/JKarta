package space.tyryshkin.jkarta;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Model_User implements Parcelable {

    private String ID, image, email, login, city, sex, birthday;

    public static final String PREFERENCES_PIN = "PREFERENCES_PIN";
    public static final String PREFERENCES_IS_HAS_FINGERPRINT = "PREFERENCES_IS_HAS_FINGERPRINT";

    public Model_User() {
    }

    public Model_User(String ID, String image, String email, String login, String city, String sex, String birthday) {
        this.ID = ID;
        this.image = image;
        this.email = email;
        this.login = login;
        this.city = city;
        this.sex = sex;
        this.birthday = birthday;
    }

    protected Model_User(Parcel in) {
        ID = in.readString();
        image = in.readString();
        email = in.readString();
        login = in.readString();
        city = in.readString();
        sex = in.readString();
        birthday = in.readString();
    }

    public static final Creator<Model_User> CREATOR = new Creator<Model_User>() {
        @Override
        public Model_User createFromParcel(Parcel in) {
            return new Model_User(in);
        }

        @Override
        public Model_User[] newArray(int size) {
            return new Model_User[size];
        }
    };

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

    @NonNull
    @Override
    public String toString() {
        return ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(image);
        parcel.writeString(email);
        parcel.writeString(login);
        parcel.writeString(city);
        parcel.writeString(sex);
        parcel.writeString(birthday);
    }
}
