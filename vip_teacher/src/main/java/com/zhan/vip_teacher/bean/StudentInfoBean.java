package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class StudentInfoBean extends BaseResponseBean implements Serializable {
    /**
     * Guid : sample string 1
     * CourseName : sample string 2
     * CName : sample string 3
     * EName : sample string 4
     * Gender : 1
     * GenderName : sample string 5
     * Nickname : sample string 6
     * HeadImgUrl : sample string 7
     * Age : 1
     * Degree : 1
     * DegreeName : sample string 8
     * CourseType : sample string 9
     * CourseTypeName : sample string 10
     * ExamDate : 2016-03-24 10:49:55
     * GuaranteeScore : sample string 11
     * LessonCount : 1
     * ProductId : 12
     * QQ : sample string 13
     * VipGrade : 14
     * VipGradeName : sample string 15
     * StudentCourseDetails : [{"CourseSubType":"sample string 1","CourseSubTypeName":"sample string 2","LessonCount":1,"StudentTeacherList":[{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"}]},{"CourseSubType":"sample string 1","CourseSubTypeName":"sample string 2","LessonCount":1,"StudentTeacherList":[{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"}]},{"CourseSubType":"sample string 1","CourseSubTypeName":"sample string 2","LessonCount":1,"StudentTeacherList":[{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"}]}]
     * HistoryCount : 16
     * HistoryExamDtoList : [{"Id":1,"StudentCourseId":2,"StudentGuid":"sample string 3","Category":"sample string 4","CourseType":"sample string 5","CourseTypeName":"sample string 6","ExamDate":"2016-03-24 10:49:55","ExamDateHis":"2016-03-24 10:49:55","TotalScore":"sample string 7","Remark":"sample string 8","DetailsCollection":[{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1}]},{"Id":1,"StudentCourseId":2,"StudentGuid":"sample string 3","Category":"sample string 4","CourseType":"sample string 5","CourseTypeName":"sample string 6","ExamDate":"2016-03-24 10:49:55","ExamDateHis":"2016-03-24 10:49:55","TotalScore":"sample string 7","Remark":"sample string 8","DetailsCollection":[{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1}]},{"Id":1,"StudentCourseId":2,"StudentGuid":"sample string 3","Category":"sample string 4","CourseType":"sample string 5","CourseTypeName":"sample string 6","ExamDate":"2016-03-24 10:49:55","ExamDateHis":"2016-03-24 10:49:55","TotalScore":"sample string 7","Remark":"sample string 8","DetailsCollection":[{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1}]}]
     * StudentAddressInfo : {"CityId":1,"CityName":"sample string 2","ProvinceId":3,"ProvinceName":"sample string 4","CountryCode":5,"CountryName":"sample string 6"}
     * CityCode : 17
     * CountryCode : 18
     * NextExamDate : 2016-03-24 10:49:55
     * NextTargetScore : 1.1
     * RereadingApplyDate : 2016-03-24 10:49:55
     * RereadingTimes : 1
     * ConsultRemark : sample string 19
     * School : sample string 20
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private String Guid;
        private String CourseName;
        private String CName;
        private String EName;
        private int Gender;
        private String GenderName;
        private String Nickname;
        private String HeadImgUrl;
        private int Age;
        private int Degree;
        private String DegreeName;
        private String CourseType;
        private String CourseTypeName;
        private String ExamDate;
        private String GuaranteeScore;
        private int LessonCount;
        private int ProductId;
        private String QQ;
        private int VipGrade;
        private String VipGradeName;
        private int HistoryCount;
        /**
         * CityId : 1
         * CityName : sample string 2
         * ProvinceId : 3
         * ProvinceName : sample string 4
         * CountryCode : 5
         * CountryName : sample string 6
         */

        private StudentAddressInfoEntity StudentAddressInfo;
        private int CityCode;
        private int CountryCode;
        private String NextExamDate;
        private double NextTargetScore;
        private String RereadingApplyDate;
        private int RereadingTimes;
        private String ConsultRemark;
        private String School;
        /**
         * CourseSubType : sample string 1
         * CourseSubTypeName : sample string 2
         * LessonCount : 1
         * StudentTeacherList : [{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"},{"TeacherId":1,"TeacherName":"sample string 2","CourseSubType":"sample string 3"}]
         */

        private List<StudentCourseDetailsEntity> StudentCourseDetails;
        /**
         * Id : 1
         * StudentCourseId : 2
         * StudentGuid : sample string 3
         * Category : sample string 4
         * CourseType : sample string 5
         * CourseTypeName : sample string 6
         * ExamDate : 2016-03-24 10:49:55
         * ExamDateHis : 2016-03-24 10:49:55
         * TotalScore : sample string 7
         * Remark : sample string 8
         * DetailsCollection : [{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1},{"HistoryExamId":1,"Id":2,"CourseSubType":"sample string 3","CourseSubTypeName":"sample string 4","Score":1.1}]
         */

        private List<HistoryExamDtoListEntity> HistoryExamDtoList;

        public void setGuid(String Guid) {
            this.Guid = Guid;
        }

        public void setCourseName(String CourseName) {
            this.CourseName = CourseName;
        }

        public void setCName(String CName) {
            this.CName = CName;
        }

        public void setEName(String EName) {
            this.EName = EName;
        }

        public void setGender(int Gender) {
            this.Gender = Gender;
        }

        public void setGenderName(String GenderName) {
            this.GenderName = GenderName;
        }

        public void setNickname(String Nickname) {
            this.Nickname = Nickname;
        }

        public void setHeadImgUrl(String HeadImgUrl) {
            this.HeadImgUrl = HeadImgUrl;
        }

        public void setAge(int Age) {
            this.Age = Age;
        }

        public void setDegree(int Degree) {
            this.Degree = Degree;
        }

        public void setDegreeName(String DegreeName) {
            this.DegreeName = DegreeName;
        }

        public void setCourseType(String CourseType) {
            this.CourseType = CourseType;
        }

        public void setCourseTypeName(String CourseTypeName) {
            this.CourseTypeName = CourseTypeName;
        }

        public void setExamDate(String ExamDate) {
            this.ExamDate = ExamDate;
        }

        public void setGuaranteeScore(String GuaranteeScore) {
            this.GuaranteeScore = GuaranteeScore;
        }

        public void setLessonCount(int LessonCount) {
            this.LessonCount = LessonCount;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public void setQQ(String QQ) {
            this.QQ = QQ;
        }

        public void setVipGrade(int VipGrade) {
            this.VipGrade = VipGrade;
        }

        public void setVipGradeName(String VipGradeName) {
            this.VipGradeName = VipGradeName;
        }

        public void setHistoryCount(int HistoryCount) {
            this.HistoryCount = HistoryCount;
        }

        public void setStudentAddressInfo(StudentAddressInfoEntity StudentAddressInfo) {
            this.StudentAddressInfo = StudentAddressInfo;
        }

        public void setCityCode(int CityCode) {
            this.CityCode = CityCode;
        }

        public void setCountryCode(int CountryCode) {
            this.CountryCode = CountryCode;
        }

        public void setNextExamDate(String NextExamDate) {
            this.NextExamDate = NextExamDate;
        }

        public void setNextTargetScore(double NextTargetScore) {
            this.NextTargetScore = NextTargetScore;
        }

        public void setRereadingApplyDate(String RereadingApplyDate) {
            this.RereadingApplyDate = RereadingApplyDate;
        }

        public void setRereadingTimes(int RereadingTimes) {
            this.RereadingTimes = RereadingTimes;
        }

        public void setConsultRemark(String ConsultRemark) {
            this.ConsultRemark = ConsultRemark;
        }

        public void setSchool(String School) {
            this.School = School;
        }

        public void setStudentCourseDetails(List<StudentCourseDetailsEntity> StudentCourseDetails) {
            this.StudentCourseDetails = StudentCourseDetails;
        }

        public void setHistoryExamDtoList(List<HistoryExamDtoListEntity> HistoryExamDtoList) {
            this.HistoryExamDtoList = HistoryExamDtoList;
        }

        public String getGuid() {
            return Guid;
        }

        public String getCourseName() {
            return CourseName;
        }

        public String getCName() {
            return CName;
        }

        public String getEName() {
            return EName;
        }

        public int getGender() {
            return Gender;
        }

        public String getGenderName() {
            return GenderName;
        }

        public String getNickname() {
            return Nickname;
        }

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public int getAge() {
            return Age;
        }

        public int getDegree() {
            return Degree;
        }

        public String getDegreeName() {
            return DegreeName;
        }

        public String getCourseType() {
            return CourseType;
        }

        public String getCourseTypeName() {
            return CourseTypeName;
        }

        public String getExamDate() {
            return ExamDate;
        }

        public String getGuaranteeScore() {
            return GuaranteeScore;
        }

        public int getLessonCount() {
            return LessonCount;
        }

        public int getProductId() {
            return ProductId;
        }

        public String getQQ() {
            if(QQ == null){
                return "";
            }
            return QQ;
        }

        public int getVipGrade() {
            return VipGrade;
        }

        public String getVipGradeName() {
            return VipGradeName;
        }

        public int getHistoryCount() {
            return HistoryCount;
        }

        public StudentAddressInfoEntity getStudentAddressInfo() {
            return StudentAddressInfo;
        }

        public int getCityCode() {
            return CityCode;
        }

        public int getCountryCode() {
            return CountryCode;
        }

        public String getNextExamDate() {
            return NextExamDate;
        }

        public double getNextTargetScore() {
            return NextTargetScore;
        }

        public String getRereadingApplyDate() {
            return RereadingApplyDate;
        }

        public int getRereadingTimes() {
            return RereadingTimes;
        }

        public String getConsultRemark() {
            return ConsultRemark;
        }

        public String getSchool() {
            return School;
        }

        public List<StudentCourseDetailsEntity> getStudentCourseDetails() {
            return StudentCourseDetails;
        }

        public List<HistoryExamDtoListEntity> getHistoryExamDtoList() {
            return HistoryExamDtoList;
        }

        public static class StudentAddressInfoEntity {
            private int CityId;
            private String CityName;
            private int ProvinceId;
            private String ProvinceName;
            private int CountryCode;
            private String CountryName;

            public void setCityId(int CityId) {
                this.CityId = CityId;
            }

            public void setCityName(String CityName) {
                this.CityName = CityName;
            }

            public void setProvinceId(int ProvinceId) {
                this.ProvinceId = ProvinceId;
            }

            public void setProvinceName(String ProvinceName) {
                this.ProvinceName = ProvinceName;
            }

            public void setCountryCode(int CountryCode) {
                this.CountryCode = CountryCode;
            }

            public void setCountryName(String CountryName) {
                this.CountryName = CountryName;
            }

            public int getCityId() {
                return CityId;
            }

            public String getCityName() {
                return CityName;
            }

            public int getProvinceId() {
                return ProvinceId;
            }

            public String getProvinceName() {
                return ProvinceName;
            }

            public int getCountryCode() {
                return CountryCode;
            }

            public String getCountryName() {
                return CountryName;
            }
        }

        public static class StudentCourseDetailsEntity {
            private String CourseSubType;
            private String CourseSubTypeName;
            private int LessonCount;
            /**
             * TeacherId : 1
             * TeacherName : sample string 2
             * CourseSubType : sample string 3
             */

            private List<StudentTeacherListEntity> StudentTeacherList;

            public void setCourseSubType(String CourseSubType) {
                this.CourseSubType = CourseSubType;
            }

            public void setCourseSubTypeName(String CourseSubTypeName) {
                this.CourseSubTypeName = CourseSubTypeName;
            }

            public void setLessonCount(int LessonCount) {
                this.LessonCount = LessonCount;
            }

            public void setStudentTeacherList(List<StudentTeacherListEntity> StudentTeacherList) {
                this.StudentTeacherList = StudentTeacherList;
            }

            public String getCourseSubType() {
                return CourseSubType;
            }

            public String getCourseSubTypeName() {
                return CourseSubTypeName;
            }

            public int getLessonCount() {
                return LessonCount;
            }

            public List<StudentTeacherListEntity> getStudentTeacherList() {
                return StudentTeacherList;
            }

            public static class StudentTeacherListEntity {
                private int TeacherId;
                private String TeacherName;
                private String CourseSubType;

                public void setTeacherId(int TeacherId) {
                    this.TeacherId = TeacherId;
                }

                public void setTeacherName(String TeacherName) {
                    this.TeacherName = TeacherName;
                }

                public void setCourseSubType(String CourseSubType) {
                    this.CourseSubType = CourseSubType;
                }

                public int getTeacherId() {
                    return TeacherId;
                }

                public String getTeacherName() {
                    return TeacherName;
                }

                public String getCourseSubType() {
                    return CourseSubType;
                }
            }
        }

        public static class HistoryExamDtoListEntity {
            private int Id;
            private int StudentCourseId;
            private String StudentGuid;
            private String Category;
            private String CourseType;
            private String CourseTypeName;
            private String ExamDate;
            private String ExamDateHis;
            private String TotalScore;
            private String Remark;
            /**
             * HistoryExamId : 1
             * Id : 2
             * CourseSubType : sample string 3
             * CourseSubTypeName : sample string 4
             * Score : 1.1
             */

            private List<DetailsCollectionEntity> DetailsCollection;

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setStudentCourseId(int StudentCourseId) {
                this.StudentCourseId = StudentCourseId;
            }

            public void setStudentGuid(String StudentGuid) {
                this.StudentGuid = StudentGuid;
            }

            public void setCategory(String Category) {
                this.Category = Category;
            }

            public void setCourseType(String CourseType) {
                this.CourseType = CourseType;
            }

            public void setCourseTypeName(String CourseTypeName) {
                this.CourseTypeName = CourseTypeName;
            }

            public void setExamDate(String ExamDate) {
                this.ExamDate = ExamDate;
            }

            public void setExamDateHis(String ExamDateHis) {
                this.ExamDateHis = ExamDateHis;
            }

            public void setTotalScore(String TotalScore) {
                this.TotalScore = TotalScore;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public void setDetailsCollection(List<DetailsCollectionEntity> DetailsCollection) {
                this.DetailsCollection = DetailsCollection;
            }

            public int getId() {
                return Id;
            }

            public int getStudentCourseId() {
                return StudentCourseId;
            }

            public String getStudentGuid() {
                return StudentGuid;
            }

            public String getCategory() {
                return Category;
            }

            public String getCourseType() {
                return CourseType;
            }

            public String getCourseTypeName() {
                return CourseTypeName;
            }

            public String getExamDate() {
                return ExamDate;
            }

            public String getExamDateHis() {
                return ExamDateHis;
            }

            public String getTotalScore() {
                return TotalScore;
            }

            public String getRemark() {
                return Remark;
            }

            public List<DetailsCollectionEntity> getDetailsCollection() {
                return DetailsCollection;
            }

            public static class DetailsCollectionEntity {
                private int HistoryExamId;
                private int Id;
                private String CourseSubType;
                private String CourseSubTypeName;
                private double Score;

                public void setHistoryExamId(int HistoryExamId) {
                    this.HistoryExamId = HistoryExamId;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public void setCourseSubType(String CourseSubType) {
                    this.CourseSubType = CourseSubType;
                }

                public void setCourseSubTypeName(String CourseSubTypeName) {
                    this.CourseSubTypeName = CourseSubTypeName;
                }

                public void setScore(double Score) {
                    this.Score = Score;
                }

                public int getHistoryExamId() {
                    return HistoryExamId;
                }

                public int getId() {
                    return Id;
                }

                public String getCourseSubType() {
                    return CourseSubType;
                }

                public String getCourseSubTypeName() {
                    return CourseSubTypeName;
                }

                public double getScore() {
                    return Score;
                }
            }
        }
    }
}
