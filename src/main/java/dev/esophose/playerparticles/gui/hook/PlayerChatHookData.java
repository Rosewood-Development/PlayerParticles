package dev.esophose.playerparticles.gui.hook;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerChatHookData {
    
    private UUID playerUUID;
    private int maxHookLength;
    private int hookLength;
    private Consumer<String> hookCallback;
    
    public PlayerChatHookData(UUID playerUUID, int hookLength, Consumer<String> hookCallback) {
        this.playerUUID = playerUUID;
        this.maxHookLength = hookLength;
        this.hookLength = hookLength;
        this.hookCallback = hookCallback;
    }
    
    /**
     * Gets the owning player of this hook
     * 
     * @return The player's UUID
     */
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    /**
     * Decrements the time remaining on this hook by 1 second
     */
    public void decrementHookLength() {
        this.hookLength--;
    }
    
    /**
     * Checks if this hook has timed out
     * 
     * @return If this hook has timed out
     */
    public boolean timedOut() {
        return this.hookLength <= 0;
    }

    /**
     * Gets the max length of the hook
     *
     * @return The max length of the hook
     */
    public int getMaxHookLength() {
        return this.maxHookLength;
    }
    
    /**
     * Gets how much time is remaining on the hook
     * 
     * @return The amount of time remaining on the hook
     */
    public int getTimeRemaining() {
        return this.hookLength;
    }
    
    /**
     * Executes the callback function
     * 
     * @param textEntered The text that was entered by the player
     */
    public void triggerCallback(String textEntered) {
        this.hookCallback.accept(textEntered);
    }

}
