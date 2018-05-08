package com.vergilyn.examples.enums;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/22
 */
public enum SongsEnum {
     SAFE_AND_SOUND(1,"Taylor Swift","Safe&Sound","2011-12-26")
    ,SHAKE_IT_OFF(2,"Taylor Swift","Shake It Off","2014-08-19")
    ,STYLE(3,"Taylor Swift","Style","2015-02-09")
    ,SOUND_OF_SILENCE(4,"Simon & Garfunkel","The Sound Of Silence","1966-01-17")
    ,BETTER_MAN(5,"Little Big Town","Better Man","2016-10-20")
    ,YESTERDAY_ONCE_MORE(6,"Carpenters","Yesterday Once More","1973-05-16")
    ;

    public final int index;
    public final String singer;
    public final String name;
    public final String date;

    private SongsEnum(int seq, String singer, String name, String date) {
        this.index = seq;
        this.singer = singer;
        this.name = name;
        this.date = date;
    }

    public int getIndex() {
        return index;
    }

    public String getSinger() {
        return singer;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }


}
