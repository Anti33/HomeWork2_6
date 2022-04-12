package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
    private static boolean exitLoop;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(9090)) {   //если в скобках записать то можно без Finally - не закрывать)
            System.out.println("Сервер запущен, ожидаем коннекта....");
            Socket socket = serverSocket.accept(); //блокирующий метод ждем подключения клиента
            System.out.println("Клиент подключился");
            final DataInputStream in = new DataInputStream(socket.getInputStream());// получение сообщений
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //отправление сообщений

            final Thread t1 = new Thread(){


                @Override
                public void run() {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        while (!exitLoop) {
                            String outputMessage = scanner.nextLine();
                            out.writeUTF(outputMessage);

                            if("/end".equalsIgnoreCase(outputMessage)){
                                out.writeUTF("/end");
                                break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            };
            t1.start();


            while (true){
                String inputMessage  = in.readUTF(); // блокирующий метод, ждем сообщения /   .readUTF() - прочтет строку а не байты
                if("/end".equalsIgnoreCase(inputMessage)){
                    out.writeUTF("/end");
                    exitLoop = true;
                    break;

                }
                System.out.println("От клиента: "+inputMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
