package com.quillphen.minecraftaiassistant.platform;

import com.quillphen.minecraftaiassistant.Constants;
import com.quillphen.minecraftaiassistant.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

// Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from one
// environment to another. In the context of MultiLoader we use this feature to access a mock API in the common code that
// is swapped out for the platform specific implementation at runtime.
public class Services {

    // In a multiloader environment, we may run into cases where a mod is loaded on multiple loaders. We can use the loader
    // name to check which loader we're running on.
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by a ServiceLoader file. You can learn more about this file at the following link:
    // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html
    // example our file on Forge points to ForgePlatformHelper while Fabric points to FabricPlatformHelper.
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
