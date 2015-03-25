package sha_homeServer;



public class ToggleDevice extends Device {
	
	private boolean state;
	
	public ToggleDevice(int id, String dtype, String n, String is)
	{
		super(id,dtype,n);
		if(is.equals("1"))
			state = true;
		else 
			state = false;
	}
	
	public boolean getState()
	{
		return state;
	}
	
	public char getStateS()
	{
		return state ? '1' : '0';
	}
	
	public boolean setState()
	{
		state = !state;
		return true;
	}

}
