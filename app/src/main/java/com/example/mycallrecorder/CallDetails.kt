package com.example.mycallrecorder

public class CallDetails{
    var serial:Int?=null
    var num:String? =null
    var name: String?=null
    var time: String?=null
    var date: String?=null
    constructor() {
    }
    constructor(serial:Int, num:String,name: String, time:String, date:String) {
        this.serial = serial
        this.num = num
        this.name=name
        this.time = time
        this.date = date
    }
}


/* public String getName()
{
    return name;
}
public void setName(String name)
{
    this.name=name;
}*/
//   var name:String?=null //  private String name;
//    var time: String? = null
//    var date: String? = null



