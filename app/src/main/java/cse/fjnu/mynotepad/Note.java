package cse.fjnu.mynotepad;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    public int id;
    public String context;
    public String title;
    public String createTime;
    public Note(int id,String context,String title){
        this.id=id;
        this.context=context;
        this.title=title;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        //这个就是把时间戳经过处理得到期望格式的时间
        String createTime = format.format(date.getTime());
        this.createTime=createTime;
    }
    public Note(int id,String context,String title,String createTime){
        this.id=id;
        this.context=context;
        this.title=title;
        this.createTime=createTime;
    }

    public int getId() {
        return id;
    }

    public String getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String toString(){
        return "id为:"+id+" context为："+context+" title为："+title+" createTime为:"+createTime;
    }

    /**
     * 判断该本笔记的内容是否包含partContext;
     * @param partTitle
     * @return
     */
    public Boolean isExited(String partTitle){
        if(this.title.contains(partTitle)){
            return true;
        }else{
            return false;
        }
    }

}
