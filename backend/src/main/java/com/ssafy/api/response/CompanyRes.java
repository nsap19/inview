package com.ssafy.api.response;

import com.ssafy.db.entity.Company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 전체 회사명 조회 API ([GET] /meeting/company) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("CompanyResponse")
public class CompanyRes {
	@ApiModelProperty(name = "1")
	private int id;
	@ApiModelProperty(name = "네이버")
	private String company_name;

	public static CompanyRes of(Company company) {
		return CompanyRes.builder().id(company.getCompanyId()).company_name(company.getCompanyName()).build();
	}
}
