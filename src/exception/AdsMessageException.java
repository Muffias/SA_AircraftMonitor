package exception;

import redis.clients.jedis.exceptions.JedisDataException;
import domain.AdsMessage;


public class AdsMessageException extends JedisDataException
{
	private AdsMessage adsMessage;
	
	
	public AdsMessageException(int errNo, String errText, AdsMessage msg)
	{
		super("AdsMessageException No"+ errNo +": " + errText + "\n" + "Message Content: --Begin--\n" + msg.toString() +"\n--End Msg Content--");
		this.adsMessage= msg;
	}
	
	public AdsMessage getAdsMessage()
	{
		return this.adsMessage;
	}

}
