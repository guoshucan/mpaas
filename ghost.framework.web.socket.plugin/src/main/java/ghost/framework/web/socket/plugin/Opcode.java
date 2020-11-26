package ghost.framework.web.socket.plugin;

/**
 * Enum which contains the different valid opcodes
 */
public enum Opcode {
    CONTINUOUS, TEXT, BINARY, PING, PONG, CLOSING
    // more to come
}