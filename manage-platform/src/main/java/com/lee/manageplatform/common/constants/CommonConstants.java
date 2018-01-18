package com.lee.manageplatform.common.constants;

public class CommonConstants {
    public static final Short DELETE_STATE = 0;//删除状态

    public static final Short TEMP_SAVE = 1;//待编辑状态

    public static final Short WAIT_AUDIT = 2;//待审核

    public static final Short LEVEL_1_AUDTI_REJECT = 3;//初审拒绝

    public static final Short LEVEL_1_AUDTI_PASS = 4;//一级审核通过

    public static final Short LEVEL_2_AUDTI_REJECT = 5;//终审拒绝

    public static final Short PUT_ON_STATE = 6;//上架状态

    public static final Short APPLY_FUT_OFF = 7;//申请下架

    public static final Short PUT_OFF_STATE = 8;//下架状态

    public static final Short APPLY_FUT_OFF_REJECT = 9;//申请下架驳回

    public static final Short ORDER_EFFECT_STATUS = 10;//预约生效

    public static final Short AURGENT_FUT_OFF = 13;//紧急下架

    public static final Short CANCLE = 14;//撤销

    /*
    * 审核类型
    * */
    public static final int AUDIT_TYPE_PRODUCT = 1;//产品
    public static final int AUDIT_TYPE_TERMINAL = 2;//终端
    public static final int AUDIT_TYPE_SOLUION = 3;//解决方案
    public static final int AUDIT_TYPE_TRADE = 4;//行业
    public static final int AUDIT_TYPE_CASE= 5;//客户案例
    public static final int AUDIT_TYPE_NEWS = 6;//新闻
    public static final int AUDIT_TYPE_EXB = 7;//展会
    public static final int AUDIT_TYPE_REC_SOLUTION = 8;//推荐解决方案
    public static final int AUDIT_TYPE_REC_TERMINAL = 9;//推荐终端
    public static final int AUDIT_TYPE_REC_PRODUCT = 10;//推荐产品
    public static final int AUDIT_TYPE_HOT_PRODUCT = 11;//热门产品
    public static final int AUDIT_TYPE_HOME_BANNER = 12;//首页banner


    /**
     * 服务与支持模块——问题单常量定义
     */
    public static final short SUPPORT_QUESTION_OPINIONS = 1; //意见与反馈  的类型
    public static final short SUPPORT_QUESTION_PROBLEM = 2; //产品问题与建议描述内容 的类型
    
    public static final short SUPPORT_QUESTION_DOING= 1;    // 问题单 处理中
    public static final short SUPPORT_QUESTION_COMPLETED = 2;   // 问题单 已完成
    
    public static final short SUPPORT_QUESTION_FLOW_QUEST = 1;   // 客户提问流水
    public static final short SUPPORT_QUESTION_FLOW_REPLY = 2;    // 客服回复流水
    
    public static final String SUPPORT_QUESTION_EXIST_REPLY = "1";    // 回复流水中存在管理员回复流水
    
    public static final int ARTICLE_TYPE_COOPERATION_PARTNER = 3;   // 合作伙伴类型的新闻动态

    public static final int PRODUCT_CATEGORY_ID_CLOUD = 10004;    // 产品大类【云+】ID
    public static final int PRODUCT_CATEGORY_ID_TRADE = 10005;    // 产品大类【行业应用】ID

    //审核状态枚举
    public static final String AUDIT_SUBMIT = "提交审核";    // 提交审核
    public static final String AUDIT_FIRST_REJECT = "初审驳回";    // 初审驳回
    public static final String AUDIT_FIRST_PASS = "初审通过";    // 初审通过
    public static final String AUDIT_TWO_REJECT = "终审驳回";    // 终审驳回
    public static final String AUDIT_TWO_PASS = "终审通过";    // 终审通过
    public static final String AUDIT_APPLY_OUT = "申请下架";    // 申请下架
    public static final String AUDIT_APPLY_PASS = "下架通过";    // 下架通过
    public static final String AUDIT_APPLY_REJECT = "下架驳回";    // 下架驳回
    public static final String AUDIT_APPLY_URGENCY = "紧急下架";    // 紧急下架
    public static final String AUDIT_APPLY_REVOCATION = "撤销";    // 撤销
    public static final String ORDER_APPLY_OFF = "预约下架";    // 预约下架
    public static final String ORDER_APPLY_ON = "预约上架";    // 预约上架


    //预约上下架
    public static final int SAVE_SUCCESS=2;//保存成功
    public static final int SAVE_DEFAUL=0;//默认 表示有无值
    public static final int ALREADY_EXISTED=1;//已经存在值
    public static final int SAVE_ERROR=3;//保存成功
    /*数据权限类型*/
    public static int TRADE_DATA_PERMISSION = 1;

    public static int PRODUCT_DATA_PERMISSION = 2;

}
