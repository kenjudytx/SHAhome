package sha_homeServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridLayout;

//Shell of home server

public class SHAhome {
	
	private int terminate;
	private ArrayList<ToggleDevice> myDevices;
	private ArrayList<JButton> myButtons;
	private String[] sysInfo;
	
	
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		PrintWriter writer;
		Socket sock;
		
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedReader(isReader);
				System.out.println("Connection to client successful");
			} catch (Exception ex) { ex.printStackTrace(); }

		}
		
		public void run() {
			try {
				String op = reader.readLine();
				if(op.equals("systemInfo"))
				{					
					String[] systemInfo = getSystemInfo();
					for(int k = 0; k < 8; k++)
						writer.println(systemInfo[k]);
				}
				else if(op.equals("allDevices"))
				{
					String[] allDev = getAllDevices();
					int max = allDev.length;
					writer.println(""+max);
					for(int k = 0; k < max; k++)
						writer.println(allDev[k]);					
				}
				else if(op.equals("modifyDevice"))
				{
					int code = modifyDevice(Integer.parseInt(reader.readLine()));
					writer.println(code);
				}
				else if(op.equals("updateDevice"))
				{
					int code = updateDevice(Integer.parseInt(reader.readLine()));
					writer.println(code);
				}
				writer.flush();
			} catch(Exception ex) { ex.printStackTrace(); }
		}
	}

	public SHAhome()
	{
		sysInfo = new String[]{"true","true","true","false","false","true","true","false"};
		myDevices = new ArrayList<ToggleDevice>();
		myDevices.add(new ToggleDevice(342343,"Door","Front Door","1"));
		myDevices.add(new ToggleDevice(11241,"Door","Back Door","1"));
		myDevices.add(new ToggleDevice(934333,"Garage","Left Garage","1"));
		myDevices.add(new ToggleDevice(32324,"Garage","Right Garage","0"));
		myDevices.add(new ToggleDevice(324324,"Light","Kitchen","1"));
		myDevices.add(new ToggleDevice(3222324,"Light","Living Room","0"));
		myDevices.add(new ToggleDevice(3121224,"Light","Dinning Room","1"));
		myDevices.add(new ToggleDevice(39924,"Light","Front Exterior","0"));
		myDevices.add(new ToggleDevice(124324,"Light","Master Bath","1"));
		myDevices.add(new ToggleDevice(32767324,"Light","Master Bed","0"));
		myDevices.add(new ToggleDevice(3242174,"Light","Study","0"));
		myDevices.add(new ToggleDevice(477724,"Light","Guest Bedroom","1"));
		myDevices.add(new ToggleDevice(3254604,"Light","Hallway","1"));
		myDevices.add(new ToggleDevice(12004,"Faucet","Kitchen","0"));
		myDevices.add(new ToggleDevice(666004,"Faucet","Master Bath","1"));
		myDevices.add(new ToggleDevice(771004,"Faucet","Guest Bath","0"));
		myDevices.add(new ToggleDevice(3241884,"Sprinkler","Frontyard","0"));
		myDevices.add(new ToggleDevice(888004,"Sprinkler","Backyard","0"));
		myDevices.add(new ToggleDevice(999004,"Sprinkler","Program 1","1"));
		
	}
	
	public String[] getSystemInfo()
	{
		return sysInfo;
	}
	
	public String[] getAllDevices()
	{
		int num = myDevices.size();
		String[] allDevices = new String[num];
		
		for(int k=0; k < num; k++)
		{
			allDevices[k] = myDevices.get(k).getDeviceID() + "@" +
					 myDevices.get(k).getType() + "@" +
					myDevices.get(k).getName() + "@" +
					 myDevices.get(k).getStateS();
		}
		return allDevices;
	}
	
	public int modifyDevice(int dID)
	{
		for(ToggleDevice td : myDevices)
			if(td.getDeviceID() == dID)
			{
				td.setState();
				for(JButton jb : myButtons)
					if(jb.getText().equals(td.getName()))
						if(td.getState())
							jb.setBackground(Color.GREEN);
						else
							jb.setBackground(Color.RED);
				return 1;
			}
		return 0;
	}
	
	public int updateDevice(int dID)
	{
		for(ToggleDevice td : myDevices)
			if(td.getDeviceID() == dID)
			{
				if(td.getState())
					return 1;
				else
					return 0;
			}
		return 3;
	}
	
	public void createGUI()
	{
		JFrame frame = new JFrame("HomeApp");
		JPanel panel = new JPanel();
		myButtons = new ArrayList<JButton>();
		panel.setLayout(new GridLayout(5,5));
		
		for(ToggleDevice td : myDevices)
		{
			JButton temp = new JButton(td.getName());
			myButtons.add(temp);
			if(td.getState())
				temp.setBackground(Color.GREEN);
			else
				temp.setBackground(Color.RED);
			temp.setOpaque(true);
			panel.add(temp);
		}
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.pack();
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				terminate = 1;
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	 public void actionPerformed(ActionEvent e) {
        
	 }
	
	public static void main(String[] args) {
		new SHAhome().go();
	}

	
	public void go() {
		terminate = 0;
		createGUI();
		


		
		try(ServerSocket serverSock = new ServerSocket(5100)){
			
			while(true && terminate == 0) {
				Socket clientSocket = serverSock.accept();
				
		
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("Got a Connection");
			} 
		} catch(Exception ex) { ex.printStackTrace(); }
	} // end of go
}
