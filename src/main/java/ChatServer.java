import java.lang.System;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session; 
import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/chat")
public class ChatServer{

	private static Set<Session> conversas;

	@OnOpen
	public void OnOpen (Session session){
		conversas.add(session);
		String sessionId = session.getId();
		System.out.println(String.format("Usuario %s entrou no chat", sessionId));
		// adicionar dados extras.
	}

	@OnMessage
	public void OnMessage (String mensagem) throws IOException{

		try{
			for(Session conversa: conversas){
				conversa.getBasicRemote().sendText(mensagem);
			}
		}
		
		catch (IOException e){
			System.out.println(String.format("Erro no envio da mensagem: \"%s\"", mensagem));
		}	
	}

	@OnClose
	public void OnClose (Session session, CloseReason motivo){
		String sessionId = session.getId();
		System.out.println(String.format("Usuario %s saiu do chat", sessionId));
		System.out.println(String.format("%s", motivo.getReasonPhrase().toString()));
		conversas.remove(session);
		
	}

}
