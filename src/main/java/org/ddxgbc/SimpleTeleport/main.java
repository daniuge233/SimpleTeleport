package org.ddxgbc.SimpleTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;


public class main extends JavaPlugin {

    private int status;
    private String player;

    String LoadSuccess = getConfig().getString("language.LoadSuccess");
    String TypeIsPlayer = getConfig().getString("language.TypeIsPlayer");
    String ReadyTeleport = getConfig().getString("language.ReadyTeleport");
    String RandomSuccess = getConfig().getString("language.RandomSuccess");
    String SendSlaveRequestComplete = getConfig().getString("language.SendSlaveRequestComplete");
    String TPtoYou = getConfig().getString("language.TPtoYou");
    String YouTPto = getConfig().getString("language.YouTPto");
    String TypeIn = getConfig().getString("language.TypeIn");
    String Accept = getConfig().getString("language.Accept");
    String Refuse = getConfig().getString("language.Refuse");
    String NotOnline = getConfig().getString("language.NotOnline");
    String ParameterError = getConfig().getString("language.ParameterError");
    String OAcceptRequest = getConfig().getString("language.OAcceptRequest");
    String AcceptRequest = getConfig().getString("language.AcceptRequest");
    String ORejectRequest = getConfig().getString("language.ORejectRequest");
    String RejectRequest = getConfig().getString("language.RejectRequest");
    String NoRequest = getConfig().getString("language.NoRequest");

    @Override
    public void onEnable() {

        //语言配置文件config.yml | language
        saveDefaultConfig();

        //注册命令
        Objects.requireNonNull(Bukkit.getPluginCommand("rt")).setExecutor(new main());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerNameBan(), this);
        System.out.println(LoadSuccess);
    }


    @Override
    //指令方法
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 玩家判断方法
        if (!(sender instanceof Player) && sender != null) {
            sender.sendMessage(TypeIsPlayer);
            return true;

        }else{
            if (cmd.getName().equalsIgnoreCase("rt")) { //随机传送
                //随机传送
                try {
                    sender.sendMessage(ReadyTeleport);
                    Thread.sleep(3000);
                    Random random = new Random();   // 随机数
                    Player p = (Player) sender;
                    Location location = p.getLocation();
                    int playerY = (int) location.getY();
                    int x = Math.abs(random.nextInt(100 - (-100) + 1) * 15);
                    int z = Math.abs(random.nextInt(150 - (-120) + 1) * 15);
                    Location teleportto = new Location(location.getWorld(), x, playerY, z);
                    // System.out.println(location);
                    boolean tp = p.teleport(teleportto);
                    sender.sendMessage(RandomSuccess + "§c" + x + "," + playerY + "," + z + "§a!");
                    return true;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }

            }
            //定义tpp命令(发送传送请求)
            if (cmd.getName().equalsIgnoreCase("tpp")) {
                //如果<传送对象>不为空
                if (!args[0].equalsIgnoreCase("a") && !args[0].equalsIgnoreCase("r") && !args[0].equalsIgnoreCase("h")) {
                    //获取对象
                    Player p = Bukkit.getPlayer(args[0]);
                    // Player = p.getName();
                    player = sender.getName();
                    //如果对方在线
                    if (p != null) {
                        //发送请求
                        status = 1;
                        sender.sendMessage(SendSlaveRequestComplete);
                        p.sendMessage("§b--------------------\n§d" + sender.getName() + TPtoYou+"\n"+TypeIn+"§c/tpp a" + Accept+"\n"+TypeIn+"§c/tpp r" +Refuse+"\n"+"§b--------------------");
                    } else {
                        //不在线
                        sender.sendMessage(NotOnline);
                    }
                    return true;
                }

                // 邀请方法
                if (args[0].equalsIgnoreCase("h")) {
                    if (args[1] != null) {   //若不为空
                        Player player1 = Bukkit.getPlayer(args[1]);
                        if (player1 == null) {    //判断是否存在该玩家
                            sender.sendMessage(ParameterError);
                            return true;
                        } else {
                            player = sender.getName();
                            status = 2;
                            player1.sendMessage("§b--------------------\n" + sender.getName() + YouTPto+"\n"+TypeIn+"§c/tpp a"+Accept+"\n"+TypeIn+"§c/tpp r"+Refuse+"\n"+"§b--------------------");
                            return true;
                        }
                    }
                }

                //接受方法
                if (args[0].equalsIgnoreCase("a")) {
                    //--------------------------------
                    //--------------------------------
                    if (status == 1) {      //请求
                        Player player1 = Bukkit.getPlayer(player);
                        if (sender.getName() == player){
                            if (player1 != null) {
                                try {
                                    player1.sendMessage(OAcceptRequest);
                                    sender.sendMessage(AcceptRequest);
                                    Thread.sleep(3000);
                                    player1.teleport((Player) sender);

                                    status = 0;
                                    return true;

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    return false;
                                }

                            }
                        }

                    } else if (status == 2) {     //邀请
                        Player p = Bukkit.getPlayer(player);

                        try {
                            p.sendMessage(OAcceptRequest);
                            sender.sendMessage(AcceptRequest);
                            Thread.sleep(3000);
                            ((Player) sender).teleport(p);

                            status = 0;
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }


                    } else {
                        sender.sendMessage(NoRequest);
                        return true;
                    }
                }

                //拒绝方法
                if (args[0].equalsIgnoreCase("r")) {
                    //--------------------------------
                    //--------------------------------
                    if (status == 1) {
                        Player player1 = Bukkit.getPlayer(player);
                        if (sender.getName() == player){
                            if (player1 != null) {
                                sender.sendMessage(RejectRequest);
                                player1.sendMessage(ORejectRequest);
                                status = 0;
                                return true;
                            }
                        }

                    } else if (status == 2) {
                        Player p = Bukkit.getPlayer(player);
                        sender.sendMessage(RejectRequest);
                        p.sendMessage(ORejectRequest);
                        status = 0;
                        return true;

                    } else {
                        sender.sendMessage(NoRequest);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}