package discordbot.event;

import discordbot.core.AbstractEventListener;
import discordbot.main.Config;
import discordbot.main.DiscordBot;
import sx.blah.discord.handle.impl.events.InviteReceivedEvent;
import sx.blah.discord.handle.impl.obj.Invite;
import sx.blah.discord.handle.obj.IInvite;

/**
 * Created on 30-8-2016
 */
public class InviteReceivedListener extends AbstractEventListener<InviteReceivedEvent> {
	public InviteReceivedListener(DiscordBot discordBot) {
		super(discordBot);
	}

	@Override
	public boolean listenerIsActivated() {
		return false;
	}

	@Override
	public void handle(InviteReceivedEvent event) {
		IInvite[] invites = event.getInvites();
		try {
			for (IInvite invite : invites) {
				Invite.InviteResponse response = invite.details();
				event.getMessage().reply(String.format("Thank you for inviting me to join the guild guild **%s**!", response.getGuildName()));
				invite.accept();
				discordBot.out.sendMessage(discordBot.client.getChannelByID(response.getChannelID()), String.format(
						"Hello all! %s invited me to join the **%s** guild. type %shelp to see what I can do.",
						event.getMessage().getAuthor().mention(),
						response.getGuildName(),
						Config.BOT_COMMAND_PREFIX));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
