package com.ssafy.api.response;

import com.ssafy.db.entity.Company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 전체 회사명 조회 API ([GET] /meeting/company) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@Builder
@ApiModel("CompanyResponse")
public class CompanyRes {
	@ApiModelProperty(name = "아이디", example = "1")
	private int id;
	@ApiModelProperty(name = "회사 이름", example = "네이버")
	private String companyName;

	public static CompanyRes of(Company company) {
		return CompanyRes.builder().id(company.getCompanyId()).companyName(company.getCompanyName()).build();
	}
}
