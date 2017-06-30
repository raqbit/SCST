package it.raqb.spongepl.scst;

import com.google.inject.Inject;
import it.raqb.spongepl.scst.commands.Commands;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "scst",
        name = "SCST",
        description = "Tweaks for the Security Craft Server",
        url = "http://raqb.it",
        authors = {
                "Raqbit"
        },
        version = "1.0"
)
public class SCST {

    @Inject
    private Logger logger;

    public static SCST INSTANCE;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        INSTANCE = this;
        Commands.registerCommands();
    }

    @Listener
    public void onServerClose(GameStoppedServerEvent event){
        INSTANCE = null;
    }
}
