package com.newlecture.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newlecture.web.entity.ChatEntity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class ChatHandler extends TextWebSocketHandler {
	
	HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
	ChatEntity cEnt = new ChatEntity();
	
   // connection established
   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	   log.info(">> afterConnectionEstablished " + session);
	   	
	   sessionMap.put(session.getId(), session);
   }
   
   // message
   @Override
   protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	   String msg = message.getPayload();
	   ObjectMapper mapper = new ObjectMapper();
	   cEnt = mapper.readValue(msg, ChatEntity.class);
	   System.out.println(">>> cEnt " + cEnt.getChatId());
	   /**
	    * ConcurrentModfiiacationException: null (동시 수정 오류) 
	    * for문 -> iteration 사용
	    */
	   
	   Set<String> set = new HashSet<>();
	   set = sessionMap.keySet();
	   Iterator<String> it = set.iterator();
	   
	   while (it.hasNext()) {
		   WebSocketSession wss = sessionMap.get(it.next());
		   try {
//			   wss.sendMessage(new TextMessage(msg));
			   
			   String msgObject = mapper.writeValueAsString(cEnt);
			   wss.sendMessage(new TextMessage(msgObject));
		   } catch (Exception e) {
//			   synchronized (sessionMap) {
//				   sessionMap.remove(it.next());
//			   }
		   }
	   }
	   log.info(">> handleTextMessage " + message.getPayload());
   }

   // connection closed
   @Override
   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	   log.info(">> afterConnectionClosed " + status);
   }
}
