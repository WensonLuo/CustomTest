package com.newpos.upos.customtext.exercise.parcel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wenson_Luo on 2017/7/22.
 * Parcel 练习
 */

public class BasicInfo implements Parcelable {

    private String sn;

    private String model;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        String str = "sn = " + sn + "; model = " + model;
        return str;
    }

    public static final Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel in) {
            BasicInfo info = new BasicInfo();
            info.setModel(in.readString());
            info.setSn(in.readString());
            return info;
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeString(model);
    }
}
