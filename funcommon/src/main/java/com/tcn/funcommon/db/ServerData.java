package com.tcn.funcommon.db;

public class ServerData {
	private int id;
	private String DATA;
	private String Time;
	private int Upflag;

	public ServerData() {
		super();
	}

	public ServerData(int id, String dATA, String time,int upflag) {
		super();
		this.id = id;
		DATA = dATA;
		Time = time;
		Upflag=upflag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDATA() {
		return DATA;
	}

	public void setDATA(String dATA) {
		DATA = dATA;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public int getUpflag() {
		return Upflag;
	}

	public void setUpflag(int i) {
		Upflag = i;
	}

	@Override
	public String toString() {
		return "ServerData [id=" + id + ", DATA=" + DATA + ", Time=" + Time
				+ ", Upflag=" + Upflag + "]";
	}
}
