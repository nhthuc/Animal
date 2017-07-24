package vn.cusc.animalsound;

/**
 * Created by Nguyen Hoang Thuc on 6/16/2017.
 */

public class Animal {
    int id;
    String name;
    String img;
    String voice;

    public Animal(int id, String name, String img, String voice) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.voice = voice;
    }

    public Animal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
