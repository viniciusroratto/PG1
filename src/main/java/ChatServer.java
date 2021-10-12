import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session; 
import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/chat")
public class ChatServer{
	private static Set<Session> conversas = new HashSet<Session>();
	Logger logger = Logger.getLogger(ChatServer.class.getName());

	@OnOpen
	public void OnOpen (Session session){
		conversas.add(session);
		String sessionId = session.getId();
		logger.log(  Level.INFO, "Usuario {0} entrou no chat", sessionId);
		// adicionar dados extras.
	}

	@OnMessage
	public void OnMessage (String mensagem, Session session) throws IOException{

		try{
			for(Session conversa: conversas){
				conversa.getBasicRemote().sendText(mensagem);
			}
			logger.log(Level.INFO, "Usuario {0} enviou mensagem!", session.getId());
		}
		
		catch (IOException e){
			logger.log(Level.SEVERE, "Erro no envio de mensagem: \"{0}\"", mensagem);
		}	
	}

	@OnClose
	public void OnClose (Session session, CloseReason motivo) throws IOException { 
		String sessionId = session.getId();
		session.close(motivo);
		logger.log(Level.INFO, "Usuario {0} saiu do chat!", sessionId);
		//logger.log(Level.INFO, "{0}", motivo.getReasonPhrase().toString()); - Retorna nulo...
		conversas.remove(session);
	}
}
