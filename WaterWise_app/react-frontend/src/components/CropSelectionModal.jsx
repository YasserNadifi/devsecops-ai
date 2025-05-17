// FieldInfoModal.js
import React from 'react';
import { useState } from 'react';
import { CropType, GrowthStage } from './croptable';

const irrigationTypes = ['Surface', 'Sprinkler', 'Drip', 'Subsurface'];

const CropSelectionModal = ({ isOpen, onClose, onSubmit }) => {
  const [selectedCrop, setSelectedCrop] = useState('');
  const [selectedStage, setSelectedStage] = useState('');
  const [irrigationType, setIrrigationType] = useState('');
  const [waterFlow, setWaterFlow] = useState('');

  const handleSubmit = () => {
    if (!selectedCrop || !selectedStage || !irrigationType || !waterFlow) {
      alert('Please fill all fields');
      return;
    }

    onSubmit({
      cropType: selectedCrop,
      growthStage: selectedStage,
      irrigationType,
      waterFlow,
    });

    onClose(); // close modal after submit
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[2000] flex items-center justify-center bg-black bg-opacity-50">
      <div className="rounded-lg bg-white p-6 w-96 shadow-lg">
        <h2 className="mb-4 text-xl font-semibold">Field Information</h2>


              <label className="text-sm font-medium">
                Crop Type
                <select
                  value={selectedCrop}
                  onChange={(e) => setSelectedCrop(e.target.value)}
                  className="mt-1 w-full rounded-md border px-3 py-2 text-sm"
                >
                  <option value="" disabled>Select crop type</option>
                  {CropType.map((crop) => (
                    <option key={crop} value={crop}>
                      {crop.charAt(0).toUpperCase() + crop.slice(1)}
                    </option>
                  ))}
                </select>
              </label>

              {/* Growth Stage */}
              <label className="text-sm font-medium">
                Growth Stage
                <select
                  value={selectedStage}
                  onChange={(e) => setSelectedStage(e.target.value)}
                  className="mt-1 w-full rounded-md border px-3 py-2 text-sm"
                >
                  <option value="" disabled>Select growth stage</option>
                  {GrowthStage.map((stage) => (
                    <option key={stage} value={stage}>
                      {stage}
                    </option>
                  ))}
                </select>
              </label>

              {/* Irrigation Type */}
              <label className="text-sm font-medium">
                Irrigation Type
                <select
                  value={irrigationType}
                  onChange={(e) => setIrrigationType(e.target.value)}
                  className="mt-1 w-full rounded-md border px-3 py-2 text-sm"
                >
                  <option value="" disabled>Select irrigation type</option>
                  {irrigationTypes.map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </select>
              </label>

              {/* Water Flow */}
            <label className="block text-sm font-medium mb-1">
              Water Flow (gal/min)
              <input
                type="text"
                value={waterFlow}
                onChange={(e) => setWaterFlow(e.target.value)}
                className="mt-1 block w-full rounded-md border px-3 py-2 text-sm"
              />
            </label>

        <div className="flex justify-end gap-2">
          <button
            onClick={onClose}
            className="rounded bg-gray-300 px-4 py-2 text-sm text-gray-800 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            className="rounded bg-green-500 px-4 py-2 text-sm text-white hover:bg-green-600"
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  );
};

export default CropSelectionModal;
