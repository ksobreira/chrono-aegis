import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/auth': 'http://localhost:8081',
      '/personagens': 'http://localhost:8083',
      '/inventario': 'http://localhost:8084',
      '/itens': 'http://localhost:8084',
      '/batalha': 'http://localhost:8082',
    }
  }
})