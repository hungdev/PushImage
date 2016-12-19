package net.downloadblog.manic.pushimage.object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ManIc on 25-Jun-16.
 */
public class ImageEntity implements Serializable {
    public  ImageEntity(){
        //default contructor cant delete for firebase
    }
    public ImageEntity( Integer  starCount, Integer myStarStatus,Long time ,Map mapUserStar,String key,String iduser, String content,String user,String photo, Integer flag, String location) {
        this.iduser= iduser;
        this.time = time;
        this.content = content;
        this.user = user;
        this.photo = photo;
        this.starCount=starCount;
        this.key= key;
        this.myStarStatus=myStarStatus;
        this.mapUserStar=mapUserStar;
        this.flag=flag;
        this.location= location;
    }


//
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("iduser", iduser);
//        result.put("user", user);
//        result.put("content", content);
//        result.put("photo", photo);
//        result.put("time", time);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//      //  result.put("key", key);
//
//        return result;
//    }


    private Long time;
    private String content;
    private String photo;
    private String user;
    private String iduser;
    private Integer myStarStatus;
    public Integer starCount;
    private String key;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int flag;
    public Map<String, Boolean> getMapUserStar() {
        return mapUserStar;
    }

    public void setMapUserStar(Map<String, Boolean> mapUserStar) {
        this.mapUserStar = mapUserStar;
    }

    public Map<String, Boolean> mapUserStar = new HashMap<>();


    public Integer getMyStarStatus() {
        return myStarStatus;
    }

    public void setMyStarStatus(Integer myStarStatus) {
        this.myStarStatus = myStarStatus;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public Integer  getStarCount() {
        return starCount;
    }

    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }


    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

//    public String getKeyUserStar(){
//        mapUserStar.get(key);
//        return mapUserStar.get(key);
//    }
}
