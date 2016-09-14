package novaz.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for discord objects
 */
public class DisUtil {
	private static final Pattern mentionUserPattern = Pattern.compile("<@!?([0-9]{4,})>");
	private static final Pattern channelPattern = Pattern.compile("<#!?([0-9]{4,})>");
	private static final Pattern anyMention = Pattern.compile("<[@#]!?([0-9]{4,})>");

	/**
	 * Checks if the string contains a mention for a user
	 *
	 * @param input string to check for mentions
	 * @return found a mention
	 */
	public static boolean isUserMention(String input) {
		return mentionUserPattern.matcher(input).matches();
	}

	/**
	 * @param input string to check for mentions
	 * @return found a mention
	 */
	public static boolean isChannelMention(String input) {
		return channelPattern.matcher(input).matches();
	}

	/**
	 * Converts any mention to an id
	 *
	 * @param mention the mention to filter
	 * @return a stripped down version of the mention
	 */
	public static String mentionToId(String mention) {
		String id = "";
		Matcher matcher = anyMention.matcher(mention);
		if (matcher.find()) {
			id = matcher.group(1);
		}
		return id;
	}

	/**
	 * Retrieve all mentions from an input
	 *
	 * @param input text to check for mentions
	 * @return list of all found mentions
	 */
	public static List<String> getAllMentions(String input) {
		List<String> list = new ArrayList<>();
		Matcher matcher = anyMention.matcher(input);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

}