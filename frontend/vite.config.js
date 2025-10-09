import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [
    react()
  ],
  server: {
      host: '0.0.0.0',
      proxy: {
        '/upload': {
          target: 'http://backend:8080',
          changeOrigin: true,
          secure: false,
        }
      }
    }
})