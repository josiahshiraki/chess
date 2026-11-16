package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        Repl repl = new Repl("http://localhost:8080");
        repl.run();
    }
}
