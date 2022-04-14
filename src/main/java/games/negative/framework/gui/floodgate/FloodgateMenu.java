package games.negative.framework.gui.floodgate;

import lombok.Getter;
import lombok.Setter;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

@Getter
@Setter
public class FloodgateMenu {

    private final String content;
    private boolean responded = false;
    private final String title;
    private ModalFormResponse response;
    private ModalForm form;

    /**
     * Create a new floodgate menu
     *
     * @param content The content of the menu
     * @param title The title of the menu
     * @param button1 Button 1
     * @param button2 Button 2
     */
    public FloodgateMenu(String content, String title, String button1, String button2) {
        this.content = content;
        this.title = title;
        this.form = ModalForm.builder()
                .content(content)
                .button1(button1)
                .button2(button2)
                .responseHandler((form1, responseData) -> {
                    this.response = form1.parseResponse(responseData);
                    responded = true;
        }).build();
    }

    /**
     * Shows the form to the specified player
     * @param player The player you would like to show the form to
     */
    public void show(FloodgatePlayer player) {
        player.sendForm(form);
    }

    /**
     * Check if the player has responded
     * @return true if the player has responded
     */
    public boolean hasResponded() {
        return responded;
    }

    /**
     * Get the response from the player
     * @return the response from the player
     * @throws Exception if they haven't responded yet!
     */
    public ModalFormResponse getResponse() throws Exception {
        if (response == null) {
            throw new Exception("There was no response!");
        }
        return response;
    }
    public void acknowledge() {
        responded = false;
    }
}
