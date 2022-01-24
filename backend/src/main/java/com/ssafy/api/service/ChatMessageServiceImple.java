package com.ssafy.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.db.entity.ChatMessage;

@Service("chatMessageService")
public class ChatMessageServiceImple implements ChatMessageService {
	static HashMap<String, List<String>> chatMap = new HashMap<>();
	static String path = "C:\\WorkSpace\\web-project\\File\\Chat\\";

	@Override
	public void saveChatMessage(ChatMessage message, String ope) {
		String id = message.getMeetingId();
		String sender = message.getSender();
		String receiver = message.getReceiver() == "" ? "모두" : message.getReceiver();
		String msg = message.getMessage();
		String date = message.getDate();
		String time = message.getTime();
		String newPath = path + id + "\\";
		List<String> participantList = chatMap.getOrDefault(id, new LinkedList<>());
		if (ope.equals("send")) {
			for (String userName : participantList) {
				saveFile(newPath + userName + "_" + receiver + ".txt",
						date + "\t" + time + "\t" + sender + "가 " + receiver + "에게 : " + msg + "\n");
			}
		} else {
			if (ope.equals("subscribe")) {
				// 구독자 추가
				participantList.add(sender);
			} else {
				// 구독자 삭제
				participantList.remove(sender);
			}
			for (String userName : participantList) {
				saveFile(newPath + userName + "_" + receiver + ".txt", date + "\t" + time + "\t" + msg + "\n");
			}
			chatMap.put(id, participantList);
		}
	}

	// 채팅 내용을 파일로 부터 읽어온다.
	private String readFile(String path) {
		// d드라이브의 chat 폴더의 chat 파일
		File file = new File(path);
		// 파일 있는지 검사
		if (!file.exists()) {
			return "";
		}
		// 파일을 읽어온다.
		try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
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
	private void saveFile(String path, String message) {
		// 파일에 메시지를 저장한다.
		try (FileOutputStream stream = new FileOutputStream(path, true)) {
			stream.write(message.getBytes("UTF-8"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
