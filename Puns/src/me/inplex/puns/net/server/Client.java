package me.inplex.puns.net.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private int x;
	private int y;

	public Client(Socket s) {
		this.socket = s;
		try {
			this.inputStream = new DataInputStream(s.getInputStream());
			this.outputStream = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.x = 0;
		this.y = 0;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}