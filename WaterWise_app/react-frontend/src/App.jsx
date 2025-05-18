import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import WaterWisePage from './components/WaterWisePage';
import { LoginPage } from './components/LoginPage';
import { RegisterPage } from './components/RegisterPage';
import { FieldMappingPage } from './components/FieldMappingPage';
import FieldInfoPage from './components/FieldInfoPage';
import { TestPage } from './components/TestPage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<WaterWisePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/newfield" element={<FieldMappingPage />} />
        <Route path="/dashboard" element={<FieldInfoPage />} />
        <Route path="/test" element={<TestPage />} />
      </Routes>
    </Router>
  );
};

export default App;
