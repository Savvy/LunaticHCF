package us.timberdnd.lunatic.actions;

import java.util.HashMap;
import java.util.Map;

import us.timberdnd.lunatic.actions.action.InvSeeAction;
import us.timberdnd.lunatic.actions.action.VanishAction;

/**
 * Created by DigitalCodex on Oct 22, 2016.
 */
public class Action {

	private static Map<String, Actions> actions = new HashMap<String, Actions>();

	public static Map<String, Actions> getActions() {
		return actions;
	}
	public static void registerActions() {
		registerAction(
				new InvSeeAction("invsee"),
				new VanishAction("vanish")
				);
	}

	public static void registerAction(Actions...actionss) {
		for(Actions action: actionss) {
			actions.put(action.getName(), action);
		}
	}
}