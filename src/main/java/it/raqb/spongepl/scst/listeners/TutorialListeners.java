package it.raqb.spongepl.scst.listeners;

import it.raqb.spongepl.scst.SCST;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Created by Raqbit on 07-07-2017.
 */
public class TutorialListeners {

    private SCST pluginInstance;

    public TutorialListeners(SCST plugin){
        pluginInstance = plugin;
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        LuckPermsApi luckPerms = pluginInstance.luckPerms;
        Player player = event.getCause().first(Player.class).get();

        User lpUser = luckPerms.getUser(player.getUniqueId());

        System.out.println(lpUser.getPrimaryGroup());
    }
}
