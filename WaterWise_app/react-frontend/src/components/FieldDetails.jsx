import { MapContainer, TileLayer, Polygon } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { useMap } from 'react-leaflet';
import { useEffect } from 'react';
import { FaSeedling, FaRulerCombined, FaChartLine } from 'react-icons/fa';
import { MdLandscape } from 'react-icons/md'


function FitBounds({ positions }) {
  const map = useMap();

  useEffect(() => {
    if (positions && positions.length > 0) {
      map.fitBounds(positions);
    }
  }, [map, positions]);

  return null;
}


export default function FieldDetails({ field ,crop, surface}) {
  if (!field) return null;
  

  
  const name = field.name;
  const latLngs = field.coordinates.map(coord => [coord.latitude, coord.longitude]);

  const cropType = crop.cropType;
  const growthStage = crop.growthStage;

  const area = surface.toFixed(2);;

  // Calculate map center as average of coords
  const center = latLngs.reduce(
    (acc, curr, i, arr) => [acc[0] + curr[0] / arr.length, acc[1] + curr[1] / arr.length],
    [0, 0]
  );

  return (
    <div className="flex gap-6">
      {/* Left meta data panel */}
      
<div className="w-[350px] bg-white p-6 rounded-2xl shadow-md flex flex-col space-y-6">
  <h2 className="text-2xl font-bold text-green-800 font-serif mb-2 mt-2 text-center">Field Info</h2>
  <div className="flex items-center space-x-4">
    <MdLandscape className="text-green-600 text-2xl" />
    <div>
      <div className="text-sm text-gray-500">Field Name</div>
      <div className="text-lg font-semibold text-gray-800">{name}</div>
    </div>
  </div>

  <div className="flex items-center space-x-4">
    <FaSeedling className="text-green-600 text-2xl" />
    <div>
      <div className="text-sm text-gray-500">Crop Type</div>
      <div className="text-lg font-semibold text-gray-800">{cropType}</div>
    </div>
  </div>

  <div className="flex items-center space-x-4">
    <FaChartLine className="text-green-600 text-2xl" />
    <div>
      <div className="text-sm text-gray-500">Growth Stage</div>
      <div className="text-lg font-semibold text-gray-800">{growthStage}</div>
    </div>
  </div>

  {area && (
    <div className="flex items-center space-x-4">
      <FaRulerCombined className="text-green-600 text-2xl" />
      <div>
        <div className="text-sm text-gray-500">Area</div>
        <div className="text-lg font-semibold text-gray-800">{area} m²</div>
      </div>
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
