package org.openmaptiles.layers;

import static com.onthegomap.planetiler.TestUtils.rectangle;

import com.onthegomap.planetiler.geo.GeoUtils;
import com.onthegomap.planetiler.reader.SimpleFeature;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.openmaptiles.OpenMapTilesProfile;

class LanduseTest extends AbstractLayerTest {

  @Test
  void testNaturalEarthUrbanAreas() {
    assertFeatures(0, List.of(Map.of(
      "_layer", "landuse",
      "class", "residential",
      "_buffer", 4d
    )), process(SimpleFeature.create(
      GeoUtils.worldToLatLonCoords(rectangle(0, Math.sqrt(1))),
      Map.of("scalerank", 1.9),
      OpenMapTilesProfile.NATURAL_EARTH_SOURCE,
      "ne_50m_urban_areas",
      0
    )));
    assertFeatures(0, List.of(), process(SimpleFeature.create(
      GeoUtils.worldToLatLonCoords(rectangle(0, Math.sqrt(1))),
      Map.of("scalerank", 2.1),
      OpenMapTilesProfile.NATURAL_EARTH_SOURCE,
      "ne_50m_urban_areas",
      0
    )));
  }

  @Test
  void testOsmLanduse() {
    assertFeatures(13, List.of(
      Map.of("_layer", "poi"),
      Map.of(
        "_layer", "landuse",
        "class", "railway",
        "_minpixelsize", 4d,
        "_minzoom", 9,
        "_maxzoom", 14
      )), process(polygonFeature(Map.of(
        "landuse", "railway",
        "amenity", "school"
      ))));
    assertFeatures(13, List.of(Map.of("_layer", "poi"), Map.of(
      "_layer", "landuse",
      "class", "school",
      "_minpixelsize", 4d,
      "_minzoom", 9,
      "_maxzoom", 14
    )), process(polygonFeature(Map.of(
      "amenity", "school"
    ))));
  }

  @Test
  void testGraveYardBecomesCemetery() {
    assertFeatures(14, List.of(
      Map.of("_layer", "poi"),
      Map.of(
        "_layer", "landuse",
        "class", "cemetery"
      )), process(polygonFeature(Map.of(
        "amenity", "grave_yard"
      ))));
  }

  @Test
  void testOsmLanduseLowerZoom() {
    assertFeatures(6, List.of(Map.of(
      "_layer", "landuse",
      "class", "suburb",
      "_minzoom", 6,
      "_maxzoom", 14,
      "_minpixelsize", 1d
    )), process(polygonFeature(Map.of(
      "place", "suburb"
    ))));
    assertFeatures(7, List.of(Map.of(
      "_layer", "landuse",
      "class", "residential",
      "_minzoom", 6,
      "_maxzoom", 14,
      "_minpixelsize", 2d
    )), process(polygonFeature(Map.of(
      "landuse", "residential"
    ))));
  }
}
