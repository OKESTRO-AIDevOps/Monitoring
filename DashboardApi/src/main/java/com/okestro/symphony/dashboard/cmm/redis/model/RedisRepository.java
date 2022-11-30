package com.okestro.symphony.dashboard.cmm.redis.model;


//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;

//@RequiredArgsConstructor
//@Repository
public class RedisRepository {
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
//    private final RedisMessageListenerContainer redisMessageListener;
//    // 구독 처리 서비스
////    private final RedisSubscriber redisSubscriber;
//    // Redis
//    private static final String ROOMS = "ROOM";
//    private final RedisTemplate<String, Object> redisTemplate;
////    private HashOperations<String, String, RoomVo> opsHashRoom;
//    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
//    private Map<String, ChannelTopic> topics;
//
//    @PostConstruct
//    public void init() {
////        opsHashRoom = redisTemplate.opsForHash();
//        if(topics == null || topics.size() == 0){
//            topics = new HashMap<>();
//            this.createTopicAndListener("messageChannel");
//        }
//
//    }
//
//
//    public void createTopicAndListener(String topicName) {
//        ChannelTopic topic = topics.get(topicName);
//        if (topic == null) {
//            topic = new ChannelTopic(topicName);
////            redisMessageListener.addMessageListener(redisSubscriber, topic);
//            topics.put(topicName, topic);
//        }
//    }
//
//    public ChannelTopic getTopic(String topic) {
//        return topics.get(topic);
//    }
//
//    public void deleteTopicAndListener(String topicName){
//        ChannelTopic topic = topics.get(topicName);
////        redisMessageListener.removeMessageListener(redisSubscriber,topic);
//        topics.remove(topicName);
//
//    }
}

