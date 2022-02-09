package com.ssafy.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.common.util.ArchiveUtil;
import com.ssafy.common.util.MeetingParticipant;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepositorySupport;

@Service("chatMessageService")
public class ChatMessageServiceImple implements ChatMessageService {
	static boolean subscribe = false;
	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	MeetingInsideService meetingInsideService;

	@Autowired
	ArchiveService arhciveService;

	@Autowired
	ArchiveUtil archiveUtil;

	@Autowired
	private MeetingParticipant meetingParticipant;

	@Override
	public void saveChatMessage(ChatMessage message, String ope) {
		String meetingId = message.getMeetingId();
		String sender = message.getSender();
		String receiver = message.getReceiver() == "" ? "모두" : message.getReceiver();
		String msg = message.getMessage();
		String date = message.getDate();
		String time = message.getTime();
		List<User> participantList = meetingParticipant.getParticipantByMeetingId(meetingId);
		if (ope.equals("send")) {
			for (User user : participantList) {
				saveFile(meetingId, user, receiver,
						date + "\t" + time + "\t" + sender + "가 " + receiver + "에게 : " + msg + "\n");
			}
		} else {
			User u = userRepositorySupport.findUserByNickname(sender);
			subscribe = false;
			if (ope.equals("subscribe")) {
				subscribe = true;
			}
			for (User user : participantList) {
				saveFile(meetingId, user, receiver, date + "\t" + time + "\t" + msg + "\n");
			}
		}
	}

	// 파일를 저장하는 함수
	private void saveFile(String meetingId, User user, String receiver, String message) {
		/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
		ArchiveType archiveType = ArchiveType.CHAT;
		String savepath = archiveUtil.getSavepath(archiveType, meetingId);
		String filename = archiveUtil.getFilename(archiveType, user, receiver, ".txt");
		String filepath = archiveUtil.getFilepath(archiveType, savepath, filename);
		/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
		if (!new File(savepath).exists()) {
			try {
				new File(savepath).mkdirs();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		System.out.println(filepath);
		if (subscribe && !isReadableFile(filepath)) {
			archiveUtil.InsertToArchive(archiveType, meetingId, filepath, filename, user);
		}

		// 파일에 메시지를 저장한다.
		try (FileOutputStream stream = new FileOutputStream(filepath, true)) {
			stream.write(message.getBytes("UTF-8"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private boolean isReadableFile(String filePath) {
		// d드라이브의 chat 폴더의 chat 파일
		File file = new File(filePath);
		// 파일 있는지 검사
		if (!file.exists()) {
			return false;
		}
		return true;
	}
}
