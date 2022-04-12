package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {

    public static void main(String[] args) {
        final ChatServer server = new ChatServer();
        server.startServer();

    }
    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(9090)) {   //если в скобках записать то можно без Finally)
            System.out.println("Сервер запущен, ожидаем коннекта....");
            Socket socket = serverSocket.accept(); //блокирующий метод ждем подключения клиента
            System.out.println("Клиент подключился");
            final DataInputStream in = new DataInputStream(socket.getInputStream());// получение сообщений
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //отправление сообщений

            Thread t1 = new Thread(){

                @Override
                public void run() {
                    System.out.println("1111111");
                    try {
                        Scanner scanner = new Scanner(System.in);
                        while (true) {
                            String outputMessage = scanner.nextLine();
                            out.writeUTF(outputMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            };
            t1.start();

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
