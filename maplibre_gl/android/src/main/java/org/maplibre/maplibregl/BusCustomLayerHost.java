package org.maplibre.maplibregl;

/**
 * JNI bridge for the native Bus Custom Layer Host.
 *
 * The actual OpenGL rendering happens in C++ (bus_custom_layer.cpp).
 * This class loads the native library and provides Java methods to
 * create/destroy the host and update bus positions.
 */
public class BusCustomLayerHost {

    /**
     * Simple data holder for a single bus, passed to native code.
     */
    public static class BusInfo {
        public double lat;
        public double lng;
        public float bearing;
        public float r, g, b;

        public BusInfo(double lat, double lng, float bearing, float r, float g, float b) {
            this.lat = lat;
            this.lng = lng;
            this.bearing = bearing;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private static boolean libraryLoaded = false;

    static {
        try {
            System.loadLibrary("bus_custom_layer");
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
        }
    }

    public static boolean isAvailable() {
        return libraryLoaded;
    }

    /**
     * Creates a native CustomLayerHost and returns its pointer as a long.
     *
     * Ownership: the returned pointer must be handed to
     * {@code new CustomLayer(id, ptr)}. MapLibre's engine then takes ownership
     * (it wraps the pointer in a std::unique_ptr) and frees the host when the
     * layer is removed. Do NOT call {@link #nativeDestroy} to free it — that
     * would double-free.
     */
    public static native long nativeCreate();

    /**
     * Updates bus positions for rendering.
     * Call this each frame before the map renders.
     *
     * @param hostPtr pointer from nativeCreate
     * @param buses   array of bus positions
     */
    public static native void nativeUpdateBuses(long hostPtr, BusInfo[] buses);

    /**
     * No-op kept for API symmetry. The engine owns the host (see
     * {@link #nativeCreate}) and frees it on layer removal, so this must not
     * delete anything. Retained only so the {@code native} declaration always
     * resolves.
     *
     * @param hostPtr pointer from nativeCreate
     */
    public static native void nativeDestroy(long hostPtr);
}
