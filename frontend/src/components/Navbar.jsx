import { Link, useNavigate, useLocation } from 'react-router-dom';
import { logout } from '../services/authService';
import './Navbar.css';

const links = [
  { to: '/personagens', label: 'Personagens', icon: '⚔️' },
  { to: '/inventario',  label: 'Inventário',  icon: '🎒' },
  { to: '/batalha',     label: 'Batalha',     icon: '🔥' },
];

export default function Navbar() {
  const navigate  = useNavigate();
  const location  = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="pix-navbar">
      <Link to="/personagens" className="pix-navbar-brand">
        <span className="pix-navbar-brand-icon">☽</span>
        CHRONO AEGIS
      </Link>

      <div className="pix-navbar-links">
        {links.map(({ to, label, icon }) => (
          <Link key={to} to={to}
            className={`pix-nav-link ${location.pathname === to ? 'active' : ''}`}>
            {icon} {label}
          </Link>
        ))}
      </div>

      <button className="pix-logout" onClick={handleLogout}>SAIR</button>
    </nav>
  );
}