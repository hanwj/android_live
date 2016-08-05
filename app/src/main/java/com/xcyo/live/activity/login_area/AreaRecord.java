package com.xcyo.live.activity.login_area;

import com.Sortlistview.CharacterParser;
import com.Sortlistview.SortHand;
import com.xcyo.baselib.record.BaseRecord;

/**
 * Created by TDJ on 2016/6/27.
 */
public class AreaRecord extends BaseRecord implements SortHand {

    private String ch;
    private String number;
    private String pinyi;

    public void setCh(String ch) {
        this.ch = ch;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }

    public String getCh() {
        return ch;
    }

    public String getNumber() {
        return number;
    }

    public String getPinyi() {
        return pinyi;
    }

    public String getSortContent(){
        return pinyi;
    }

    @Override
    public String getSort() {
        CharacterParser chp = getCharacter();
        if(getSortContent() == null || getSortContent().length() == 0){
            return "#";
        }
        String phead = chp.getSelling(getSortContent()).substring(0, 1).toUpperCase();
        if(phead.matches("[A-Z]")){
            return phead;
        }
        return "#";
    }

    @Override
    public CharacterParser getCharacter() {
        return CharacterParser.getInstance();
    }
}
