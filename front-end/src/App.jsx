import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Cadastro from './pages/Cadastro';
import Personagens from './pages/Personagens';
import Inventario from './pages/Inventario';
import Batalha from './pages/Batalha';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';
import './App.css';

function Layout({ children }) {
  return (
    <>
      <Navbar />
      <main className="main-content">{children}</main>
    </>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/personagens" element={<Personagens />} />
        <Route path="/inventario" element={<Inventario />} />
        <Route path="/batalha" element={<Batalha />} />
        

        <Route
          path="/personagens"
          element={
            <PrivateRoute>
              <Layout><Personagens /></Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/inventario"
          element={
            <PrivateRoute>
              <Layout><Inventario /></Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/batalha"
          element={
            <PrivateRoute>
              <Layout><Batalha /></Layout>
            </PrivateRoute>
          }
        />

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
