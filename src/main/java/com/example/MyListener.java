package com.example;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MyListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String message = event.getMessage().getContentRaw();
            //if you type "!hello" the bot will say hi to you
            if (message.equalsIgnoreCase("!hello")) {
                event.getChannel().sendMessage("Hello, " + event.getAuthor().getName() + "!").queue();
            //if you type "!generate" it will generate but takes a while
            } else if (message.equalsIgnoreCase("!generate")) {
                // Call your method to generate the string
                String generatedString = YourStringGenerator.generateString();

                // Send the generated string to the Discord channel
                event.getChannel().sendMessage("Generated String: " + generatedString).queue();
            }
        }
       
    }
}