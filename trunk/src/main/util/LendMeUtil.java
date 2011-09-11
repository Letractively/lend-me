package main.util;

import java.util.ArrayList;
import java.util.List;

import entities.util.Message;
import entities.util.Topic;

public class LendMeUtil {
	
	public static String toOrganizedTopicsArray(List<Topic> topics) {
		StringBuilder orgTopicsArray = new StringBuilder();
		
		for (int i = 0; i < topics.size(); i++) {
			if (i != topics.size() - 1) {
				orgTopicsArray.append(topics.get(i).getSubject() + "; ");
			}
			else {
				orgTopicsArray.append(topics.get(i).getSubject());
			}
		}
		return orgTopicsArray.toString();
		
	}
	
	public static String toOrganizedMessagesArray(List<Message> messages) {
		StringBuilder orgMessagesArray = new StringBuilder();
		
		List<Message> msgList = new ArrayList<Message>(messages);
		
		for (int i = 0; i < msgList.size(); i++) {
			if (i != msgList.size() - 1) {
				orgMessagesArray.append(msgList.get(i).getMessage() + "; ");
			}
			else {
				orgMessagesArray.append(msgList.get(i).getMessage());
			}
		}
		return orgMessagesArray.toString();
		
	}
}