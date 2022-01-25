package com.ssafy.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 파일 정보 조회 API ([GET] /download/meeting/{meetingId}?archiveType=file) 요청에 대한
 * 응답값 정의.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("ArchiveResponse")
public class ArchiveRes {
	@ApiModelProperty(name = "archive_id", example = "1")
	private int id;

	@ApiModelProperty(name = "archive_type", example = "1")
	private int archive_type;

	@ApiModelProperty(name = "archive_name", example = "이미지 파일")
	private String archive_name;

	@ApiModelProperty(name = "path", example = "1")
	private String path;

}
