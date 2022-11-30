package com.okestro.symphony.dashboard.cmm.redis.svc;


//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.okestro.symphony.dashboard.cmm.websocket.model.WsVo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Service;

//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class RedisService implements MessageListener {
public class RedisService {

//    private final ObjectMapper objectMapper;
//    private final RedisTemplate redisTemplate;
//    private final SimpMessageSendingOperations messagingTemplate;
//
//    /**
//     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
//     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            // redis에서 발행된 데이터를 받아 deserialize(역직렬화)
//            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            // SendMessage 객채로 맵핑
//            WsVo sendMessage = objectMapper.readValue(publishMessage, WsVo.class);
//            // Websocket 구독자에게 채팅 메시지 Send
//            if(sendMessage.getDest() == null || sendMessage.getDest().equals("")){
//                messagingTemplate.convertAndSend("/topic/adminMessage" + sendMessage.getIdToken(), sendMessage);
//            } else {
//                messagingTemplate.convertAndSend(sendMessage.getDest() + sendMessage.getIdToken(), sendMessage);
//            }
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    /**
//     * Redis에서 메시지가 발행(publish)을 한다.
//     */
//    public void publish(ChannelTopic topic, WsVo message) {
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//    }

}

