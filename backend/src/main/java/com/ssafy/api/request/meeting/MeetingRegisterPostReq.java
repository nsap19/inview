package com.ssafy.api.request.meeting;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.db.entity.Industry;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.entity.meeting.Status;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 미팅 API ([POST] /meeting) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("MeetingRegisterPostReq")
public class MeetingRegisterPostReq {
	@ApiModelProperty(name = "제목", example = "방 제목")
	@NotNull
	String title;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "시작 시간", example = "2022-01-18 11:15:06")
	Date start_time;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "예상 종료 시간", example = "2022-01-18 13:15:06")
	Date end_time;

	@ApiModelProperty(name = "비밀번호", example = "1111")
	String password;

	@ApiModelProperty(name = "최대 참여 인원", example = "5")
	@Max(10)
	@Min(2)
	int user_limit = 6;

	@ApiModelProperty(name = "산업군명", example = "IT")
	String industry_name;

	@ApiModelProperty(name = "회사명", example = "[\"카카오\", \"네이버\"]")
	List<String> company_name_list;

	public Meeting toMeeting(User host, Industry industry) {
		return Meeting.builder().title(this.title).startTime(this.start_time).endTime(this.end_time)
				.password(this.password).userLimit(this.user_limit).user(host).industry(industry)
				.url(UUID.randomUUID().toString()).status(Status.WAITING).build();
	}

}