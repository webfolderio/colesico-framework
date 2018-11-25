package colesico.examples.ioc.multiplugin;

import colesico.framework.ioc.Polysupplier;

import java.util.Iterator;

public class MyHostBean {
    private final Polysupplier<MyPluginInterface> plugins;

    public MyHostBean(Polysupplier<MyPluginInterface> plugins) {
        this.plugins = plugins;
    }

    public void run() {
        System.out.println("Plugins:");

        if (plugins.isNotEmpty()) {
            plugins.forEach(
                    plugin -> System.out.println(plugin.getInfo()),
                    null);
        } else {
            System.out.println("no plugins");
        }
    }
}
