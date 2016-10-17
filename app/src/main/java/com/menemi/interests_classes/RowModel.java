package com.menemi.interests_classes;

/**
 * Created by tester03 on 02.08.2016.
 */
public class RowModel
{
    private String name;
    private int itemId;


   public RowModel(String name, int itemId){
       this.name = name;
       this.itemId = itemId;
    }
    public String getName(){
        return this.name;
    }
    public int getItemId()
    {
        return itemId;
    }
}
