import React, { useState, useRef } from 'react';
import { MapContainer, TileLayer, FeatureGroup } from 'react-leaflet';
import { EditControl } from 'react-leaflet-draw';
import CursorCoordinates from "./CursorCoordinates";
import CropSelectionModal from './CropSelectionModal';
import 'leaflet/dist/leaflet.css';
import 'leaflet-draw/dist/leaflet.draw.css';

export const FieldMappingPage = () => {
  const [coordinates, setCoordinates] = useState([]);
  const [hasPolygon, setHasPolygon] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const featureGroupRef = useRef(null);

  const onCreated = (e) => {
    const layer = e.layer;
    const latlngs = layer.getLatLngs()[0].map(({ lat, lng }) => [lat, lng]);
    setCoordinates(latlngs);
    setHasPolygon(true);
  };

  const onEdited = (e) => {
    e.layers.eachLayer((layer) => {
      const latlngs = layer.getLatLngs()[0].map(({ lat, lng }) => [lat, lng]);
      setCoordinates(latlngs);
    });
  };

  const onDeleted = () => {
    setCoordinates([]);
    setHasPolygon(false);
  };

  const handleSave = () => {
    if (!coordinates.length) {
      alert("You have to draw field first!");
      return;
    }
    setShowModal(true); // open modal to input field info
  };

  const handleModalSubmit = async (info) => {
    try {
      const body = {
        boundaries: coordinates,
        cropInfo : info
      };
      console.log(body)

      const res = await fetch('https://api.example.com/fields', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });

      if (!res.ok) throw new Error('Failed to save field');
      alert("Field saved successfully!");
      setShowModal(false);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  const handleCancel = () => {
    window.history.back();
  };

  return (
    <div className="relative h-screen w-full">
      <MapContainer 
        center={[31.622, -7.989]} 
        zoom={5} 
        style={{ height: '100%', width: '100%' }}
      >
        <CursorCoordinates />
        <FeatureGroup ref={featureGroupRef}>
          <EditControl
            key={hasPolygon ? 'no-polygon' : 'polygon'}
            position="topright"
            onCreated={onCreated}
            onEdited={onEdited}
            onDeleted={onDeleted}
            draw={{
              rectangle: false,
              circle: false,
              circlemarker: false,
              marker: false,
              polyline: false,
              polygon: !hasPolygon
            }}
          />
        </FeatureGroup>
        <TileLayer url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}" />
      </MapContainer>

      <div className="absolute bottom-4 right-4 z-[1000] flex gap-2">
        <button 
          onClick={handleSave} 
          className="flex h-10 items-center rounded-lg bg-green-400 px-4 text-sm font-medium text-white transition-colors hover:bg-green-300 
          focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-green-400 active:bg-green-500"
        >
          Set crop info
        </button>
        <button 
          onClick={handleCancel}
          className="flex h-10 items-center rounded-lg bg-gray-400 px-4 text-sm font-medium text-white transition-colors hover:bg-gray-300 
          focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-400 active:bg-gray-500"
        >
          Cancel
        </button>
      </div>

      {/* Field Info Modal */}
      <CropSelectionModal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onSubmit={handleModalSubmit}
      />
    </div>
  );
};
