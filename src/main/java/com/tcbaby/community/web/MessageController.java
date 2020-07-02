package com.tcbaby.community.web;

import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.Message;
import com.tcbaby.community.service.MessageService;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tcbaby
 * @date 20/05/13 10:29
 */
@Controller
@LoginRequired
@RequestMapping("/letter")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String queryCurrentUserConversations(Model model, Page page) {
        // 获取当前用户id
        int userId = HostHolder.getUser().getId();
        // 设置分页信息
        page.setSize(5);
        page.setTotal(messageService.countConversationByUserId(userId));
        page.setPath("/letter");
        // 分页 获取用户的会话列表
        List<Message> messages = messageService.queryConversationsByUserId(userId, page.getPage(), page.getSize());
        List<Map<String, Object>> conversations = new ArrayList<>();
        for (Message message : messages) {
            HashMap<String, Object> c = new HashMap<>();
            String conversationId = message.getConversationId();
            c.put("lastMessage", message);
            c.put("totalMessage", messageService.countConversationMessage(0, conversationId));
            c.put("totalUnReadMessage", messageService.countConversationUnreadMessage(userId, conversationId));
            // 设置私信人
            if (userId == message.getFromId()) {
                c.put("target", userService.queryUserById(message.getToId()));
            } else {
                c.put("target", userService.queryUserById(message.getFromId()));
            }
            conversations.add(c);
        }
        // 获取用户的未读消息数
        model.addAttribute("totalUnReadletter", messageService.countUnreadLetterByUserId(userId));
        model.addAttribute("conversations", conversations);
        return "/site/letter";
    }

    @GetMapping("/{fromId}")
    public String queryConversationDetail(Model model, Page page, @PathVariable("fromId") int fromId) {
        // 拼接会话Id
        int toId = HostHolder.getUser().getId();
        String conversationId = (toId < fromId) ? toId + "_" + fromId : fromId + "_" + toId;
        // 设置分页信息
        page.setTotal(messageService.countConversationMessage(0, conversationId));
        page.setSize(5);
        page.setPath("/letter/" + fromId);
        // 分页 获取对话内容
        List<Message> messages = messageService.queryMessageByConversationId(conversationId, page.getPage(), page.getSize());
        // 设置为已读状态
        List<Integer> messageIds = messages.stream().
                filter(m -> m.getStatus() == 0 && m.getToId() == toId).
                map(Message::getId).collect(Collectors.toList());
        messageService.updateMessageStatusToReaded(messageIds);

        model.addAttribute("target", userService.queryUserById((fromId)));
        model.addAttribute("messages", messages);
        return "/site/letter-detail";
    }

    /**
     * 发送私信
     * @return
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> sendMessage(String toUsername, String content) {
        messageService.sendMessage(toUsername, content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
