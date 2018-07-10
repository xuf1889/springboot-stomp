package demo.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GreetingController {
	@Autowired
	SocketSessionRegistry webAgentSessionRegistry;
	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping(value = "/index")
	public String index() {
		return "/index";
	}

	@RequestMapping(value = "/msg/message")
	public String ToMessage() {
		return "/message";
	}

	@RequestMapping(value = "/msg/messaget2")
	public String ToMessaget2() {
		return "/messaget2";
	}

	@GetMapping(value = "/msg/sendcommuser")
	public @ResponseBody OutMessage SendToCommUserMessage(HttpServletRequest request) {
		List<String> keys = webAgentSessionRegistry.getAllSessionIds().entrySet().stream().map(Map.Entry::getKey)
				.collect(Collectors.toList());
		Date date = new Date();
		keys.forEach(x -> {
			String sessionId = webAgentSessionRegistry.getSessionIds(x).stream().findFirst().get().toString();
			template.convertAndSendToUser(sessionId, "/topic/greetings",
					new OutMessage("commmsg：allsend, " + "send  comm" + date.getTime() + "!"),
					createHeaders(sessionId));

		});
		return new OutMessage("sendcommuser, " + new Date() + "!");
	}

	@MessageMapping("/msg/hellosingle")
	public void greeting2(InMessage message) throws Exception {
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("test", "test");
		String sessionId = webAgentSessionRegistry.getSessionIds(message.getId()).stream().findFirst().get();
		template.convertAndSendToUser(sessionId, "/topic/greetings",
				new OutMessage("single send to：" + message.getId() + ", from:" + message.getName() + "!"),
				createHeaders(sessionId));
	}

	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

}
