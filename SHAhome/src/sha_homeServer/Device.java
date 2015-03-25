package sha_homeServer;


public abstract class Device {
	
	private String name;
	private int deviceID;
	private String type;
	
	public Device(int id, String dtype, String n)
	{
		name = n;
		type = dtype;
		deviceID = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getDeviceID()
	{
		return deviceID;
	}
	
	public String getType()
	{
		return type;
	}
	
	public boolean refresh()
	{
		return true;
	}

}
