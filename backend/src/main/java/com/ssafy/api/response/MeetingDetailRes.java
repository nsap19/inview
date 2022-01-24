package com.ssafy.api.response;

import java.util.Date;
import java.util.List;

public class MeetingDetailRes {
	int id;

	String title;

	Date start_time;

	Date close_time;

	String host_nickname;

	List<String> participant_nickname_list;

}
