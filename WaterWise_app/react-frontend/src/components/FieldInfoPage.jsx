
import { WiRaindrop, WiRaindrops } from 'react-icons/wi';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { FaTint } from 'react-icons/fa';
import animationData from '../assets/water.json';
import Lottie from 'react-lottie';
import { WeatherOverview } from './WeatherOverview';
import FieldDetails from './FieldDetails';
import { FaTrash } from "react-icons/fa";

export default function FieldInfoPage() {
  const [selectedField, setSelectedField] = useState(null);
  const [wateringReq, setWateringReq] = useState(null);
  const [data, setData] = useState();
  const navigate = useNavigate();
  const [weather, setWeather] = useState(null);
  const [crop,setCrop]=useState(null);
  const [surface,setSurface]=useState(null);

  useEffect(() => {
    if(selectedField){
      const API_KEY="dc87fcbe174d8fdf21975befd687cc2b"
      console.log(selectedField.coordinates[0])
      const lat = selectedField.coordinates[0].latitude
      const lon = selectedField.coordinates[0].longitude
      console.log(lon)
      console.log(lat)
      axios
        .get(`https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&appid=${API_KEY}`)
        .then(res => setWeather(res.data))
        .catch(console.error);
      axios
        .get(`http://localhost:8080/api/crops/${selectedField.cropId}`)
        .then(res => setCrop(res.data))
        .catch(console.error);
        axios
        .get(`http://localhost:8080/api/fields/surface/${selectedField.cropId}`)
        .then(res => setSurface(res.data))
        .catch(console.error);
      
    }
  }, [selectedField]);

useEffect(() => {
  console.log("crop")
  if (crop) {
    console.log("crop");
    console.log(crop);
  }
}, [crop]);

  useEffect(() => {
  if (surface) {
    console.log("surface : "+surface);
  }
}, [surface]);

    const fetchFields = async () => {
      try {
        
        if(localStorage.getItem("current_user") === null){
          alert("You need to be logged in first !!");
          window.location.href = "/login";
          return;
        }
        const userId = localStorage.getItem('current_user');
        const response = await axios.get(`http://localhost:8080/api/fields/user/${userId}`);
        setData(response.data);
      } catch (err) {
        alert("Error: " + err.message);
      }
    };

  useEffect(() => {
    fetchFields();
  }, []);

  const lottieOptions = {
    loop: true,
    autoplay: true,
    animationData: animationData,
    rendererSettings: {
      preserveAspectRatio: 'xMidYMid slice',
    },
  };

  const handleAddField = () => navigate('/newfield');

  const handleSelectField = async (field) => {
    console.log(field)
    setWateringReq(null);
    try {
      setSelectedField(field);
      const wateringReqResponse = await axios.get(`http://localhost:8080/api/irrigation/weekly-needs/${field.cropId}`);
      setWateringReq(wateringReqResponse.data);
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

  const handleDeleteField=async (fieldId)=>{
    const confirmDelete = window.confirm("Are you sure you want to delete this field?");
  if (!confirmDelete) return;

    await axios.delete(`http://localhost:8080/api/fields/${fieldId}`);

  fetchFields();
  if (selectedField?.id === fieldId) {
    setSelectedField(null);
  }

  }

  const handleLogout = () => {
    localStorage.removeItem('current_user');
    navigate('/login');
  };

  return (
<div className="flex flex-col h-screen bg-green-50 font-sans">
  {/* Top Bar */}
  <header className="h-[75px] bg-white p-4 flex items-center justify-between border-b border-gray-200">
    <h1 className="text-3xl font-bold text-green-700 font-serif ml-8">WaterWise</h1>
    <button
      onClick={handleLogout}
      className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 transition"
    >
      Logout
    </button>
  </header>

      {/* Main Layout: Sidebar + Content */}
      <div className="flex flex-1 overflow-hidden">
        {/* Sidebar */}
        <aside className="w-72 h-[calc(100vh-75px)] bg-white shadow-md p-6 flex flex-col">

          {/* Fixed header section */}
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-bold text-green-800 font-serif">Your Fields</h2>
            <button
              onClick={handleAddField}
              className="text-sm bg-green-500 text-white px-3 py-1 rounded-md hover:bg-green-600 transition"
            >
              + Add
            </button>
          </div>

          {/* Scrollable list container */}
          <div className="flex-1 overflow-y-auto pr-1">
            {data ? (
      data.map((field) => (
        <div key={field.id} className="relative">
          <button
            onClick={() => {field != selectedField && handleSelectField(field)}}
            className={`w-full flex items-center justify-between px-4 py-2 mb-2 rounded-md font-medium transition ${
              selectedField?.id === field?.id
                ? 'bg-green-100 text-green-800'
                : 'hover:bg-green-50 text-gray-800'
            }`}
          >
            <span className="truncate">{field.name}</span>
            <button
              onClick={(e) => {
                e.stopPropagation(); // Prevent field selection
                handleDeleteField(field.id);
              }}
              className="text-red-500 hover:text-red-700 ml-3 w-[10]"
              title="Delete field"
            >
              <FaTrash className="text-sm" />
            </button>
          </button>
        </div>
      ))
    ) : (
              <p className="text-sm text-gray-600">Loading fields...</p>
            )}
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1 overflow-y-auto p-8 pt-10 h-[calc(100vh-75px)]">
          
          {!selectedField ? (
            <div className="text-center text-gray-700 text-lg font-medium">
              Please select a field from the sidebar.
            </div>
          ) : !wateringReq ? (
            <div className="flex flex-col justify-center items-center h-full">
              <Lottie options={lottieOptions} height={300} width={300} />
              <h4 className="text-green-800 font-medium mt-4">
                We are getting your watering schedule... hang tight!
              </h4>
            </div>
          ) : (
            <>
            <FieldDetails field={selectedField} crop ={crop} surface={surface}/>
              <div className="mb-6">
                <h2 className="text-2xl font-bold text-green-800 font-serif mb-2 mt-6">
                  Wheater Info
                </h2>
              </div>
              <WeatherOverview data={weather} />


              <div className="mb-6">
                <h2 className="text-2xl font-bold text-green-800 font-serif mb-2">
                  Farm Water Requirements
                </h2>
                <p className="text-gray-700">
                  Here’s your field’s water needs for the next 6 days.
                </p>
              </div>
              {/* Cards */}
              <div className="flex space-x-4 overflow-x-auto pb-4">
                {wateringReq.map(({ date, waterInLitres }) => (
                  <div
                    key={date}
                    className="flex-shrink-0 w-40 bg-white rounded-2xl shadow-md p-4 flex flex-col items-center"
                  >
                    <div className="text-sm text-gray-500 mb-3">{date}</div>
                    <FaTint className="text-5xl text-cyan-400 mb-4" />
                    <div className="text-gray-800 text-sm text-center">
                      <span className="font-medium">Total:</span>{' '}
                      {waterInLitres.toFixed(2)} L
                    </div>
                    <div className="text-gray-800 text-sm text-center mt-1">
                      <span className="font-medium">per m²:</span>{' '}
                      {(waterInLitres/surface).toFixed(2)} L/m²
                    </div>
                  </div>
                ))}
              </div>

              
            </>
          )}
        </main>
      </div>
    </div>
  );
}
