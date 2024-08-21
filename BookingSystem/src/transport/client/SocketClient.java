package transport.client;

import transport.dto.request.RequestDto;
import transport.dto.response.ResponseDto;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface SocketClient <R extends RequestDto, V extends ResponseDto>{

    V sendRequest(R request);

    void connect(String host, int port) throws IOException;


    void sendData(String data) throws IOException;


    String receiveData() throws IOException;


    void disconnect() throws IOException;


    boolean isConnected();



}
