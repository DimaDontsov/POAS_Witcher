/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author dryush
 */
public class Advert implements ICommented {
    
    private long id;
    public long getId(){
        return id;
    }
    
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    private String info;
    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }
    
    public final static int MAX_IMAGES = 10;
    private LinkedList <Bitmap> images;
    public ArrayList<Bitmap> getImages(){

        ArrayList<Bitmap> imgs = new ArrayList<>();
        for (Bitmap img : imgs) {
            imgs.add( (Bitmap) img.copy(img.getConfig(), false) );
        }
        return imgs;
    }
    public ArrayList<Bitmap> addImage(Bitmap img, int index){
        if (images.size() >= MAX_IMAGES){
            throw new RuntimeException("Не пихайте больше " + MAX_IMAGES + " картинок");
        }

        images.add(index, img.copy(img.getConfig(), false));

        return getImages();
    }

    public ArrayList<Bitmap> addImage(Bitmap img){

        return addImage(img, images.size());
        //this.images.addLast(img);
    }
    
    public void removeImage(int index){
    
    }
    
    private Location location;
    public Location getLovation(){
        return location;
    }
    public void setLocation(Location loc){
        this.location = loc;
    }
    
    int reward;
    public int getReward(){
        return reward;
    }
    public void setReward(int reward){
        this.reward = reward;
    }
    
    //Profile author;
    private long id_author;
    public long getAuthorId(){
        return id_author;
    }
    
    private LinkedList<Long> idSubscribedWitchersList;
    public ArrayList<Long> getIdSubscribedWitchersList(){
        return new ArrayList<>(idSubscribedWitchersList);
    }
    public ArrayList<Long> addIdSubscribedWitcher( long id_witcher){
        idSubscribedWitchersList.add(id_witcher);
        return getIdSubscribedWitchersList();
    }
    
    //Profile executor;
    private long id_executor;

    public void setExecutorId( long id_witcher){
        id_executor = id_witcher;
    }
    public long getExecutorId(){
        return id_executor;
    }

    public static enum AdvertStatus{
        FREE,
        ASSIGNED_WITCHER,
        IN_PROCESS,
        COMPLETED,
        WITCHER_LEAVE,
        CUSTOMER_REFUSED;
    };
    
    private AdvertStatus status;
    public AdvertStatus getStatus(){
        return status;
    }
    
    private Date dateOfCreate;
    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public Advert(String name, String info, Location location, int reward, long id_author,
                  AdvertStatus status, Date dateOfCreate) {
        this.name = name;
        this.info = info;
        this.location = location;
        this.reward = reward;
        this.id_author = id_author;
        this.status = AdvertStatus.FREE;
        this.dateOfCreate = dateOfCreate;
    }
    
    private CommentsContainer comments;
    @Override
    public ArrayList<Comment> getComments() {
        return comments.getComments();
    }
    @Override
    public ArrayList<Comment> addComment(Comment com) {
        return comments.addComment(com);
    }

    public Advert(long id, String name, String info, LinkedList<Bitmap> images, Location location, int reward,
                  long id_author, LinkedList<Long> idSubscribedWitchersList, long id_executor, AdvertStatus status,
                  Date dateOfCreate, CommentsContainer comments) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.images = images;
        this.location = location;
        this.reward = reward;
        this.id_author = id_author;
        this.idSubscribedWitchersList = idSubscribedWitchersList;
        this.id_executor = id_executor;
        this.status = status;
        this.dateOfCreate = dateOfCreate;
        this.comments = comments;
    }
    
    
    @Override
    public Object clone(){
        
    return new Advert(id, name, info, images, location, reward, id_author, idSubscribedWitchersList,
            id_executor, status, dateOfCreate, comments);
        
    }
}
