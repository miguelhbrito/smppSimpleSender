package primeiroSMPP.smpp;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppBindException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppInvalidArgumentException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		DefaultSmppClient client = new DefaultSmppClient();

		SmppSessionConfiguration sessionConfig = new SmppSessionConfiguration();

		sessionConfig.setType(SmppBindType.TRANSCEIVER);
		sessionConfig.setHost("10.13.5.46");
		sessionConfig.setPort(2775);
		sessionConfig.setSystemId("smppclient1");
		sessionConfig.setPassword("password");

		try {
			SmppSession session = client.bind(sessionConfig);

			SubmitSm sm = createSubmitSm("987654321", "123456789", "Teste cliente se conectando "
					+ "e enviando 1 mensagem !!!", "UTF-8");
			System.out.println("Enviando mensagem");

			session.submit(sm, TimeUnit.SECONDS.toMillis(100));

			System.out.println("Mensagem enviada !!!");
			System.out.println("Esperando 10 segundos para destruir tudo");
			TimeUnit.SECONDS.sleep(10);
			System.out.println("Desfazendo conexao");

			session.close();
			session.destroy();

			System.out.println("Destruindo o cliente");
			client.destroy();
			System.out.println("Até mais !");
			
			//ERROS QUE PODEM DAR 
		} catch (SmppTimeoutException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SmppChannelException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SmppBindException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnrecoverablePduException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RecoverablePduException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static SubmitSm createSubmitSm(String origem, String destino, String texto, String encode)
			throws SmppInvalidArgumentException {
		SubmitSm sm = new SubmitSm();
		sm.setSourceAddress(new Address((byte) 5, (byte) 0, origem));
		sm.setDestAddress(new Address((byte) 1, (byte) 1, destino));
		sm.setDataCoding((byte) 8);
		sm.setShortMessage(CharsetUtil.encode(texto, encode));

		return sm;
	}
}