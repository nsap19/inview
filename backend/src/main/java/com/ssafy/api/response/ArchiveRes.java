package com.ssafy.api.response;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.ssafy.db.entity.Archive;
import com.ssafy.db.entity.ArchiveType;

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
	@ApiModelProperty(name = "archiveId", example = "1")
	private int id;

	@ApiModelProperty(name = "archiveType", example = "1")
	private ArchiveType archiveType;

	@ApiModelProperty(name = "archiveName", example = "이미지 파일.png")
	private String archiveName;

	@ApiModelProperty(name = "path", example = "C:\\WorkSpace\\web-project\\S06P12A201\\backend\\files\\1\\file\\")
	private String path;

	public static List<ArchiveRes> of(List<Archive> archives) {
		List<ArchiveRes> result = new LinkedList<ArchiveRes>();
		String originalName = "";
		for (Archive archive : archives) {
			switch (archive.getArchiveType()) {
			case VIDEO:
				break;
			case MEMO:
				break;
			case EVALUATION:
				break;
			case FILE:
				StringTokenizer st = new StringTokenizer(archive.getArchiveName(), "_");
				st.nextToken();
				StringBuilder sb = new StringBuilder();
				while (st.hasMoreTokens()) {
					sb.append(st.nextToken()).append("_");
				}
				sb.setLength(sb.length() - 1);
				originalName = String.valueOf(sb);
				break;
			case CHAT:
				break;
			}

			result.add(ArchiveRes.builder()
					.id(archive.getArchiveId())
					.archiveType(archive.getArchiveType())
					.archiveName(originalName)
					.path(archive.getPath())
					.build());
		}

		return result;
	}

}
