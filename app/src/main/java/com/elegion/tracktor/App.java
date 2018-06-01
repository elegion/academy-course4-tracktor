package com.elegion.tracktor;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
        //will generate after one @Inject
        //MemberInjectorRegistryLocator.setRootRegistry(new com.elegion.tracktor.MemberInjectorRegistry());
        //FactoryRegistryLocator.setRootRegistry(new com.elegion.tracktor.FactoryRegistry());

        //Scope scope = Toothpick.openScope(this);
        //scope.installModules(new SmoothieApplicationModule(this));
    }
}
