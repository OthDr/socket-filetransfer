import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args) {

		String HOST = "localhost";
		int PORT = 9999;
		Socket socket = null;
		File file = null;
		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {

			socket = new Socket(HOST, PORT);
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.println("Type file name you wanna download: ");
				String message = scanner.nextLine();

				bufferedWriter.write(message);
				bufferedWriter.newLine();
				bufferedWriter.flush();

				file = new File("./Downloads/" + message);

				if (file.exists()) {
					receiveFile("copy" + message);
				} else {
					receiveFile(message);

				}
				if (message.equalsIgnoreCase("close"))
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();

				if (inputStreamReader != null)
					inputStreamReader.close();

				if (outputStreamWriter != null)
					outputStreamWriter.close();

				if (bufferedReader != null)
					bufferedReader.close();

				if (bufferedWriter != null)
					bufferedWriter.close();
				if (dataInputStream != null)
					dataInputStream.close();
				if (dataOutputStream != null)
					dataOutputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// receive file method
	private static void receiveFile(String fileName) throws Exception {
		int bytes = 0;
//		File file = new File(fileName);
//		boolean exists = file.exists();
//		if (exists) {
//			System.out.println("File already exists");
//		} else {
			FileOutputStream fileOutputStream = new FileOutputStream("./Downloads/" + fileName);

			long size = dataInputStream.readLong(); // read file size
			byte[] buffer = new byte[4 * 1024];
			while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
				// Here we write the file using write method
				fileOutputStream.write(buffer, 0, bytes);
				size -= bytes; // read upto file size
			}
			// file is successfully received
			System.out.println("File is Received");
			fileOutputStream.close();
//		}
	}

}
