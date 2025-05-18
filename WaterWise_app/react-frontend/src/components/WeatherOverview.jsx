// WeatherOverview.jsx
import React from 'react';

import {
  SunIcon,
  CloudIcon,
  CloudArrowDownIcon,
} from '@heroicons/react/24/outline';

import {
  Wind,
  Droplet,
} from 'lucide-react';

export function WeatherOverview({ data }) {
  if (!data) return null;

  // pull out the bits you need (metric units assumed)
  const temp = Math.round(data.main.temp);
  const description = data.weather[0].description;
  const humidity = data.main.humidity;
  const rain = data.rain?.['1h'] ?? 0;
  const wind = Math.round(data.wind.speed);

  // helper to classify high/low
  const classify = (value, thresholds) => {
    if (value >= thresholds.high) return 'High';
    if (value <= thresholds.low) return 'Low';
    return 'Moderate';
  };

  return (
<div className="flex space-x-4 overflow-x-auto pb-4 px-2">
  {/* Temperature */}
  <div className="flex-1 min-w-[120px] max-w-[250px] bg-white rounded-2xl shadow-md p-4 flex flex-col items-start">
    <div className="text-lg font-semibold text-gray-600 mb-2">Temperature</div>
    <div className="text-2xl font-bold">{temp}°C</div>
    <div className="flex items-center mt-2 text-gray-500">
      <SunIcon className="w-5 h-5 mr-1" />
      <span className="capitalize">{description}</span>
    </div>
  </div>

  {/* Humidity */}
  <div className="flex-1 min-w-[120px] max-w-[250px] bg-white rounded-2xl shadow-md p-4 flex flex-col items-start">
    <div className="text-lg font-semibold text-gray-600 mb-2">Humidity</div>
    <div className="text-2xl font-bold">{humidity}%</div>
    <div className="flex items-center mt-2 text-gray-500">
      <Droplet className="w-5 h-5 mr-1" />
      <span>{classify(humidity, { low: 30, high: 70 })}</span>
    </div>
  </div>

  {/* Precipitation */}
  <div className="flex-1 min-w-[120px] max-w-[250px] bg-white rounded-2xl shadow-md p-4 flex flex-col items-start">
    <div className="text-lg font-semibold text-gray-600 mb-2">Precipitation</div>
    <div className="text-2xl font-bold">{rain.toFixed(1)} mm</div>
    <div className="flex items-center mt-2 text-gray-500">
      <CloudArrowDownIcon className="w-5 h-5 mr-1" />
      <span>{classify(rain, { low: 0.5, high: 2 })}</span>
    </div>
  </div>

  {/* Wind Speed */}
  <div className="flex-1 min-w-[120px] max-w-[250px] bg-white rounded-2xl shadow-md p-4 flex flex-col items-start">
    <div className="text-lg font-semibold text-gray-600 mb-2">Wind Speed</div>
    <div className="text-2xl font-bold">{wind} km/h</div>
    <div className="flex items-center mt-2 text-gray-500">
      <Wind className="w-5 h-5 mr-1" />
      <span>{classify(wind, { low: 5, high: 15 })}</span>
    </div>
  </div>
</div>
  );
}
