package com.ming.live_player.bean;

import java.util.List;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class NetAudioBean {


    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * count : 4248
         * np : 1547070962
         */

        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {

        private String bookmark;
        private String cate;
        private String comment;
        private int down;
        private int forward;
        private String id;
        private int is_best;
        private String passtime;
        private String share_url;
        private int status;
        private String text;
        private String type;
        private UBean u;
        private String up;
        private VideoBean video;
        private ImageBean image;
        private GifBean gif;
        private List<TagsBean> tags;
        private List<TopCommentsBean> top_comments;

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIs_best() {
            return is_best;
        }

        public void setIs_best(int is_best) {
            this.is_best = is_best;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public GifBean getGif() {
            return gif;
        }

        public void setGif(GifBean gif) {
            this.gif = gif;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<TopCommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<TopCommentsBean> top_comments) {
            this.top_comments = top_comments;
        }

        public static class UBean {


            private boolean is_v;
            private boolean is_vip;
            private String name;
            private int relationship;
            private String room_icon;
            private String room_name;
            private String room_role;
            private String room_url;
            private String uid;
            private List<String> header;

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getRelationship() {
                return relationship;
            }

            public void setRelationship(int relationship) {
                this.relationship = relationship;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }

        public static class VideoBean {
            /**
             * download : ["http://wvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpdm.mp4","http://uvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpdm.mp4","http://tvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpdm.mp4"]
             * duration : 17
             * height : 1066
             * playcount : 2485
             * playfcount : 319
             * thumbnail : ["http://wimg.spriteapp.cn/picture/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.jpg","http://dimg.spriteapp.cn/picture/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.jpg"]
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.jpg"]
             * video : ["http://wvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.mp4","http://uvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.mp4","http://tvideo.spriteapp.cn/video/2019/0109/90fdc23213ec11e98065842b2b4c75ab_wpd.mp4"]
             * width : 600
             */

            private int duration;
            private int height;
            private int playcount;
            private int playfcount;
            private int width;
            private List<String> download;
            private List<String> thumbnail;
            private List<String> thumbnail_small;
            private List<String> video;

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getPlaycount() {
                return playcount;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public int getPlayfcount() {
                return playfcount;
            }

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<String> getDownload() {
                return download;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }

            public List<String> getVideo() {
                return video;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }
        }

        public static class ImageBean {
            /**
             * big : ["http://wimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab_1.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab_d.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab_d.jpg","http://wimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab.jpg"]
             * height : 834
             * medium : []
             * small : []
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2019/01/09/0c4f459813fa11e9bc8f842b2b4c75ab.jpg"]
             * width : 690
             */

            private int height;
            private int width;
            private List<String> big;
            private List<String> download_url;
            private List<?> medium;
            private List<?> small;
            private List<String> thumbnail_small;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<String> getBig() {
                return big;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<?> getMedium() {
                return medium;
            }

            public void setMedium(List<?> medium) {
                this.medium = medium;
            }

            public List<?> getSmall() {
                return small;
            }

            public void setSmall(List<?> small) {
                this.small = small;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class GifBean {
            /**
             * download_url : ["http://wimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_d.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_d.jpg","http://wimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_a_1.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_a_1.jpg"]
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_a_1.jpg","http://dimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e_a_1.jpg"]
             * height : 254
             * images : ["http://wimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e.gif","http://dimg.spriteapp.cn/ugc/2019/01/09/5c34caa8aa28e.gif"]
             * width : 210
             */

            private int height;
            private int width;
            private List<String> download_url;
            private List<String> gif_thumbnail;
            private List<String> images;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }

        public static class TagsBean {
            /**
             * colum_set : 1
             * display_level : 0
             * forum_sort : 0
             * forum_status : 2
             * id : 58191
             * image_list : http://img.spriteapp.cn/ugc/2017/07/8225f4ee660711e7978d842b2b4c75ab.png
             * info : 爆笑视频让你笑的飞起

             乱选分类会被版主删帖哦~

             版主申请加微信：L1391446139
             * name : 搞笑视频
             * post_number : 494785
             * sub_number : 59142
             * tail : 个小逗比
             */

            private int colum_set;
            private int display_level;
            private int forum_sort;
            private int forum_status;
            private int id;
            private String image_list;
            private String info;
            private String name;
            private int post_number;
            private int sub_number;
            private String tail;

            public int getColum_set() {
                return colum_set;
            }

            public void setColum_set(int colum_set) {
                this.colum_set = colum_set;
            }

            public int getDisplay_level() {
                return display_level;
            }

            public void setDisplay_level(int display_level) {
                this.display_level = display_level;
            }

            public int getForum_sort() {
                return forum_sort;
            }

            public void setForum_sort(int forum_sort) {
                this.forum_sort = forum_sort;
            }

            public int getForum_status() {
                return forum_status;
            }

            public void setForum_status(int forum_status) {
                this.forum_status = forum_status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage_list() {
                return image_list;
            }

            public void setImage_list(String image_list) {
                this.image_list = image_list;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPost_number() {
                return post_number;
            }

            public void setPost_number(int post_number) {
                this.post_number = post_number;
            }

            public int getSub_number() {
                return sub_number;
            }

            public void setSub_number(int sub_number) {
                this.sub_number = sub_number;
            }

            public String getTail() {
                return tail;
            }

            public void setTail(String tail) {
                this.tail = tail;
            }
        }

        public static class TopCommentsBean {
            /**
             * cmt_type : text
             * content : 是文艺别打码，是色情请别发。
             * hate_count : 0
             * id : 11712706
             * like_count : 50
             * passtime : 2019-01-10 09:21:43
             * precid : 0
             * preuid : 0
             * status : 0
             * u : {"header":["http://wimg.spriteapp.cn/profile/large/2018/12/22/5c1e45f0b0f69_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/12/22/5c1e45f0b0f69_mini.jpg"],"is_vip":false,"name":"小老弟","room_icon":"","room_name":"","room_role":"","room_url":"","sex":"m","uid":"16082905"}
             * voicetime : 0
             * voiceuri :
             */

            private String cmt_type;
            private String content;
            private int hate_count;
            private int id;
            private int like_count;
            private String passtime;
            private int precid;
            private int preuid;
            private int status;
            private UBeanX u;
            private int voicetime;
            private String voiceuri;

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getHate_count() {
                return hate_count;
            }

            public void setHate_count(int hate_count) {
                this.hate_count = hate_count;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public UBeanX getU() {
                return u;
            }

            public void setU(UBeanX u) {
                this.u = u;
            }

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public static class UBeanX {
                /**
                 * header : ["http://wimg.spriteapp.cn/profile/large/2018/12/22/5c1e45f0b0f69_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/12/22/5c1e45f0b0f69_mini.jpg"]
                 * is_vip : false
                 * name : 小老弟
                 * room_icon :
                 * room_name :
                 * room_role :
                 * room_url :
                 * sex : m
                 * uid : 16082905
                 */

                private boolean is_vip;
                private String name;
                private String room_icon;
                private String room_name;
                private String room_role;
                private String room_url;
                private String sex;
                private String uid;
                private List<String> header;

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }
    }
}
