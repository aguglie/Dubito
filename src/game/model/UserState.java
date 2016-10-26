package game.model;

/**
 * Theese are all states in which the client could be
 */
public enum UserState {
    LOGIN,//User is on login page (no username entered yet)
    LOBBY,//User is on lobby page, no game match choosen yet
    WAITING_START;//User is on game page but it has not started yet.
}
