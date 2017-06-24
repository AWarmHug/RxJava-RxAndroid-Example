package me.warm.rxjava_rxandroid_example.entity;

/**
 * Created by xc on 2017/3/8.
 * 上传文件
 */

public class UpData {

    /**
     * status : 200
     * message : OK
     * data : {"name":"xh.png","type":"image/png","tmp_name":"/alidata/www/erp.51hs.cn/tmp/170309031535-d15eff4726e7cb3e058e8c6f8f1850fe.png","size":11987}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : xh.png
         * type : image/png
         * tmp_name : /alidata/www/erp.51hs.cn/tmp/170309031535-d15eff4726e7cb3e058e8c6f8f1850fe.png
         * size : 11987
         */

        private String name;
        private String type;
        private String tmp_name;
        private int size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTmp_name() {
            return tmp_name;
        }

        public void setTmp_name(String tmp_name) {
            this.tmp_name = tmp_name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", tmp_name='" + tmp_name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UpData{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
