



public final class BooleanHelper {
public static boolean integer2Boolean(int val)
{
	if(val == 0)
		return false;
	else
		return true;
}
public static boolean string2boolean(String val,int pos)
{
	if(val.charAt(pos) == '0')
		return false;
	else
		return true;
}
public static boolean char2boolean(char val)
{
	if(val == '0')
		return false;
	else
		return true;
}
}
