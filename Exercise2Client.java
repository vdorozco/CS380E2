
import java.io.*;
import java.util.zip.CRC32;
import java.net.Socket;


public class Exercise2Client {
	
	public static void main(String[] args) throws IOException{
		try(Socket s = new Socket("cs380.codebank.xyz",38102)){
			System.out.println("Connected to server.");
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			int[] array = new int[100];
			byte[] bArray = new byte[100];
			System.out.println("Received byte:");
			for(int i = 0; i < array.length; i++){
				int firstB = is.read();
				int secondB = is.read();
				array[i] = (firstB<<4)|secondB;
				if(i%10==0)
					System.out.print("\t");


				System.out.printf("%02X", array[i]);
				if((i+1)%10==0)
					System.out.println();
				bArray[i] = (byte)array[i];
			}
			CRC32 crc = new CRC32();
			crc.update(bArray);
			long code = crc.getValue();
			System.out.printf("Generated CRC32: %08X.\n", code);
			byte[] outArray = new byte[4];
			for(int i = outArray.length-1; i >= 0; i--){
				long ones = 255;
				outArray[i] = (byte)(code&ones);
				code>>=8;
			}
			os.write(outArray);
			if(is.read()==1)
				System.out.println("Response good.");
		}
		System.out.println("Disconnected from server.");
	}			
			

}
