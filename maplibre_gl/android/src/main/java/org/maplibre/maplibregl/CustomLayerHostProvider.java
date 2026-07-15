package org.maplibre.maplibregl;

import java.util.Map;

/**
 * App-supplied factory for native custom-layer hosts.
 *
 * <p>The plugin knows how to attach a native {@code mbgl::style::CustomLayerHost}
 * pointer to the map as a {@code CustomLayer}, but it deliberately knows nothing
 * about how such a host is created or what its data means — that is
 * application-specific. Implement this interface in your app and register it via
 * {@link MapLibreCustomLayers#registerHostProvider} to back
 * {@code MapLibreMapController.addCustomLayer} / {@code setCustomLayerData}.
 */
public interface CustomLayerHostProvider {

  /**
   * Create a native {@code CustomLayerHost} and return its raw pointer as a long
   * (return 0 to signal that no host could be created).
   *
   * <p>Ownership: the plugin hands the returned pointer to
   * {@code CustomLayer(id, ptr)}; MapLibre's engine then owns it (wraps it in a
   * {@code unique_ptr}) and frees it when the layer is removed. Do not free it
   * yourself once it has been handed over — see {@link #destroyHost}.
   *
   * @param layerId the custom-layer id
   * @param args    call arguments (e.g. {@code renderingMode})
   */
  long createHost(String layerId, Map<String, Object> args);

  /** Push new frame data to the host previously created for {@code layerId}. */
  void updateData(String layerId, long hostPtr, Map<String, Object> data);

  /**
   * Free a host that was created but never handed to the engine (e.g. the layer
   * failed to be added). This is NOT called after a successful add — the engine
   * owns the host then and freeing it here would double-free.
   */
  void destroyHost(String layerId, long hostPtr);
}
