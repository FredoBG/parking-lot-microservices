import React, { useState } from 'react';
import { checkOut } from '../services/api';

const CheckOutPage = () => {
    const [ticketId, setTicketId] = useState('');
    const [status, setStatus] = useState({ type: '', message: '' });
    const [loading, setLoading] = useState(false);
    const [resultData, setResultData] = useState(null);

    const handleCheckOut = async (e) => {
        e.preventDefault();
        setLoading(true);
        setStatus({ type: '', message: '' });
        setResultData(null);

        try {
            const response = await checkOut(ticketId);
            setResultData(response.data);
            setStatus({ type: 'success', message: "Check-out completed!" });
            setTicketId('');
        } catch (err) {
            setStatus({ type: 'error', message: err.response?.data?.message || "Communication error." });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-md mx-auto p-8 bg-white shadow-xl rounded-2xl border border-gray-100">
            <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">📤 Vehicle Check-Out</h2>
            <form onSubmit={handleCheckOut} className="space-y-5">
                <input
                    className="w-full border border-gray-300 p-3 rounded-lg font-mono text-sm"
                    placeholder="Enter Ticket UUID"
                    value={ticketId}
                    onChange={(e) => setTicketId(e.target.value.trim())}
                    required
                    disabled={loading}
                />
                <button type="submit" disabled={loading} className="w-full py-3 bg-red-600 hover:bg-red-700 rounded-lg font-bold text-white transition-all">
                    {loading ? 'Processing...' : 'Calculate Fee & Exit'}
                </button>
            </form>

            {status.type === 'error' && <div className="mt-6 p-4 bg-red-50 text-red-700 rounded-lg">{status.message}</div>}

            {resultData && (
                <div className="mt-6 p-6 rounded-xl border bg-green-50 border-green-200 text-green-900">
                    <h3 className="font-bold border-b border-green-200 pb-2 mb-4">✅ Exit Confirmed</h3>
                    <div className="space-y-2 text-sm">
                        <div className="flex justify-between"><span>Plate:</span><span className="font-bold">{resultData.licensePlate}</span></div>
                        <div className="flex justify-between"><span>Total Fee:</span><span className="text-lg font-extrabold text-green-600">${resultData.totalFee?.toFixed(2)}</span></div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CheckOutPage;