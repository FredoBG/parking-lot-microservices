import React, { useState } from 'react';
import { checkIn } from '../services/api';

const CheckInPage = () => {
  const [plate, setPlate] = useState('');
  const [message, setMessage] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await checkIn(plate);
      setMessage({ type: 'success', text: `Vehicle ${plate} checked in successfully!` });
      setPlate('');
    } catch (err) {
      setMessage({ type: 'error', text: 'Error during check-in. Please try again.' });
    }
  };

  return (
    <div className="max-w-md bg-white p-8 rounded-xl shadow-md border border-gray-200">
      <h2 className="text-2xl font-bold mb-6">New Check-In</h2>
      {message && (
        <div className={`mb-4 p-3 rounded ${message.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
          {message.text}
        </div>
      )}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="License Plate (e.g. ABC-123)"
          className="w-full p-2 border rounded-md"
          value={plate}
          onChange={(e) => setPlate(e.target.value.toUpperCase())}
          required
        />
        <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-md hover:bg-indigo-700">
          Confirm Entry
        </button>
      </form>
    </div>
  );
};

export default CheckInPage;
