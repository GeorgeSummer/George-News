package com.george.summer.gerogenews;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/31.
 */
public class NewsType  {
    private ArrayList<NewsInfo> tList;

    public NewsType(ArrayList<NewsInfo> tList) {
        this.tList = tList;
    }

    public ArrayList<NewsInfo> gettList() {
        return tList;
    }

    public void settList(ArrayList<NewsInfo> tList) {
        this.tList = tList;
    }

    public static class NewsInfo implements Serializable {
        private String tid;
        private String tname;

        public NewsInfo(String tid, String tname) {
            this.tid = tid;
            this.tname = tname;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }
    }
}
