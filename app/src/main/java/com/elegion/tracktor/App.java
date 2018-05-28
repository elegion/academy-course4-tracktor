package com.elegion.tracktor;

import android.app.Application;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.registries.FactoryRegistryLocator;
import toothpick.registries.MemberInjectorRegistryLocator;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    private static Scope sAppScope;

    @Override
    public void onCreate() {
        super.onCreate();

        Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
        //will generate after one @Inject
        //MemberInjectorRegistryLocator.setRootRegistry(new com.elegion.tracktor.MemberInjectorRegistry());
        //FactoryRegistryLocator.setRootRegistry(new com.elegion.tracktor.FactoryRegistry());

        //sAppScope = Toothpick.openScope(App.class);
        //sAppScope.installModules(new SmoothieApplicationModule(this), new NetworkModule(), new AppModule(this));
    }

    public static Scope getAppScope() {
        return sAppScope;
    }
}
