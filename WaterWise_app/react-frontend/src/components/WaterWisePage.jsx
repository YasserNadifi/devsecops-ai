import React from 'react';
import { Link } from 'react-router-dom';


const WaterWisePage = () => {
  return (
    <main className="min-h-screen bg-green-50 flex flex-col p-6 md:p-12">
      <header className="h-20 md:h-40 bg-[#d6f5d6] rounded-lg flex items-center justify-center mb-6">
        <h1 className="text-2xl md:text-4xl font-bold text-green-800 font-serif">
          WaterWise
        </h1>
      </header>

      <section className="flex flex-col-reverse md:flex-row items-center gap-8">
        <div className="bg-white rounded-2xl shadow-md p-6 md:p-10 md:w-1/2 text-center md:text-left">
          <p className="text-lg md:text-2xl text-gray-800 leading-relaxed font-serif">
            <strong>Welcome to WaterWise.</strong> 
            <br/>
            Your smart companion for efficient and sustainable irrigation. Monitor your fields, optimize water use, and grow better with data insights.
          </p>
          <Link
            to="/login"
            className="mt-6 inline-block bg-green-200 hover:bg-green-300 text-gray-800 font-medium py-2 px-5 rounded-lg transition"
          >
            Log In
          </Link>
        </div>

        <div className="md:w-1/2">
          <img
            src="/deep-watering.jpeg"
            alt="WaterWise dashboard preview"
            className="w-full rounded-lg shadow-lg"
          />
        </div>
      </section>
    </main>
  );
};

export default WaterWisePage;
