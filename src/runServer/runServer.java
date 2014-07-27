package runServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import biz.source_code.base64Coder.Base64Coder;
import Sender_Receiver.Sender;
import Sender_Receiver.ServerListener;

public class runServer {

	public static Sender sender = new Sender();
	public static ServerListener serverListener = new ServerListener();
	public static void main(String[] args) throws IOException{
		Sender sender = new Sender();
		
		serverListener.init();
		System.out.println("Server Running");
		do{
			byte[] data = serverListener.ReadData();
			
			if(data != null){
				String temp_dataString = new String(Base64Coder.decodeLines(new String(data)));
				String[] optionString = temp_dataString.split(":");
				sender.init(serverListener.getIP());
				if(optionString[0].equals("AAuth")){
					if(sender.isInterrupted()){
						sender.init(serverListener.getIP());
					}else {
						sender.close();
						sender.init(serverListener.getIP());
					}
					int length = Integer.parseInt(optionString[1]);
					String[] authStrings = ReceiveDatafromAndroid(length);
					Save2File("AAuth.txt",authStrings);
					boolean verified = AndroidVerification();
					System.out.println("verification result : " + verified);
					if(verified)
						sender.SendData(sender.PreparePacket(Base64Coder.encodeLines("Verified".getBytes()).getBytes()));
					else {
						sender.SendData(sender.PreparePacket(Base64Coder.encodeLines("Not Verified".getBytes()).getBytes()));
					}
				}else if(optionString[0].equals("ANAuth")){
					if(sender.isInterrupted()){
						sender.init(serverListener.getIP());
					}else {
						sender.close();
						sender.init(serverListener.getIP());
					}
					int length = Integer.parseInt(optionString[1]);
					String[] nauthStrings = ReceiveDatafromAndroid(length);
					Save2File("ANAuth.txt",nauthStrings);
				}else if(optionString[0].equals("PAuth")){
					System.out.println("enter PAuth");
					if(sender.isInterrupted()){
						sender.init(serverListener.getIP());
					}else {
						sender.close();
						sender.init(serverListener.getIP());
					}
					int length = Integer.parseInt(optionString[1]);
					String[] nauthStrings = ReceiveDatafromPC(length);
					Save2File("PAuth.txt",nauthStrings);
					boolean verified = PcVerification();
					System.out.println("verification result : " + verified);
					if(verified)
						sender.SendData(sender.PreparePacket(Base64Coder.encodeLines("Verified".getBytes()).getBytes()));
					else {
						sender.SendData(sender.PreparePacket(Base64Coder.encodeLines("Not Verified".getBytes()).getBytes()));
					}
				}else if(optionString[0].equals("PNAuth")){
					System.out.println("enter PNAuth");
					if(sender.isInterrupted()){
						sender.init(serverListener.getIP());
					}else {
						sender.close();
						sender.init(serverListener.getIP());
					}
					int length = Integer.parseInt(optionString[1]);
					String[] nauthStrings = ReceiveDatafromAndroid(length);
					Save2File("PNAuth.txt",nauthStrings);
					sender.SendData(sender.PreparePacket(Base64Coder.encodeLines("allreceived".getBytes()).getBytes()));
				}
			}
		}while(true);
	}
	public static String[] ReceiveDatafromAndroid(int length) throws IOException{
		int round = 0;
		String[] receivedStrings = new String[length];
		while(round < length){
			byte[] tempBytes = serverListener.ReadData();
			receivedStrings[round] =  new String(Base64Coder.decodeLines(new String(tempBytes)));
			System.out.println("receivedString : " + receivedStrings[round]);
			round ++;
		}
		return receivedStrings;
	}
	public static String[] ReceiveDatafromPC(int length) throws IOException{
		int round = 0;
		String[] receivedStrings = new String[length];
		while(round < length){
			byte[] tempBytes = serverListener.ReadData();
			receivedStrings[round] =  new String(Base64Coder.decodeLines(new String(tempBytes)));
			System.out.println("receivedString : " + receivedStrings[round]);
			round ++;
		}
		return receivedStrings;
	}
	public static void Save2File(String FileName,String[] Content) throws IOException{
		FileOutputStream fos = new FileOutputStream(new File(FileName));
		for(int i=0;i<Content.length;i++){
			fos.write(Base64Coder.encodeString(Content[i]).getBytes());
			fos.write("\n".getBytes());
		}
		fos.close();
	}
	public static String[] ReadFromFIle(String FileName) throws IOException{
		BufferedReader fin = new BufferedReader(new FileReader(FileName));
		String aString = null;
		ArrayList<String> tempAL = new ArrayList<String>();
		while((aString = fin.readLine())!=null){
			
			//System.out.println("READ IN :" + aString);
			tempAL.add(aString);
		}
		fin.close();
		String[] aUKS = new String[tempAL.size()];
		for(int i=0;i<tempAL.size();i++){
			aUKS[i] = tempAL.get(i);
		}
		return aUKS;
	}
	public static boolean AndroidVerification() throws IOException{
		boolean result = true;
		String[] auth1 = ReadFromFIle("AAuth.txt");
		String[] auth2 = ReadFromFIle("ANAuth.txt");
		for(int i=0;i<auth1.length;i++){
			if(!(auth1[i].equals(auth2[i]))){
				result = false;
			}
		}
		return result;
	}
	public static boolean PcVerification() throws IOException{
		boolean result = true;
		String[] auth1 = ReadFromFIle("PAuth.txt");
		String[] auth2 = ReadFromFIle("PNAuth.txt");
		for(int i=0;i<auth1.length;i++){
			if(!(auth1[i].equals(auth2[i]))){
				result = false;
			}
		}
		return result;
	}
}
