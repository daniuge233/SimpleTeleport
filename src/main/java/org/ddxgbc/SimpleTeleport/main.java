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

    @Override
    public void onEnable() {
        //注册命令
        Objects.requireNonNull(Bukkit.getPluginCommand("rt")).setExecutor(new main());
        System.out.println("§b[SimpleTeleport] §d插件成功加载！");
    }

    @Override
    //指令方法
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rt")) { //随机传送
            //判断必须为玩家所发出
            if (!(sender instanceof Player) && sender != null) {
//              Player p = (Player) sender;
                sender.sendMessage("§c您必须是一个§a玩家§c!");
                return true;
                //如果是玩家
            } else {
                //随机传送
                try {
                    sender.sendMessage("§b已经准备好,三秒后开始传送！");
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
                    sender.sendMessage("§a您已被传送至" + "§c" + x + "," + playerY + "," + z + "§a!");
                    return true;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        //定义tpp命令(发送传送请求)
        if (cmd.getName().equalsIgnoreCase("tpp")) {
            //如果<传送对象>不为空
            if (!args[0].equalsIgnoreCase("a") && !args[0].equalsIgnoreCase("r") && !args[0].equalsIgnoreCase("h")) {
                //获取对象
                Player p = Bukkit.getPlayer(args[0]);
                // Player = p.getName();
                status = 1;
                player = sender.getName();
                //如果对方在线
                if (p != null) {
                    //发送请求
                    sender.sendMessage("§b已发送请求！");
                    p.sendMessage("§b--------------------\n§d" + sender.getName() + "§a想要来你这里看看\n§b输入§c/tpp a" + "§b接受\n输入§c/tpp r" + "§b拒绝。\n§b--------------------");
                } else {
                    //不在线
                    sender.sendMessage("§b您选择的玩家不在线！");
                }
                return true;
            }

            // 邀请方法
            if (args[0].equalsIgnoreCase("h")){
                if (args[1] != null){   //若不为空
                    Player player1 = Bukkit.getPlayer(args[1]);
                    if (player1 == null){    //判断是否存在该玩家
                        sender.sendMessage("§b你给的玩家参数好像不存在或不在线");
                        return true;
                    }else{
                        player = sender.getName();
                        status = 2;
                        player1.sendMessage("§b--------------------\n"+sender.getName() + "想要你到他那里\n§b输入§c/tpp a§b接受\n输入§c/tpp r§b拒绝。\n§b--------------------");
                    }
                }
            }

            //接受方法
            if (args[0].equalsIgnoreCase("a")) {
                //--------------------------------
                //--------------------------------
                if (status == 1) {      //请求
                    Player player1 = Bukkit.getPlayer(player);
                    if (player1 != null) {
                        try {
                            player1.sendMessage("§b对方接受了你的请求，三秒后开始传送~");
                            sender.sendMessage("§b接受成功，三秒后传送~");
                            Thread.sleep(3000);
                            player1.teleport((Player) sender);

                            status = 0;
                            return true;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                } else if(status == 2){     //邀请
                    Player p = Bukkit.getPlayer(player);

                    try {
                        p.sendMessage("§b对方接受了你的请求，三秒后开始传送~");
                        sender.sendMessage("§b接受成功，三秒后传送~");
                        Thread.sleep(3000);
                        ((Player) sender).teleport(p);

                        status = 0;
                        return true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } else {
                    sender.sendMessage("§b还没有任何人想找你哦~");
                    return true;
                }
            }

            //拒绝方法
            if (args[0].equalsIgnoreCase("r")) {
                //--------------------------------
                //--------------------------------
                if (status == 1) {
                    Player player1 = Bukkit.getPlayer(player);
                    if (player1 != null) {
                        sender.sendMessage("§b拒绝成功~");
                        player1.sendMessage("§b对方拒绝了你的请求");
                        status = 0;
                        return true;
                    }
                }else if (status == 2){
                    Player p = Bukkit.getPlayer(player);
                    sender.sendMessage("§b拒绝成功~");
                    p.sendMessage("§b对方拒绝了你的请求");
                    status = 0;
                    return true;

                } else {
                    sender.sendMessage("§b还没有任何人想找你哦~");
                    return true;
                }
            }
        }
        return false;
    }
}