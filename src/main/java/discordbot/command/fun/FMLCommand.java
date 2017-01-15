package discordbot.command.fun;

import discordbot.core.AbstractCommand;
import discordbot.handler.Template;
import discordbot.main.Config;
import discordbot.main.DiscordBot;
import discordbot.main.Launcher;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * !fml
 */
public class FMLCommand extends AbstractCommand {

	private static final int MIN_QUEUE_ITEMS = 40;
	private final BlockingQueue<String> items;

	public FMLCommand() {
		super();
		items = new LinkedBlockingQueue<>();
	}

	@Override
	public String getDescription() {
		return "fmylife! Returns a random entry from fmylife.com";
	}

	@Override
	public String getCommand() {
		return "fml";
	}

	@Override
	public String[] getUsage() {
		return new String[]{};
	}

	@Override
	public String[] getAliases() {
		return new String[]{};
	}

	@Override
	public String execute(DiscordBot bot, String[] args, MessageChannel channel, User author) {

		if (items.size() < MIN_QUEUE_ITEMS) {
			channel.sendTyping().queue();
			getFMLItems();
		}
		if (!items.isEmpty()) {
			try {
				String item = StringEscapeUtils.unescapeHtml4(items.take());
				if (item.length() >= 2000) {
					item = item.substring(0, 1999);
				}
				return item;
			} catch (InterruptedException e) {
				Launcher.logToDiscord(e, "fml-command", "interrupted");
			}
		}
		return Template.get("command_fml_not_today");
	}

	private void getFMLItems() {
		try {
			Document document = Jsoup.connect("http://fmylife.com/random").timeout(30_000).userAgent(Config.USER_AGENT).get();
			if (document != null) {
				Elements fmls = document.select("p.block a[href^=/article/]");
				for (Element fml : fmls) {
					items.add(fml.text().trim());
				}
			}
		} catch (IOException e) {
			Launcher.logToDiscord(e, "fml-command", "boken");
		}

	}
}