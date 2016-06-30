package com.zhan.vip_teacher.network;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ApiUrls {
    //User - 用户登录
    public static final String TEACHER_LOGIN = "TeacherLogin";
    //老师ID获取当天课程信息
    public static final String TEACHER_DAILY_LESSON= "GetTeacherTodayLesson";
    //修改密码
    public static final String TEACHER_UPDATETEACHERPASSWORG = "UpdateTeacherPassword";
    //教师的学生列表
    public static final String TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST = "GetTeacherStudentCourseInfoList";
    //根据UserID获取教师信息
    public static final String TEACHER_INFO = "GetTeacherInfoByUserID";
    //学生档案
    public static final String GETTEACHERSTUDENTINFOBYDATA = "GetTeacherStudentInfoByData";
    //查询老师课表
    public static final String TEACHER_LESSON = "GetTeacherLessonByDate";
    //根据lessonId获取反馈信息及上堂课后作业 扩展带上学生信息
    public static final String GET_FEEDBACK = "GetFeedbackByLessonId_Extend";
    //待反馈课程列表
    public static final String GET_NO_FEEDBACK_LIST = "GetNoFeedBackList";
    //教师签到
    public static final String TEACHER_SIGN_IN = "SaveTeacherSignIn";
    //获取常用字典：上课状态，课程进度，作业评分，签到状态
    public static final String GET_COM_DIC = "GetCommDictionary";
    //课程反馈
    public static final String TEACHER_CLASS_FEEDBACK = "TeacherClassFeedback";
    //老师下学生的所有课表
    public static final String GET_STUDENT_LESSON_BYTEACHERID = "GetStudentLessonByTeacherId";
    //取当月教师的排课总数和未签到总数
    public static final String GET_NOTSIGNIN_AND_LESSONCOUNT = "GetNotSigninAndLessonCount";
    //课程统计数量
    public static final String GET_LESSONSTATISTIC_COUNTBYDATA= "GetLessonStatisticCountByData";
    //获取老师课程统计
    public static final String GET_LESSONSTATISTICSBYDATA = "GetLessonStatisticsByData";
    //教师签到统计
    public static final String GET_TEACHER_SIGNIN_TOTAL = "GeTeacherSignInTotal";
    //教师签到查询
    public static final String GET_TEACHER_SIGNIN_LIST = "GetTeacherSignInList";
    //教师催课
    public static final String TEACHER_HASTEN_LESSON = "TeacherHastenLesson";

    //APP升级
    public static final String  APPCLINE_UPGRADE = "http://beikao.api.zhan.com/AppClient/upgrade";
    //意见反馈
    public static final String  TEACHER_FEEDBACK = "http://userfeedback.tpooo.com/appmsg/api/message/addMessage.do";

    //添加事件
    public static final String  ADD_EVENT = "http://userfeedback.tpooo.com/AppEvent/appEvent/eventmsg/add.do";
}
