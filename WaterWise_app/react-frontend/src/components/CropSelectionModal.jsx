
import { useState } from 'react';
import { CropType, GrowthStage } from './croptable';
import { ExclamationCircleIcon } from '@heroicons/react/24/outline';

const irrigationTypes = ['Surface', 'Sprinkler', 'Drip', 'Subsurface'];

const CropSelectionModal = ({ isOpen, onClose, onSubmit }) => {
  const [selectedCrop, setSelectedCrop] = useState('');
  const [selectedStage, setSelectedStage] = useState('');
  const [irrigationType, setIrrigationType] = useState('');
  const [waterFlow, setWaterFlow] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = () => {
    if (!selectedCrop || !selectedStage || !irrigationType || !waterFlow) {
      setErrorMessage('Please fill all fields');
      return;
    }

    onSubmit({
      cropType: selectedCrop,
      growthStage: selectedStage,
      irrigationType,
      waterFlow,
    });

    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[2000] flex items-center justify-center bg-black bg-opacity-50 p-6 md:p-12">
      <div className="bg-white rounded-2xl shadow-md p-6 md:p-10 w-full max-w-xl">
        <h2 className="text-2xl md:text-4xl font-bold text-green-800 font-serif text-center mb-6">
          Field Information
        </h2>

        {errorMessage && (
          <div className="flex items-center mb-4 text-red-500 text-sm">
            <ExclamationCircleIcon className="h-5 w-5 mr-2" />
            <span>{errorMessage}</span>
          </div>
        )}

        <div className="space-y-6">
          <div>
            <label htmlFor="crop" className="block text-gray-900 text-xs font-medium mb-2">
              Crop Type
            </label>
            <select
              id="crop"
              value={selectedCrop}
              onChange={(e) => setSelectedCrop(e.target.value)}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] px-3 text-sm outline-2 placeholder:text-gray-500"
            >
              <option value="" disabled>
                Select crop type
              </option>
              {CropType.map((crop) => (
                <option key={crop} value={crop}>
                  {crop.charAt(0).toUpperCase() + crop.slice(1)}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="stage" className="block text-gray-900 text-xs font-medium mb-2">
              Growth Stage
            </label>
            <select
              id="stage"
              value={selectedStage}
              onChange={(e) => setSelectedStage(e.target.value)}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] px-3 text-sm outline-2 placeholder:text-gray-500"
            >
              <option value="" disabled>
                Select growth stage
              </option>
              {GrowthStage.map((stage) => (
                <option key={stage} value={stage}>
                  {stage}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="irrigation" className="block text-gray-900 text-xs font-medium mb-2">
              Irrigation Type
            </label>
            <select
              id="irrigation"
              value={irrigationType}
              onChange={(e) => setIrrigationType(e.target.value)}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] px-3 text-sm outline-2 placeholder:text-gray-500"
            >
              <option value="" disabled>
                Select irrigation type
              </option>
              {irrigationTypes.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="waterFlow" className="block text-gray-900 text-xs font-medium mb-2">
              Water Flow (gal/min)
            </label>
            <input
              id="waterFlow"
              type="text"
              value={waterFlow}
              onChange={(e) => setWaterFlow(e.target.value)}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] px-3 text-sm outline-2 placeholder:text-gray-500"
            />
          </div>
        </div>

        <div className="mt-8 flex justify-end gap-4">
          <button
            onClick={onClose}
            className="w-36x bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium py-3 rounded-lg"
          >
            Return to map
          </button>
          <button
            onClick={handleSubmit}
            className="w-32 bg-green-500 hover:bg-green-600 text-white font-medium py-3 rounded-lg disabled:opacity-50 flex items-center justify-center gap-2"
          >
            Save field
          </button>
        </div>
      </div>
    </div>
  );
};

export default CropSelectionModal;
