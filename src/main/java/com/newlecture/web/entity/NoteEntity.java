package com.newlecture.web.entity;

public class NoteEntity {

	String note_idx;
	String send_id;
	String note_title;
	String note_content;
	String send_date;
	String read_date;
	String read_yn;
	String send_del_yn;
	String recv_del_yn;

	public String getNote_idx() {
		return note_idx;
	}

	public void setNote_idx(String note_idx) {
		this.note_idx = note_idx;
	}

	public String getSend_id() {
		return send_id;
	}

	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}

	public String getNote_title() {
		return note_title;
	}

	public void setNote_title(String note_title) {
		this.note_title = note_title;
	}

	public String getNote_content() {
		return note_content;
	}

	public void setNote_content(String note_content) {
		this.note_content = note_content;
	}

	public String getSend_date() {
		return send_date;
	}

	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}

	public String getRead_date() {
		return read_date;
	}

	public void setRead_date(String read_date) {
		this.read_date = read_date;
	}

	public String getRead_yn() {
		return read_yn;
	}

	public void setRead_yn(String read_yn) {
		this.read_yn = read_yn;
	}

	public String getSend_del_yn() {
		return send_del_yn;
	}

	public void setSend_del_yn(String send_del_yn) {
		this.send_del_yn = send_del_yn;
	}

	public String getRecv_del_yn() {
		return recv_del_yn;
	}

	public void setRecv_del_yn(String recv_del_yn) {
		this.recv_del_yn = recv_del_yn;
	}


}
