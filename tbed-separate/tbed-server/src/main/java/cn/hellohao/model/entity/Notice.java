package cn.hellohao.model.entity;

public class Notice {
    private Integer id;
    private String text;


    public Notice() {
        super();
    }


    public Notice(Integer id, String text, String password, String email, String birthder, Integer level,
                  String keyID) {
        super();
        this.id = id;
        this.text = text;

    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String gettext() {
        return text;
    }


    public void settext(String text) {
        this.text = text;
    }


}
