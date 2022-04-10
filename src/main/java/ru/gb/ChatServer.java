package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public static void main(String[] args) {

    //Socket socket = null;


        try(ServerSocket serverSocket = new ServerSocket(9090)) {   //если в скобках записать то можно без Finally)
            System.out.println("Сервер запущен, ожидаем коннекта....");
            Socket socket = serverSocket.accept(); //блокирующий метод ждем подключения клиента
            System.out.println("Клиент подключился");
            DataInputStream in = new DataInputStream(socket.getInputStream());// получение сообщений
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //отправление сообщений
            while (true){
                String inputMessage  = in.readUTF(); // блокирующий метод, ждем сообщения /   .readUTF() - прочтет строку а не байты
                System.out.println("От клиента: "+inputMessage);
                if( "/end".equalsIgnoreCase(inputMessage)){    // poison pill - ядовитая таблетка при написании енд выйдет из цикла
                    out.writeUTF(inputMessage);
                        break;                                 //if( message.equalsIgnoreCase("/end") - пишем это а потом Flip через Alt+Enter
                }                                           //Защищаемся от нуля в мессадже.  equalsIgnoreCase - сравнивает без учета регистра

            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        System.out.println("Stop server....");
    }

}
