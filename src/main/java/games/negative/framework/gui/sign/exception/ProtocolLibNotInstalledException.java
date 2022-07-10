package games.negative.framework.gui.sign.exception;

public class ProtocolLibNotInstalledException extends Exception {

    public ProtocolLibNotInstalledException() {
        super("Please install ProtocolLib to use the SignGUI util.");
    }

}
