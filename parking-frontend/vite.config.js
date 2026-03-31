import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8083',
      '/auth': 'http://localhost:8083',
      '/oauth2': 'http://localhost:8083',
      '/login': 'http://localhost:8083'
    }
  }
})