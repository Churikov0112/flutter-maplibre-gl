part of '../maplibre_gl.dart';

/// Custom Layer Host interface for rendering OpenGL content on the map.
///
/// Implement this class to provide custom OpenGL rendering that integrates
/// with MapLibre's rendering pipeline. The [render] method is called each
/// frame and you can issue OpenGL ES calls within it.
///
/// Note: The OpenGL context is managed by MapLibre. You should not create
/// or destroy GL resources in [render]; use [initialize] and [deinitialize]
/// for resource management.
abstract class CustomLayerHost {
  /// Called once when the custom layer is first added to a map.
  ///
  /// Use this to load shaders, create buffers, and perform other one-time
  /// GL resource setup. The GL context is guaranteed to be current.
  void initialize();

  /// Called each frame to render the custom layer content.
  ///
  /// You receive the current map state via [parameters] and can issue
  /// OpenGL ES 2.0+ draw calls. The projection and model-view matrices
  /// are provided in [parameters] for transforming your geometry.
  ///
  /// This method is called on the GL thread, not the UI thread.
  void render(CustomLayerRenderParameters parameters);

  /// Called when the GL context is lost (e.g. on app backgrounding).
  ///
  /// Release any context-dependent resources here. After this call,
  /// [initialize] will be called again when the context is restored.
  void contextLost();

  /// Called when the custom layer is removed from a map or the map is
  /// destroyed. Release all GL resources here.
  void deinitialize();
}

/// Parameters passed to [CustomLayerHost.render] describing the current
/// map viewport state.
class CustomLayerRenderParameters {
  /// The viewport width in physical pixels.
  final double width;

  /// The viewport height in physical pixels.
  final double height;

  /// The current camera latitude in degrees.
  final double latitude;

  /// The current camera longitude in degrees.
  final double longitude;

  /// The current zoom level.
  final double zoom;

  /// The current bearing (rotation) in degrees clockwise from north.
  final double bearing;

  /// The current pitch (tilt) in degrees from vertical.
  final double pitch;

  /// The camera's field of view in degrees.
  final double fieldOfView;

  /// The 4x4 projection matrix as a flat list of 16 doubles (column-major).
  final List<double> projectionMatrix;

  /// The near-clipped 4x4 projection matrix (column-major).
  ///
  /// This may differ from [projectionMatrix] when terrain or other
  /// near-plane adjustments are active.
  final List<double> nearClippedProjectionMatrix;

  const CustomLayerRenderParameters({
    required this.width,
    required this.height,
    required this.latitude,
    required this.longitude,
    required this.zoom,
    required this.bearing,
    required this.pitch,
    required this.fieldOfView,
    required this.projectionMatrix,
    required this.nearClippedProjectionMatrix,
  });
}
