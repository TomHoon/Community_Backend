package com.newlecture.web.entity;

import lombok.Data;

public class CommentEntity {
	String comment_idx;
	String member_id;
	String id;
	String comment_content;
	String comment_reg_date;
	String comment_amnd_date;
	String comment_del_yn;
	String comment_step;
	String comment_order;
	String comment_group;
	String comment_like;
	String comment_report;
	String comment_recommend;
	String comment_unrecommend;
	String comment_up_id_list;
	String comment_down_id_list;
	String comment_ud_temp_id;
	
	public String getComment_idx() {
		return comment_idx;
	}
	public void setComment_idx(String comment_idx) {
		this.comment_idx = comment_idx;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_reg_date() {
		return comment_reg_date;
	}
	public void setComment_reg_date(String comment_reg_date) {
		this.comment_reg_date = comment_reg_date;
	}
	public String getComment_amnd_date() {
		return comment_amnd_date;
	}
	public void setComment_amnd_date(String comment_amnd_date) {
		this.comment_amnd_date = comment_amnd_date;
	}
	public String getComment_del_yn() {
		return comment_del_yn;
	}
	public void setComment_del_yn(String comment_del_yn) {
		this.comment_del_yn = comment_del_yn;
	}
	public String getComment_step() {
		return comment_step;
	}
	public void setComment_step(String comment_step) {
		this.comment_step = comment_step;
	}
	public String getComment_order() {
		return comment_order;
	}
	public void setComment_order(String comment_order) {
		this.comment_order = comment_order;
	}
	public String getComment_group() {
		return comment_group;
	}
	public void setComment_group(String comment_group) {
		this.comment_group = comment_group;
	}
	public String getComment_like() {
		return comment_like;
	}
	public void setComment_like(String comment_like) {
		this.comment_like = comment_like;
	}
	public String getComment_report() {
		return comment_report;
	}
	public void setComment_report(String comment_report) {
		this.comment_report = comment_report;
	}
	public String getComment_recommend() {
		return comment_recommend;
	}
	public void setComment_recommend(String comment_recommend) {
		this.comment_recommend = comment_recommend;
	}
	public String getComment_unrecommend() {
		return comment_unrecommend;
	}
	public void setComment_unrecommend(String comment_unrecommend) {
		this.comment_unrecommend = comment_unrecommend;
	}
	public String getComment_ud_temp_id() {
		return comment_ud_temp_id;
	}
	public void setComment_ud_temp_id(String comment_ud_temp_id) {
		this.comment_ud_temp_id = comment_ud_temp_id;
	}
	public String getComment_up_id_list() {
		return comment_up_id_list;
	}
	public void setComment_up_id_list(String comment_up_id_list) {
		this.comment_up_id_list = comment_up_id_list;
	}
	public String getComment_down_id_list() {
		return comment_down_id_list;
	}
	public void setComment_down_id_list(String comment_down_id_list) {
		this.comment_down_id_list = comment_down_id_list;
	}
	
	
}
