package com.hechi.niumall.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png文件"),
    FILE_SIZE_ERROR(508,"文件大小超过限制"),

    LOGIN_TIMEOUT(1001,"说明登录过期  提示重新登录"),
    ACCOUNT_LOCKED(1002,"被锁定或封禁 提示重新登录"),
    LOGIN_ERROR(1003,"用户名或密码错误"),
    LOGIN_TOKEN_ERROR(1004,"用户令牌异常 提示重新登录"),

    SLIDESHOW_IS_NULL(1500,"轮播图为空"),
    SLIDESHOW_UPDATA_ERROE(1501,"轮播图更新失败"),

    ADD_CATEGORY_ERROR(2000,"添加新的类别出错"),
    CATEGORY_ERROR(2001,"商品类别不存在"),
    UPDATE_CATEGORY_ERROR(2002,"更新商品类别出错"),


    GOODS_IS_NOT_EXTST(3000,"商品不存在"),
    CATEGORY_GOODS_ISNULL(3001,"该品牌商品为空"),
    CATEGORY_GOODS_NOT_EXIST(3002,"该分类的商品为空"),
    GOODS_UPDATE_ERROR(3003,"更新商品出错"),
    GOODS_DELEDTE_ERROR(3004,"删除商品"),
    GOODS_ADD_ERROR(3005,"添加商品出错"),

    ORDER_IS_NULL(4000,"订单不存在"),
    ORDER_USER_IS_NULL(4001,"该用户查询不到订单"),
    SHOPCART_ERROE(4501,"添加购物车出错"),
    SHOPCART_DELETE_ERROE(4502,"删除购物车出错"),


    USER_ADDR_NOTEXIST(5000,"用户地址不存在"),
    USER_ADD_ADDR_ERROR(5001,"新建用户地址出错"),
    USER_UPDATA_ADDR_ERROR(5002,"更新用户地址出错"),
    USER_DELETE_ADDR_ERROR(5003,"移除用户地址出错"),


    ALI_PAY_SIGN_ERROR(6000,"支付成功异步通知验签失败"),
    ALI_PAY_TOTAL_AMOUNT_ERROR(6001,"支付宝金额校验失败"),
    ALI_PAY_SELLERID_ERROR(6002,"商家pId校验失败"),
    ALI_PAY_APPID_ERROR(6003,"商家appId校验失败"),
    ALI_PAY_SELL_ERROR(6004,"支付失败"),
    BANANCE_NOT_ENOUGH(6005,"余额不足");


    int code;
    String msg;
    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}