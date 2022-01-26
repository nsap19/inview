package com.ssafy.api.response;

import com.ssafy.db.entity.Industry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 전체 직군 조회 API ([GET] /meeting/industry) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("IndustryResponse")
public class IndustryRes {
	@ApiModelProperty(name = "아이디", example = "1")
	private int id;
	@ApiModelProperty(name = "직군 이름", example = "IT")
	private String industryName;

	public static IndustryRes of(Industry industry) {
		return IndustryRes.builder().id(industry.getIndustryId()).industryName(industry.getIndustryName()).build();
	}
}
