import React from 'react';

const HomePage = () => (
  <div className="space-y-6">
    <header>
      <h1 className="text-3xl font-bold text-gray-900">Welcome back, Fredo</h1>
      <p className="text-gray-600">Overview of the Parking Lot system status.</p>
    </header>

    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
        <p className="text-sm font-medium text-gray-500 uppercase">System Status</p>
        <p className="text-2xl font-bold text-green-600">Active</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
        <p className="text-sm font-medium text-gray-500 uppercase">Microservices</p>
        <p className="text-2xl font-bold text-indigo-600">Connected</p>
      </div>
    </div>
  </div>
);

export default HomePage;
