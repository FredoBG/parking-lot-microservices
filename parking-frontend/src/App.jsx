import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import LoginGuard from './components/LoginGuard';
import HomePage from './pages/HomePage';
import CheckInPage from './pages/CheckInPage';
import CheckOutPage from './pages/CheckOutPage';
import HistoryPage from './pages/HistoryPage';
import TestPage from './pages/TestPage';

function App() {
  return (
    <Router>
      {/* The Guard ensures we are logged in BEFORE rendering the sidebar or pages */}
      <LoginGuard>
        <div className="flex h-screen bg-gray-50">
          <nav className="w-64 bg-slate-900 text-white p-6 space-y-4">
            <div className="text-xl font-bold pb-4 border-b border-slate-700">Parking System</div>
            <ul className="space-y-4">
              <li><Link to="/" className="hover:text-indigo-300 block">🏠 Dashboard</Link></li>
              <li><Link to="/history" className="hover:text-indigo-300 block">📜 History</Link></li>
              <li><Link to="/check-in" className="hover:text-indigo-300 block">🚗 Check-In</Link></li>
              <li><Link to="/check-out" className="hover:text-indigo-300 block">📤 Check-Out</Link></li>
              <li><Link to="/test" className="hover:text-indigo-300 block text-yellow-400">⚡ TEST ENDPOINT</Link></li>
            </ul>
          </nav>

          <main className="flex-1 p-10 overflow-auto">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/check-in" element={<CheckInPage />} />
              <Route path="/check-out" element={<CheckOutPage />} />
              <Route path="/history" element={<HistoryPage />} />
              <Route path="/test" element={<TestPage />} />
            </Routes>
          </main>
        </div>
      </LoginGuard>
    </Router>
  );
}

export default App;