package server.network;

import server.controller.Lobby;
import server.controller.Room;
import server.model.User;
import utils.Logga;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Andrea on 10/18/2016.
 */
public class SocketListener implements Runnable {
    private Socket socket;
    private User user;
    private Lobby lobby;

    public SocketListener(Socket socket) {
        this.socket = socket;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            //We have just joined the game, we have report ourselves to the lobby
            lobby = Lobby.getInstance();
            lobby.addSocketListener(this);//Registers this socket to lobby room

            while (true) {
                String line = in.nextLine();

                if (line.equals("exit")) {
                    break;
                } else if (line.equals("get rooms")) {
                    out.print("Le stanze sono: ");
                    lobby.getRoomsList().forEach((room) -> out.print(room.toString() + ", "));
                    out.println();
                    out.flush();
                } else if (line.equals("add room")) {
                    lobby.createRoom("Odio");
                    out.println("Room created");
                    out.flush();
                } else if (line.equals("join room")) {
                    if (!lobby.getRoomsList().isEmpty()) {
                        Room room = (Room) lobby.getRoomsList().get(0);
                        room.join(this);
                        out.println("Now you are in " + room.toString());
                    } else {
                        out.println("No rooms in here");
                    }
                    out.flush();
                } else if (line.equals("leave room")) {
                    if (!lobby.getRoomsList().isEmpty()) {
                        Room room = (Room) lobby.getRoomsList().get(0);
                        if (room.isMyRoom(this)) {
                            room.leave(this);
                            out.println("You just left " + room.toString());
                        } else {
                            out.println("You are not in " + room.toString());
                        }
                    } else {
                        out.println("No rooms to leave");
                    }
                    out.flush();
                } else if (line.equals("info")) {
                    out.println("You are logged as " + user.toString() + ", there are " + lobby.getSocketsList().size() + " users in lobby");
                    out.flush();
                } else {
                    out.println("what does '" + line + "' mean?");
                    out.flush();
                }
            }

            try {
                lobby.getSocketsList().remove(this);
            } catch (Exception e) {
                //Amen
            }
            // chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}