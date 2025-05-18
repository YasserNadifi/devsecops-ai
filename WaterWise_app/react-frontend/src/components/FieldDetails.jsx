import { MapContainer, TileLayer, Polygon } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { useMap } from 'react-leaflet';
import { useEffect } from 'react';


function FitBounds({ positions }) {
  const map = useMap();

  useEffect(() => {
    if (positions && positions.length > 0) {
      map.fitBounds(positions);
    }
  }, [map, positions]);

  return null;
}


export default function FieldDetails({ field ,crop}) {
  if (!field) return null;
  

  
  const name = field.name;
  const latLngs = field.coordinates.map(coord => [coord.latitude, coord.longitude]);

  const cropType = crop.cropType;
  const growthStage = crop.growthStage;

  const area = 5;

  // Calculate map center as average of coords
  const center = latLngs.reduce(
    (acc, curr, i, arr) => [acc[0] + curr[0] / arr.length, acc[1] + curr[1] / arr.length],
    [0, 0]
  );

  return (
    <div className="flex h-full gap-6">
      {/* Left meta data panel */}
      <div className="flex-1 bg-white p-6 rounded-2xl shadow-md flex flex-col space-y-4 max-w-md">
        <h3 className="text-xl font-semibold text-green-800 mb-2">{name}</h3>
        <div>
          <span className="font-medium text-gray-700">Crop Type: </span>
          <span>{cropType}</span>
        </div>
        <div>
          <span className="font-medium text-gray-700">Growth Stage: </span>
          <span>{growthStage}</span>
        </div>
        {area && (
          <div>
            <span className="font-medium text-gray-700">Area: </span>
            <span>{area} m²</span>
          </div>
        )}
      </div>

      {/* Right map panel */}
<div className="flex-1 rounded-2xl overflow-hidden shadow-md" style={{ minHeight: 300 }}>
  <MapContainer
    center={center} // still needed for initial render
    zoom={15}       // will be overridden by fitBounds
    scrollWheelZoom={false}
    style={{ height: '100%', width: '100%' }}
  >
    <TileLayer
      attribution='&copy; OpenStreetMap contributors'
      url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
    />
    <Polygon positions={latLngs} pathOptions={{ color: 'green', fillOpacity: 0.3 }} />
    <FitBounds positions={latLngs} />
  </MapContainer>
</div>
    </div>
  );
}
