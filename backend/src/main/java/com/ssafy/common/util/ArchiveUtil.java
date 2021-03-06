package com.ssafy.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.UserRepositorySupport;

@Component
public class ArchiveUtil {
	private final String fileSeparator = File.pathSeparator;
	private final String videoFileSeperator = "/";
	private final String dir = System.getProperty("user.dir");
	private final String src = dir + "files" + fileSeparator;

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	MeetingInsideService meetingInsideService;

	@Autowired
	ArchiveService arhciveService;

	public String getSavepath(ArchiveType archiveType, String meetingId) {

		if (archiveType.equals(ArchiveType.VIDEO)) {
			// docker path
			return "file:///tmp" + fileSeparator + "files" + fileSeparator + meetingId + fileSeparator
					+ String.valueOf(archiveType).toLowerCase();
		}

		return src + meetingId + fileSeparator + String.valueOf(archiveType).toLowerCase();
	}

	public String getFilename(ArchiveType archiveType, User user, String option, String extension) {
		LocalTime localTime = LocalTime.now();
		String salt = new SaltGenerator(localTime.toString()).toString();
		switch (archiveType) {
		case VIDEO:
			return String.valueOf(localTime.getHour()) + "시 " + String.valueOf(localTime.getMinute()) + "분 "
					+ String.valueOf(localTime.getSecond()) + "초 " + user.getNickname() + extension;
		case MEMO:
			try {
				// 현재 시간, 같은 파일명 업로드시 구분을 위한 salt로 사용
				return new MD5Generator(salt + option).toString() + "_" + salt + "_" + option;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		case EVALUATION:
			return option;
		case FILE:
			try {
				// 현재 시간, 같은 파일명 업로드시 구분을 위한 salt로 사용
				return new MD5Generator(salt + option).toString() + "_" + salt + "_" + option;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		case CHAT:
			return user.getNickname() + "_" + option + extension;
		default:
			break;
		}

		return "";
	}

	public String getFilepath(ArchiveType archiveType, String savepath, String filename) {
		if (archiveType.equals(ArchiveType.VIDEO)) {
			return savepath + videoFileSeperator + filename;
		}
		return savepath + fileSeparator + filename;
	}

	public String getOsFilepath(ArchiveType archiveType, String path) {
		if (archiveType.equals(ArchiveType.VIDEO)) {
			String OsFilePath = path.substring(11).replaceAll("/", Matcher.quoteReplacement(fileSeparator));
			return dir + OsFilePath;
		}

		return path;
	}

	public String getReverseSlashpath(String path) {
		String OsFilePath = path.replaceAll("/", Matcher.quoteReplacement(fileSeparator));
		String reverseSlashPath = OsFilePath.replaceAll(Matcher.quoteReplacement(fileSeparator), "/");

		return reverseSlashPath;
	}

	public void InsertToArchive(ArchiveType archiveType, String meetingId, String filepath, String filename,
			User user) {
		ArchiveRegisterPostReq archiveRegisterPostReq = new ArchiveRegisterPostReq();
		archiveRegisterPostReq.setArchiveType(archiveType);
		Meeting meeting = meetingInsideService.getMeeting(Integer.parseInt(meetingId));
		archiveRegisterPostReq.setMeeting(meeting);
		archiveRegisterPostReq.setPath(filepath);
		archiveRegisterPostReq.setArchiveName(filename);

		switch (archiveType) {
		case VIDEO:
			archiveRegisterPostReq.setUser(user);
			arhciveService.createArchive(archiveRegisterPostReq);
			break;
		case MEMO:
			archiveRegisterPostReq.setUser(user);
			arhciveService.createArchive(archiveRegisterPostReq);
			break;
		case EVALUATION:
			arhciveService.createAllEvaluation(archiveRegisterPostReq);
			break;
		case FILE:
			arhciveService.createAllArchive(archiveRegisterPostReq);
			break;
		case CHAT:
			archiveRegisterPostReq.setUser(user);
			arhciveService.createArchive(archiveRegisterPostReq);
			break;
		default:
			break;
		}
	}
}
