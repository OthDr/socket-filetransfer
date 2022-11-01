import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args) throws IOException {

		int PORT = 9999;
		Socket socket = null;
		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		ServerSocket serverSocket = null;

		serverSocket = new ServerSocket(PORT);

		try {
			System.out.println("Server is Starting in Port 9999");

			socket = serverSocket.accept();
			System.out.println("Connected");

			inputStreamReader = new InputStreamReader(socket.getInputStream());
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			while (true) {

				String message = bufferedReader.readLine();

				System.out.println("Client: " + message);

				sendFile("./FilesToSend/" + message);

				bufferedWriter.write(message);
				bufferedWriter.newLine();
				bufferedWriter.flush();

				if (message.equalsIgnoreCase("close")) {
					break;
				}

			}

			socket.close();
			inputStreamReader.close();
			outputStreamWriter.close();
			bufferedReader.close();
			bufferedWriter.close();

			dataInputStream.close();
			dataOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// sendFile methode
	private static void sendFile(String path) throws Exception {
		int bytes = 0;
		File file = new File(path);
		boolean exists = file.exists();
		System.out.println(exists);
		if (exists) {

			FileInputStream fileInputStream = new FileInputStream(file);

			// send the desired file to the client
			dataOutputStream.writeLong(file.length());
			// Here we break file into chunks
			byte[] buffer = new byte[4 * 1024];
			while ((bytes = fileInputStream.read(buffer)) != -1) {
				// Send the file to Server Socket
				dataOutputStream.write(buffer, 0, bytes);
				dataOutputStream.flush();
			}
			// close IO stream
			fileInputStream.close();
		} else
			System.out.println("No such a file found");
	}

}
