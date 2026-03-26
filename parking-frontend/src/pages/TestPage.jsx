import React, { useEffect, useState, useRef } from 'react';
// Import the specific function instead of the default api instance
import { getTestMessage } from '../services/api';

const TestPage = () => {
    const [data, setData] = useState('Verifying connection...');
    const [loading, setLoading] = useState(true);
    const hasCalled = useRef(false);

    useEffect(() => {
        // Prevent React 18 from running this twice and confusing the BFF
        if (hasCalled.current) return;
        hasCalled.current = true;

        const fetchData = async () => {
            try {
                // Now using the centralized function from api.js
                const response = await getTestMessage();
                setData(response.data);
            } catch (err) {
                // This will trigger if bff-service or ticket-service is down
                setData("Connection failed. Check your microservices.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    return (
        <div className="p-8 bg-white rounded-xl shadow-lg border border-gray-200">
            <h2 className="text-2xl font-bold text-indigo-800 mb-6">⚡ System Connection Test</h2>
            <div className={`p-6 rounded-lg font-mono text-sm ${loading ? 'bg-gray-100' : 'bg-slate-900 text-green-400'}`}>
                {loading ? "Please wait..." : <pre>{data}</pre>}
            </div>
        </div>
    );
};

export default TestPage;