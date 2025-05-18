import { WiRaindrop, WiRaindrops } from 'react-icons/wi';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { user } from '@nextui-org/react';
import axios from "axios";
import { FaTint } from 'react-icons/fa';
import animationData from '../assets/water.json';
import Lottie from 'react-lottie';


export default function FieldInfoPage() {
  const [selectedField, setSelectedField] = useState(null);
  const [wateringReq, setWateringReq] = useState(null);
  const [error, setError] = useState(null);
  const [data,setData]=useState();
  const navigate = useNavigate();
  
useEffect(() => {
  const fetchFields = async () => {
    try {
      const userId = localStorage.getItem('current_user');
      if (!userId) {
        alert("User ID not found");
        return;
      }

      const response = await axios.get(`http://localhost:8080/api/fields/user/${userId}`);
      setData(response.data);
      console.log("Fetched data:", data); // Correct place to log
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

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

  const renderIcon = (value) => {
    if (value >= 100) {
      return <WiRaindrops className="icon" size={100} color="#00ffff" />;
    } else {
      return <WiRaindrop className="icon" size={100} color="#00ffff" />;
    }
  };

  const handleAddField = () => {
    navigate('/newfield'); 
  };

  const handleSelectField = async (field) =>{
    setWateringReq(null);
    try {
      setSelectedField(field);
      console.log(field);

      const wateringReqResponse = await axios.get(`http://localhost:8080/api/irrigation/weekly-needs/${field.cropId}`);
      setWateringReq(wateringReqResponse.data);
    } catch (err) {
      alert("Error: " + err.message);
    }

  }

  return (
    <div className="flex min-h-screen bg-green-50">
      {/* Sidebar */}
<aside className="w-64 bg-white shadow-md p-6 flex flex-col justify-between">
<div>
  <h2 className="text-xl font-bold text-green-800 mb-4">Your Fields</h2>
  {data ? (
    data.map((field) => (
      <button
        key={field.id}
        onClick={() => handleSelectField(field)}
        className={`w-full text-left px-4 py-2 mb-2 rounded-md font-medium ${
          selectedField === field
            ? 'bg-green-100 text-green-800'
            : 'hover:bg-green-50 text-gray-800'
        }`}
      >
        {field.name}
      </button>
    ))
  ) : (
    <p>Loading fields...</p>
  )}
</div>

  <button
    onClick={handleAddField} // Define this function to handle the action
    className="mt-4 w-full rounded-md bg-green-500 px-4 py-2 text-white font-medium hover:bg-green-400 active:bg-green-600 transition"
  >
    + Add Field
  </button>
</aside>


      {/* Main Content */}
      <main className="flex-1 p-8">
        {!selectedField ? (
          <div className="text-center text-gray-700 text-lg">Please select a field from the sidebar.</div>
        ) :  !wateringReq ? (
                  <div className="flex flex-col justify-center items-center" >
                    <Lottie options={lottieOptions} height={300} width={300} />
                    <h4 className="text-black">We are getting your watering schedule... hang tight! </h4>
                  </div>
        ) : (
        <>
          {/* Header / Intro */}
          <div className="mb-6">
            <h2 className="text-2xl font-semibold mb-2">
              Farm Water Requirements
            </h2>
            <p className="text-gray-700">
              We have calculated your total water needs per dayfor the next 6 days.
            </p>
          </div>

          {/* Card container */}
          <div className="flex space-x-4 overflow-x-auto pb-4">
            {wateringReq.map(({ date, waterInLitres }) => (
              <div
                key={date}
                className="flex-shrink-0 w-40 bg-white rounded-2xl shadow p-4 flex flex-col items-center"
              >
                {/* Date */}
                <div className="text-sm text-gray-500 mb-3">{date}</div>

                {/* Drop icon */}
                <FaTint className="text-5xl text-cyan-400 mb-4" />

                {/* Water amount */}
                <div className="text-gray-800 mb-1">
                  <span className="font-medium">Water:</span>{' '}
                  {waterInLitres.toFixed(2)}  L
                </div>

              </div>
            ))}
          </div>
        </>
        )
        }
      </main>
    </div>
  );
}
