package com.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
// Showcase Project: Discord and Twitterbot
//Lizzy Mackenroth
//THIS IS THE MAIN CLASS FOR THE DISCORD BOT, RUN THIS FOR DISCORD BOT; if you want to see the discod bot, install it on discord with:
// uses command !generate on discord after running the code

public class DiscordBot{

    public static void main(String[] args) throws Exception {


        //authenticates the bot
        JDABuilder builder = JDABuilder.createDefault("MTE3OTgyOTQ5NDQ2NzUyNjY2Nw.G17Kur.I4p65MGkHspy60NRLTTRAGXxRJkoBJC21FeIBY");
        //seats activity
        builder.setActivity(Activity.playing("yee"));
        //sets gateway
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        //builds
        JDA jda = builder.build();
        
       
       //calls My listener
        jda.addEventListener(new MyListener());
         

        // Build and start the bot
        
       // System.out.println("Made it");
        
    }
    

    
}

