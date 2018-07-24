package com.lib.aliocr.bean;

import java.io.Serializable;

/**
 * 作者：xin on 2018/7/9 0009 13:50
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class RepOutput implements Serializable {


    private static final long serialVersionUID = 2694028643765036256L;

    private String address;// 地址信息
    private String config_str;// 配置信息，同输入configure
    private faceRect face_rect;// 信息
    private String name;// 姓名
    private String nationality;//  民族
    private String num;//  身份证号
    private String sex;//  性别
    private String birth;//  出生日期
    private String start_date;// 有效期起始时间
    private String end_date;// 有效期结束时间
    private String issue;// 签发机关
    private boolean success;// true表示成功，false表示失败

    static class faceRect implements Serializable {
        private static final long serialVersionUID = -7765350923476725941L;
        private double angle;
        private Center center;
        private Size size;

        public double getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public Center getCenter() {
            return center;
        }

        public void setCenter(Center center) {
            this.center = center;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        static class Center implements Serializable {
            private static final long serialVersionUID = 3232617470096971014L;
            private double x;
            private double y;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            @Override
            public String toString() {
                return "Center{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }

        static class Size implements Serializable {
            private static final long serialVersionUID = -3276942859242710227L;
            private double height;
            private double width;

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }


            @Override
            public String toString() {
                return "Size{" +
                        "height=" + height +
                        ", width=" + width +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "faceRect{" +
                    "angle=" + angle +
                    ", center=" + center +
                    ", size=" + size +
                    '}';
        }
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public faceRect getFace_rect() {
        return face_rect;
    }

    public void setFace_rect(faceRect face_rect) {
        this.face_rect = face_rect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    @Override
    public String toString() {
        return "RepOutput{" +
                "address='" + address + '\'' +
                ", config_str='" + config_str + '\'' +
                ", face_rect=" + face_rect +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", num='" + num + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", issue='" + issue + '\'' +
                ", success=" + success +
                '}';
    }
}
