import React, { useState } from 'react';
import { checkIn } from '../services/api';

const CheckInPage = () => {
    const [formData, setFormData] = useState({
        licensePlate: '',
        type: 'CAR' // Default matching your VehicleType Enum
    });
    const [status, setStatus] = useState({ type: '', message: '' });
    const [loading, setLoading] = useState(false); // New state for the animation

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); // Start animation
        setStatus({ type: '', message: '' });

        try {
            const response = await checkIn(formData);
            setStatus({
                type: 'success',
                message: `Ticket ${response.data.id} created successfully!`
            });
            setFormData({ licensePlate: '', type: 'CAR' });
        } catch (err) {
            setStatus({
                type: 'error',
                message: err.response?.data?.message || "Server error. Check backend logs."
            });
        } finally {
            setLoading(false); // Stop animation regardless of success/fail
        }
    };

    return (
        <div className="max-w-md mx-auto p-8 bg-white shadow-xl rounded-2xl border border-gray-100">
            <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2">
                🚗 Vehicle Check-In
            </h2>

            <form onSubmit={handleSubmit} className="space-y-5">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">License Plate</label>
                    <input
                        className="w-full border border-gray-300 p-3 rounded-lg font-mono uppercase focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                        placeholder="ABC-123"
                        value={formData.licensePlate}
                        onChange={(e) => setFormData({...formData, licensePlate: e.target.value.toUpperCase()})}
                        required
                        disabled={loading}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Vehicle Type</label>
                    <select
                        className="w-full border border-gray-300 p-3 rounded-lg bg-white focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                        value={formData.type}
                        onChange={(e) => setFormData({...formData, type: e.target.value})}
                        disabled={loading}
                    >
                        <option value="CAR">Car</option>
                        <option value="MOTORCYCLE">Motorcycle</option>
                        <option value="VAN">Van</option>
                    </select>
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full py-3 rounded-lg font-bold text-white transition-all flex justify-center items-center gap-2 ${
                        loading ? 'bg-indigo-400 cursor-wait' : 'bg-indigo-600 hover:bg-indigo-700 active:scale-95'
                    }`}
                >
                    {loading ? (
                        <>
                            <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                            </svg>
                            Processing...
                        </>
                    ) : 'Register Entry'}
                </button>
            </form>

            {status.message && (
                <div className={`mt-6 p-4 rounded-lg border text-sm font-medium animate-fade-in ${
                    status.type === 'success' ? 'bg-green-50 text-green-700 border-green-200' : 'bg-red-50 text-red-700 border-red-200'
                }`}>
                    {status.message}
                </div>
            )}
        </div>
    );
};

export default CheckInPage;