package com.tcn.funcommon.sign.rsa;

/**
 * 数据返回实体
 * 
 * @author 李国平
 * @date 2016年7月20日
 */
public class Result {
	
	private String code = "0";//状态编码0-失败，1成功
	
	private String msg;//附带的提示信息
	
	private Object data;//数据对象
	public Result(){
		
		
	}
	
	/**
	 * @param code 状态码
	 * @param msg 附带消息
	 * @param data Object
	 */
	public Result(String code, String msg, Object data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 * @param code 状态码
	 * @param msg 附带消息
	 */
	public Result(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	/**
	 * @param code 状态码
	 * @param msg 附带消息
	 */
	public Result(String code){
		this.code = code;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
