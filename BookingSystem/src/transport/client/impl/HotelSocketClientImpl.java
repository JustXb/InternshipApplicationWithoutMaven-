package transport.client.impl;

import transport.client.SocketClient;
import transport.dto.request.RequestDto;
import transport.dto.response.ResponseDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HotelSocketClientImpl  {
    private static final String host = "localhost";
    private static final int port = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;




}
