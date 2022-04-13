package org.ddxgbc.SimpleTeleport;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerNameBan implements Listener {
    
    @EventHandler
    public void BanName(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String PlayerName = player.getName();
        if (PlayerName.equals("a") || PlayerName.equals("h") || PlayerName.equals("r")){
            player.kickPlayer(PlayerName);
        }
    }
}
