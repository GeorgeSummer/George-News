package com.george.summer.gerogenews;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class NetNews implements Serializable {
        private String boardid;
        private String title;
        private List<Pics> imgextra;
        private String imgsrc;
        private String source;
        private long replyCount;
        private List<Ads> ads;
        private int hasAD;
        private String docid;
        private String url;

    public NetNews(List<Ads> ads, String boardid, List<Pics> imgextra,
                   String imgsrc, long replyCount, String source,
                   String title,int hasAD,String docid,String url) {
        this.ads = ads;
        this.boardid = boardid;
        this.imgextra = imgextra;
        this.imgsrc = imgsrc;
        this.replyCount = replyCount;
        this.source = source;
        this.title = title;
        this.hasAD = hasAD;
        this.docid = docid;
        this.url = url;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Ads> getAds() {
        return ads;
    }

    public void setAds(List<Ads> ads) {
        this.ads = ads;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public List<Pics> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<Pics> imgextra) {
        this.imgextra = imgextra;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHasAD() {
        return hasAD;
    }

    public void setHasAD(int hasAD) {
        this.hasAD = hasAD;
    }

    public static class Pics implements Serializable{
            private String imgsrc;

            public Pics(String imgsrc) {
                this.imgsrc = imgsrc;
            }

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }
        }

    public static class Ads implements Serializable {
        private String imgsrc;
        private String title;

        public Ads(String imgsrc, String title) {
            this.imgsrc = imgsrc;
            this.title = title;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

