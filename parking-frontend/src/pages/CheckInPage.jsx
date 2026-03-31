import React, { useState, useEffect } from 'react';
import { checkIn, getVehicleTypes } from '../services/api';

const CheckInPage = () => {
  const [formData, setFormData] = useState({
    plate: '', // We keep 'plate' for the input field state
    vehicleType: ''
  });
  const [vehicleTypes, setVehicleTypes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTypes = async () => {
      try {
        const response = await getVehicleTypes();
        setVehicleTypes(response.data);
        if (response.data.length > 0) {
          setFormData(prev => ({ ...prev, vehicleType: response.data[0] }));
        }
      } catch (err) {
        console.error("Could not fetch vehicle types:", err);
        setError("Failed to load vehicle types.");
      }
    };
    fetchTypes();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.plate || !formData.vehicleType) {
      alert("Please fill in all fields");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      // THIS IS THE CRITICAL PART
      // We take 'plate' from the form and rename it to 'licensePlate' for the Backend
      const requestData = {
        licensePlate: formData.plate,
        vehicleType: formData.vehicleType
      };

      console.log("SENDING TO BACKEND:", requestData);

      await checkIn(requestData);

      alert("Vehicle registered successfully!");
      setFormData({ plate: '', vehicleType: vehicleTypes[0] || '' });

    } catch (err) {
      console.error("Full Error Object:", err);
      // This shows the "License plate is mandatory" message from your @NotBlank validation
      const backendMessage = err.response?.data?.message || "Error connecting to server";
      setError(backendMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto bg-white p-8 rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 border-b pb-2">Vehicle Check-In V2</h2>

      {error && (
        <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            License Plate V2
          </label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-md outline-none transition uppercase"
            placeholder="ABC-123"
            value={formData.plate}
            onChange={(e) => setFormData({ ...formData, plate: e.target.value.toUpperCase() })}
            required
            disabled={loading}
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Vehicle Type
          </label>
          <select
            className="w-full px-4 py-2 border rounded-md outline-none transition"
            value={formData.vehicleType}
            onChange={(e) => setFormData({ ...formData, vehicleType: e.target.value })}
            required
            disabled={loading}
          >
            {vehicleTypes.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        <button
          type="submit"
          disabled={loading}
          className={`w-full py-3 px-4 rounded-md text-white font-semibold transition ${
            loading ? 'bg-gray-400' : 'bg-indigo-600 hover:bg-indigo-700'
          }`}
        >
          {loading ? 'Processing...' : 'Register Entry'}
        </button>
      </form>
    </div>
  );
};

export default CheckInPage;