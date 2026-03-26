import React, { useEffect, useState } from 'react';
import { getTickets } from '../services/api';

const HistoryPage = () => {
  const [tickets, setTickets] = useState([]);

  useEffect(() => {
    getTickets().then(res => setTickets(res.data)).catch(() => {});
  }, []);

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
      <table className="w-full text-left">
        <thead className="bg-gray-50 border-b">
          <tr>
            <th className="p-4 font-semibold text-gray-700">License Plate</th>
            <th className="p-4 font-semibold text-gray-700">Check-in Time</th>
          </tr>
        </thead>
        <tbody>
          {tickets.map((t, i) => (
            <tr key={i} className="border-b last:border-0 hover:bg-gray-50">
              <td className="p-4 font-mono">{t.licensePlate}</td>
              <td className="p-4 text-gray-600">{new Date(t.entryTime).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default HistoryPage;
