package net.savagedev.imlib.modules;

import net.savagedev.imlib.gui.IMTitleUpdater;

import java.net.URL;
import java.net.URLClassLoader;

public class IMLibModuleLoader extends URLClassLoader {
    private IMTitleUpdater titleUpdater;

    public IMLibModuleLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

        Class<?> clazz = null;
        try {
            clazz = super.loadClass("net.savagedev.imlib.gui.TitleUpdater");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Class<? extends IMTitleUpdater> titleUpdater = null;
        if (clazz != null) {
            titleUpdater = clazz.asSubclass(IMTitleUpdater.class);
        }

        try {
            if (titleUpdater != null) {
                this.titleUpdater = titleUpdater.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public IMTitleUpdater getTitleUpdater() {
        return this.titleUpdater;
    }
}
