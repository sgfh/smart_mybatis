package com.smart.mybatis.pojo;



public class BaseVo<T> {

	public static final String CODE = "code";
	public static final String MSG = "msg";

	public final static String CODE_SUC = "success";

	public final static String CODE_ERR = "error";

	public static final String CODE_ERR_PARAM = "error_param";

	// 未认证错误
	public static final String CODE_ERR_UNAUTH = "error_unauth";

	public static final String CODE_ERR_EXIST = "error_exist";

	public static final String CODE_ERR_UNEXIST = "error_unexist";

	public final static String CODE_ERR_PWD = "error_pwd";

	public final static String MSG_SUC = "操作成功!";

	public final static String MSG_ERR = "操作错误!";

	public final static String MSG_ERR_PARAM = "参数错误!";

	public final static String MSG_UNAUTH = "请登入后操作!";

	private String code = CODE_SUC;

	private String msg = CODE_SUC;

	private T content;
	// 额外的参数
	private Object additional;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		if (CODE_ERR_PARAM.equals(code)) {
			this.msg = MSG_ERR_PARAM;
		} else if (CODE_ERR_UNAUTH.equals(code)) {
			this.msg = MSG_UNAUTH;
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public Object getAdditional() {
		return additional;
	}

	public void setAdditional(Object additional) {
		this.additional = additional;
	}

}