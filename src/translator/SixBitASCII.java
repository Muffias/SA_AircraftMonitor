package translator;





public final class SixBitASCII {
public static String bin2ASCII(String binString)
{
	String result = "";
	if(binString.length() % 6 == 0)
	{
		for(int i = 0; i < binString.length(); i=i+6)
		{
			if(binString.substring(i,i+1).equals("0"))
				result =  result + (char) (Integer.parseInt(binString.substring(i,i+6),2)+64);
			else
				result =  result + (char) (Integer.parseInt(binString.substring(i,i+6),2));
		}
		
		
	}
	else
		result = "404 Plane not Found (SIXBIT ERROR)";
	
	return result;	
}
public static void main(String[] args)
{
	System.out.println(bin2ASCII("000100110000000001001110001001000101001100"));
}
}
