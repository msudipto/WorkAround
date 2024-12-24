//package coms309.service;
//import coms309.entity.Message;
//import coms309.entity.UserProfile;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//
//@Service
//public class ChatService {
//    @Autowired
//    private GroupChatRepository groupChatRepository;
//
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//
//    public GroupChat createGroupChat(List<UserProfile> users, String groupChatName) {
//        GroupChat groupChat = new GroupChat();
//        groupChat.setGroupChatName(groupChatName);
//        groupChat.setUsers(users);
//        return groupChatRepository.save(groupChat);
//    }
//    public GroupChat getGroupChatById(Long groupChatId) {
//        return groupChatRepository.findById(groupChatId)
//                .orElseThrow(() -> new RuntimeException("GroupChat not found with id: " + groupChatId));
//    }
//
//
//    public List<Message> getChatHistory(Long groupChatId) {
//        Optional<GroupChat> groupChat = groupChatRepository.findById(groupChatId);
//        return messageRepository.findAllByGroupChat(groupChat.get());       //TODO: sort
//    }
//
//
////    public void sendChatHistory(Long groupChatId, String sessionId, SimpMessagingTemplate simpMessagingTemplate) {
////        List<Message> messages = getChatHistory(groupChatId);
////        for (Message message : messages) {
////            simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/history", message);
////        }
////    }
//
//
//
//
//    public Message addMessageToGroupChat(Long groupChatId, String content, String sender) {
//        Message message = new Message();
//        message.setContent(content);
//        message.setSender(sender);
//        message.setTimestamp(LocalDateTime.now());
//
//
//        Optional<GroupChat> gp = groupChatRepository.findById(groupChatId);
//        message.setGroupChat(gp.get());
//        return messageRepository.save(message);
//    }
//
//
//}
