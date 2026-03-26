import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
//import api from './services/api';
import HomePage from './pages/HomePage';
import CheckInPage from './pages/CheckInPage';
import HistoryPage from './pages/HistoryPage';
import TestPage from './pages/TestPage';

// We create a small helper component to handle the redirect logic
const NavigationWrapper = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    // Check if we have a saved path from the login redirect
    const savedPath = localStorage.getItem('redirect_after_login');

    // If we are on the home page but have a saved path, go back there!
    if (savedPath && savedPath !== '/' && location.pathname === '/') {
      localStorage.removeItem('redirect_after_login'); // Clear memory
      navigate(savedPath); // Jump back to where we were (e.g. /test)
    }
  }, [navigate, location]);

  return children;
};

function App() {
  return (
    <Router>
      <NavigationWrapper>
        <div className="flex h-screen bg-gray-50">
          <nav className="w-64 bg-slate-900 text-white p-6 space-y-4">
            <div className="text-xl font-bold pb-4 border-b border-slate-700">Parking System</div>
            <ul className="space-y-4">
              <li>
                <Link to="/" className="hover:text-indigo-300 block transition">🏠 Dashboard</Link>
              </li>
              <li>
                <Link to="/history" className="hover:text-indigo-300 block transition">📜 History</Link>
              </li>
              <li>
                <Link to="/check-in" className="hover:text-indigo-300 block transition">🚗 Check-In</Link>
              </li>
              <li>
                <Link to="/test" className="hover:text-indigo-300 block transition text-yellow-400 font-bold">
                  ⚡ TEST ENDPOINT
                </Link>
              </li>
            </ul>
          </nav>
          <main className="flex-1 p-10 overflow-auto">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/check-in" element={<CheckInPage />} />
              <Route path="/history" element={<HistoryPage />} />
              <Route path="/test" element={<TestPage />} />
            </Routes>
          </main>
        </div>
      </NavigationWrapper>
    </Router>
  );
}

export default App;
