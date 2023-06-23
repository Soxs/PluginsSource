package net.runelite.client.plugins.disablenotifications;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.instrument.ClassDefinition;

@Extension
@PluginDescriptor(
	name = "AutoRunEnabler",
	description = "Automatically enables run.",
	tags = {"run", "enable run", "run energy", "soxs"},
	enabledByDefault = true,
	hidden = false
)
@Slf4j
@SuppressWarnings("unused")
@Singleton
public class AutoRun extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private EventBus eventBus;




	@Override
	protected void startUp()
	{

	}

	@Override
	protected void shutDown()
	{

	}

	private void changeCode() {
		try {
			ClassPool classPool = ClassPool.getDefault();

			CtClass ctClass = classPool.get("net.runelite.client.Notifier");
			ctClass.stopPruning(true);

			if (ctClass.isFrozen()) {
				ctClass.defrost();
			}

			CtMethod[] method = ctClass.getDeclaredMethods("notify");
			for (CtMethod m : method)
				m.insertBefore("return;");

			byte[] bytecode = ctClass.toBytecode();
			ClassDefinition definition = new ClassDefinition(Class.forName("net.runelite.client.Notifier"), bytecode);

			//needs instrumentation :(
			//instrumentation.redefineClasses(definition);

		} catch (Throwable e) {
			e.printStackTrace();
			for (StackTraceElement ee : e.getStackTrace())
				System.err.println(ee.toString());
		}
	}

}
