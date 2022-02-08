package com.ssafy.api.service;

import java.io.FileOutputStream;

import org.springframework.stereotype.Service;

@Service("videoChatService")
public class VideoChatServiceImpl implements VideoChatService {
	static String path = "file:///tmp/HelloWorldRecorded.webm";
	
	@Override
	public void saveVideoChat() {
		try(FileOutputStream stream= new FileOutputStream(this.path, true)){
//			stream.write();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

}
