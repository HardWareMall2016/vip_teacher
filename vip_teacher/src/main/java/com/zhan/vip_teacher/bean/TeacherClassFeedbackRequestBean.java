package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by WuYue on 2016/3/14.
 */
public class TeacherClassFeedbackRequestBean extends BaseRequestBean{

    /**
     * LessonId : 1
     * AttendClassStatus : sample string 2
     * ClassStartTime : 2016-03-14 15:11:44
     * ClassEndTime : 2016-03-14 15:11:44
     * TaskStatus : sample string 3
     * TaskComment : sample string 4
     * RecordingFilePath : sample string 5
     * SkypeScreenshotFilePath : sample string 6
     * LastTask : sample string 7
     * ClassContent : sample string 8
     * Task : sample string 9
     * Summary : sample string 10
     * CreateTime : 2016-03-14 15:11:44
     * TeacherUpdateTime : 2016-03-14 15:11:44
     * Creater : 11
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int LessonId;
        private String AttendClassStatus;
        private String ClassStartTime;
        private String ClassEndTime;
        private String TaskStatus;
        private String TaskComment;
        private String RecordingFilePath;
        private String SkypeScreenshotFilePath;
        private String LastTask;
        private String ClassContent;
        private String Task;
        private String Summary;
        private String CreateTime;
        private String TeacherUpdateTime;
        private int Creater;

        public void setLessonId(int LessonId) {
            this.LessonId = LessonId;
        }

        public void setAttendClassStatus(String AttendClassStatus) {
            this.AttendClassStatus = AttendClassStatus;
        }

        public void setClassStartTime(String ClassStartTime) {
            this.ClassStartTime = ClassStartTime;
        }

        public void setClassEndTime(String ClassEndTime) {
            this.ClassEndTime = ClassEndTime;
        }

        public void setTaskStatus(String TaskStatus) {
            this.TaskStatus = TaskStatus;
        }

        public void setTaskComment(String TaskComment) {
            this.TaskComment = TaskComment;
        }

        public void setRecordingFilePath(String RecordingFilePath) {
            this.RecordingFilePath = RecordingFilePath;
        }

        public void setSkypeScreenshotFilePath(String SkypeScreenshotFilePath) {
            this.SkypeScreenshotFilePath = SkypeScreenshotFilePath;
        }

        public void setLastTask(String LastTask) {
            this.LastTask = LastTask;
        }

        public void setClassContent(String ClassContent) {
            this.ClassContent = ClassContent;
        }

        public void setTask(String Task) {
            this.Task = Task;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setTeacherUpdateTime(String TeacherUpdateTime) {
            this.TeacherUpdateTime = TeacherUpdateTime;
        }

        public void setCreater(int Creater) {
            this.Creater = Creater;
        }

        public int getLessonId() {
            return LessonId;
        }

        public String getAttendClassStatus() {
            return AttendClassStatus;
        }

        public String getClassStartTime() {
            return ClassStartTime;
        }

        public String getClassEndTime() {
            return ClassEndTime;
        }

        public String getTaskStatus() {
            return TaskStatus;
        }

        public String getTaskComment() {
            return TaskComment;
        }

        public String getRecordingFilePath() {
            return RecordingFilePath;
        }

        public String getSkypeScreenshotFilePath() {
            return SkypeScreenshotFilePath;
        }

        public String getLastTask() {
            return LastTask;
        }

        public String getClassContent() {
            return ClassContent;
        }

        public String getTask() {
            return Task;
        }

        public String getSummary() {
            return Summary;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public String getTeacherUpdateTime() {
            return TeacherUpdateTime;
        }

        public int getCreater() {
            return Creater;
        }
    }
}
