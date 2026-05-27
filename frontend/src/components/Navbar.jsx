import { Link, useNavigate, useLocation } from 'react-router-dom';
import { logout } from '../services/authService';

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const links = [
    { to: '/personagens', label: 'Personagens', icon: '⚔️' },
    { to: '/inventario', label: 'Inventário', icon: '🎒' },
    { to: '/batalha', label: 'Batalha', icon: '🔥' },
  ];

  return (
    <nav className="navbar">
      <Link to="/personagens" className="navbar-brand">
        <span className="brand-icon">☽</span>
        <span className="brand-name">Chrono Aegis</span>
      </Link>

      <div className="navbar-links">
        {links.map(({ to, label, icon }) => (
          <Link
            key={to}
            to={to}
            className={`nav-link ${location.pathname === to ? 'active' : ''}`}
          >
            <span className="nav-icon">{icon}</span>
            {label}
          </Link>
        ))}
      </div>

      <button className="btn-logout" onClick={handleLogout}>
        Sair
      </button>
    </nav>
  );
}
