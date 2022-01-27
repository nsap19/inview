package com.ssafy.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.common.util.CurParticipant;
import com.ssafy.common.util.MD5Generator;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
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

	@Override
	public void saveChatMessage(ChatMessage message, String ope) {
		String meetingId = message.getMeetingId();
		String sender = message.getSender();
		String receiver = message.getReceiver() == "" ? "모두" : message.getReceiver();
		String msg = message.getMessage();
		String date = message.getDate();
		String time = message.getTime();
		List<User> participantList = new CurParticipant().getParticipantList(meetingId);
		if (ope.equals("send")) {
			for (User user : participantList) {
				saveFile(meetingId, user, receiver,
						date + "\t" + time + "\t" + sender + "가 " + receiver + "에게 : " + msg + "\n");
			}
		} else {
			User u = userRepositorySupport.findUserByNickname(sender).get();
			subscribe = false;
			if (ope.equals("subscribe")) {
				// 구독자 추가
				participantList.add(u);
				subscribe = true;
			} else {
				// 구독자 삭제
				participantList.remove(u);
			}
			for (User user : participantList) {
				saveFile(meetingId, user, receiver, date + "\t" + time + "\t" + msg + "\n");
			}
			new CurParticipant().setParticipantList(meetingId, participantList);
		}
	}

	// 채팅 내용을 파일로 부터 읽어온다.
	private String readFile(String meetingId, String sender, String receiver) {
		String savePath = System.getProperty("user.dir") + "\\files\\" + meetingId + "\\chat";
		String filePath = savePath + "\\" + sender + "_" + receiver + ".txt";

		// d드라이브의 chat 폴더의 chat 파일
		File file = new File(filePath);
		// 파일 있는지 검사
		if (!file.exists()) {
			return "";
		}
		// 파일을 읽어온다.
		try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
			byte[] bytesData = new byte[(int) raf.length()];
			raf.readFully(bytesData);
			raf.close();
			return new String(bytesData);
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	// 파일를 저장하는 함수
	private void saveFile(String meetingId, User user, String receiver, String message) {
		/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
		String savePath = System.getProperty("user.dir") + "\\" + meetingId + "\\chat";
		String filename = user.getNickname() + "_" + receiver + ".txt";
		/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
		if (!new File(savePath).exists()) {
			try {
				new File(savePath).mkdir();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		String filePath = savePath + "\\" + filename;

		if (subscribe) {
			chatInsertToArchive(meetingId, filePath, filename, user);
		}

		// 파일에 메시지를 저장한다.
		try (FileOutputStream stream = new FileOutputStream(filePath, true)) {
			stream.write(message.getBytes("UTF-8"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void chatInsertToArchive(String meetingId, String filepath, String filename, User user) {
		ArchiveRegisterPostReq archiveRegisterPostReq = new ArchiveRegisterPostReq();

		archiveRegisterPostReq.setPath(filepath);
		archiveRegisterPostReq.setArchiveName(filename);
		archiveRegisterPostReq.setArchiveType(ArchiveType.CHAT); // chat
		archiveRegisterPostReq.setUser(user);
		Meeting meeting = meetingInsideService.getMeeting(Integer.parseInt(meetingId));
		archiveRegisterPostReq.setMeeting(meeting);

		arhciveService.createArchive(archiveRegisterPostReq);
	}

}
