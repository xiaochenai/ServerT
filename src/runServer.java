

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import biz.source_code.base64Coder.Base64Coder;
import Sender_Receiver.Sender;
import Sender_Receiver.ServerListener;

public class runServer {

	public static Sender sender = new Sender();
	private static boolean AndroidVerified = false;
	public static ServerListener serverListener = new ServerListener();
	public static boolean pcVerified = false;
	private static String PCIP;
	public static void main(String[] args) throws IOException{
		
		
		System.out.println("Server Running");
		ServerSocket server = new ServerSocket(1234);
		Socket client;
		
		do{
			
			client = server.accept();
			PrintStream out = new PrintStream(client.getOutputStream());
			BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str =  buf.readLine();
		
				System.out.println("receive : " + str);
				if (str.length() == 0 || str == null)
					continue;
				
				String[] optionString = str.split(":");
				if(optionString[0].equals("AAuth")){
					String[] authStrings = ReceiveDatafromAndroid(optionString);
					Save2File("AAuth.txt",authStrings);
					boolean verified = AndroidVerification();
					System.out.println("verification result : " + verified);
					Socket PC = new Socket(PCIP,4321);
					PrintStream outPC = new PrintStream(PC.getOutputStream());

					if(verified && pcVerified == true){
						System.out.println("Verified");
						outPC.println("Verified");
						out.println("Verified");
						//deleteFile("AAuth.txt");
					}
					else {
						//deleteFile("AAuth.txt");
						out.println("Not Verified");	
						outPC.println(" Not Verified");
					}
					outPC.close();
				}else if(optionString[0].equals("ANAuth")){
					//deleteFile("ANAuth.txt");
					String[] nauthStrings = ReceiveDatafromAndroid(optionString);
					Save2File("ANAuth.txt",nauthStrings);
					out.println("allreceived");	
				}else if(optionString[0].equals("PAuth")){
					PCIP = client.getInetAddress().getHostAddress();
					String[] nauthStrings = ReceiveDatafromAndroid(optionString);
					Save2File("PAuth.txt",nauthStrings);
					boolean verified = PcVerification();
					System.out.println("verification result : " + verified);
					if(verified)
						pcVerified = true;
					else {
						pcVerified = false;
					}
					//deleteFile("PAuth.txt");
				}else if(optionString[0].equals("PNAuth")){
					//deleteFile("PNAuth.txt");
					System.out.println("PNAuth enter *************");
					String[] nauthStrings = ReceiveDatafromAndroid(optionString);
					Save2File("PNAuth.txt",nauthStrings);
					out.println("allreceived");
				}
			
		}while(true);
	}
	public static String[] ReceiveDatafromAndroid(String[] ReceivedArray) throws IOException{
		String[] receivedStrings = new String[ReceivedArray.length-1];
		System.arraycopy(ReceivedArray, 1, receivedStrings, 0, ReceivedArray.length-1);
		return receivedStrings;
	}
	public static String[] ReceiveDatafromPC(String[] ReceivedArray) throws IOException{
		String[] receivedStrings = new String[ReceivedArray.length-1];
		System.arraycopy(ReceivedArray, 1, receivedStrings, 0, ReceivedArray.length-1);
		return receivedStrings;
	}
	public static void deleteFile(String file){
		File f = new File(file);
		f.delete();
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
		File f = new File("ANAuth.txt");
		if(!f.exists())
			return result;
		String[] auth1 = ReadFromFIle("AAuth.txt");
		String[] auth2 = ReadFromFIle("AAuth.txt");
		System.out.println("auth1 length : " + auth1.length + " auth2 length : " + auth2.length);
		
		if(auth1.length != auth2.length)
			return false;
		for(int i=0;i<auth1.length;i++){
			if(!(auth1[i].equals(auth2[i]))){
				result = false;
			}
		}
		return result;
	}
	public static boolean PcVerification() throws IOException{
		boolean result = true;
		File f = new File("PNAuth.txt");
		if(!f.exists())
			return result;
		String[] auth1 = ReadFromFIle("PAuth.txt");
		String[] auth2 = ReadFromFIle("PAuth.txt");
		if(auth1.length != auth2.length)
			return false;
		for(int i=0;i<auth1.length;i++){
			if(!(auth1[i].equals(auth2[i]))){
				result = false;
			}
		}
		return result;
	}
}
