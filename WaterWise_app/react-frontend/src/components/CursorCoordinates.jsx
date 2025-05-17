import React, { useState } from "react";
import { useMapEvents } from "react-leaflet";

const CursorCoordinates = () => {
  const [position, setPosition] = useState(null);

  useMapEvents({
    mousemove(e) {
      setPosition(e.latlng);
    },
  });

  return position ? (
    <div
      style={{
        position: "absolute",
        bottom: 10,
        left: 10,
        backgroundColor: "white",
        padding: "6px 10px",
        borderRadius: "6px",
        fontSize: "0.875rem",
        boxShadow: "0 0 6px rgba(0,0,0,0.2)",
        zIndex: 1000,
      }}
    >
      Lat: {position.lat.toFixed(5)}, Lng: {position.lng.toFixed(5)}
    </div>
  ) : null;
};

export default CursorCoordinates;
