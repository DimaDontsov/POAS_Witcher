/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwitcherclientpart;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Acer
 */
public class Profile implements ICommented{
    
    private long id;
    
    Profile(){
        
    }
    
    public long getId(){
        return id;
    }
    
    private String name;
    
    public void setName(String _name){
        name = _name;
    }
    
    public String getName(){
        return name;
    }

    CommentsContainer comments;
    @Override
    public ArrayList<Comment> getComments() {
        return comments.getComments();
    }
    @Override
    public ArrayList<Comment> addComment(Comment com) {
        return comments.addComment(com);
    }
    
    public static enum ProfileType{ WITCHER, CUSTOMER }
    
    private ProfileType type;
    
    public ProfileType getType(){
        return type;
    }
    
    private String info;
    
    public void setInfo(String _info){
        info = _info;
    }
    
    public String getInfo(){
        return info;
    }
    
    private LinkedList<AdvertCard> history;
    
    public void addAdvertinHistory(AdvertCard advert){
        history.add(advert);
    }
    
//    public AdvertCard getAdvertFromHistory(int numberAdvert){
//        return history.get(numberAdvert).clone();
//    }
    
    public ArrayList<AdvertCard> getHistory(){
        return new ArrayList<AdvertCard>(history);
    }
    
    private Image image;
    
    public void setImage(Image _image){
        image = _image;
    }
    
    public Image getImage(){
        return image;
    }
    
    public void removeImage(){
        image = null;
    }
    
    
}
