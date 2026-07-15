package org.maplibre.maplibregl;

/**
 * Registry for the app-supplied {@link CustomLayerHostProvider}.
 *
 * <p>Register a provider once at startup (e.g. from your Activity's
 * {@code configureFlutterEngine}) before any custom layer is added:
 *
 * <pre>{@code
 * MapLibreCustomLayers.registerHostProvider(new MyCustomLayerHostProvider());
 * }</pre>
 */
public final class MapLibreCustomLayers {

  private static volatile CustomLayerHostProvider hostProvider;

  private MapLibreCustomLayers() {}

  public static void registerHostProvider(CustomLayerHostProvider provider) {
    hostProvider = provider;
  }

  public static CustomLayerHostProvider getHostProvider() {
    return hostProvider;
  }
}
