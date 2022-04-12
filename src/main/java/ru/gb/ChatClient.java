package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static boolean exitLoop;
    Socket socket;  // 'эти три переменные нужны на уровне класса: socket, in, out
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {/// тоже есть метод мейн

        final ChatClient client = new ChatClient();
        client.start();

    }


        private void start() {
            try {
                openConnection();
                Scanner scanner = new Scanner(System.in);
                while (!exitLoop) {
                    String outputMessage = scanner.nextLine();
                    out.writeUTF(outputMessage);  // посылка сообщений
                    if("/end".equalsIgnoreCase(outputMessage)){
                        break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    private void closeConnection() {
        if(socket != null) {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (in != null) {
            try{
                in.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if (out != null){
            try{
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void openConnection () throws IOException {
            socket = new Socket("localhost", 9090); ////либо вместо локалхост 127.0.0.1
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t1 = new Thread(){
                @Override
                public void run() {
                    try {
                        while (true) {
                            String inputMessage = in.readUTF();
                            if("/end".equalsIgnoreCase(inputMessage)){
                                out.writeUTF("/end");
                                exitLoop = true;
                                break;
                            }
                            System.out.println("от сервера: " + inputMessage);
                        }
                    }catch (IOException e){
                        //e.printStackTrace();

                    }
                    finally {
                        System.out.println("close connection..");
                        closeConnection();

                    }


                }
            };
            t1.start();


    }



}
