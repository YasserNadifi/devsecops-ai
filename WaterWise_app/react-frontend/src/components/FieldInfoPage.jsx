import { Card, CardBody, CardHeader } from '@nextui-org/card';
import { WiRaindrop, WiRaindrops } from 'react-icons/wi';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';


export default function FieldInfoPage() {
  const [selectedField, setSelectedField] = useState(null);
  const navigate = useNavigate();


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

  const today = new Date();
  const week = Array.from({ length: 7 }, (_, i) => {
    const date = new Date(today);
    date.setDate(today.getDate() + i);
    return date.toLocaleDateString('en-US', {
      month: '2-digit',
      day: '2-digit',
      year: 'numeric',
    });
  });

  function litersToGallons(liters) {
    const conversionFactor = 0.264172;
    const gallons = liters * conversionFactor;
    return gallons.toFixed(2);
  }

  return (
    <div className="flex min-h-screen bg-green-50">
      {/* Sidebar */}
<aside className="w-64 bg-white shadow-md p-6 flex flex-col justify-between">
  <div>
    <h2 className="text-xl font-bold text-green-800 mb-4">Your Fields</h2>
    {['Field 1', 'Field 2', 'Field 3'].map((field) => (
      <button
        key={field}
        onClick={() => setSelectedField(field)}
        className={`w-full text-left px-4 py-2 mb-2 rounded-md font-medium ${
          selectedField === field
            ? 'bg-green-100 text-green-800'
            : 'hover:bg-green-50 text-gray-800'
        }`}
      >
        {field}
      </button>
    ))}
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
        ) : 
          <div className="flex flex-col justify-center items-center">
            
            <h1 className="text-black">We are getting your watering schedule... hang tight!</h1>
          </div>
        // ) : (
        //   <>
        //     <h1 className="text-black text-xl mb-4 font-semibold">
        //       Water requirements and irrigation schedule for <span className="text-green-800">{selectedField}</span>
        //     </h1>
        //     <p className="text-black mb-2">
        //       We assume you will turn on the irrigation system at 10pm. Water units are in gallons.
        //     </p>
        //     <div className="flex flex-wrap gap-4">
        //       {result?.waterUsage.map((d, i) => (
        //         <Card className="w-64" key={d + i}>
        //           <CardHeader className="text-black ml-4">{week[i]}</CardHeader>
        //           <CardBody className="p-4 flex flex-col items-center gap-2">
        //             {renderIcon(d)}
        //             <p className="text-black">Water: {litersToGallons(d)}</p>
        //             <p className="text-black">End time: {result?.timeToEnd[i]}</p>
        //           </CardBody>
        //         </Card>
        //       ))}
        //     </div>
        //   </>
        // )
        }
      </main>
    </div>
  );
}
