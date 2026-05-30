import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../services/authService';
import './Login.css';

export default function Login() {
  const [form, setForm] = useState({ email: '', senha: '' });
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');
    setLoading(true);
    try {
      await login(form);
      navigate('/personagens');
    } catch (err) {
      setErro(err.response?.data || 'Credenciais inválidas.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-root">
      <div className="login-card">
        <div className="login-logo">
          <span className="login-logo-icon">☽</span>
          <div className="login-logo-title">CHRONO AEGIS</div>
          <div className="login-logo-sub">► Entre no reino ◄</div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="pix-field">
            <label className="pix-label">E-MAIL</label>
            <input className="pix-input" type="email" name="email"
              value={form.email} onChange={handleChange}
              placeholder="seu@email.com" required />
          </div>
          <div className="pix-field">
            <label className="pix-label">SENHA</label>
            <input className="pix-input" type="password" name="senha"
              value={form.senha} onChange={handleChange}
              placeholder="••••••••" required />
          </div>

          {erro && <div className="pix-erro">{erro}</div>}

          <button className="pix-btn pix-btn-full" type="submit" disabled={loading}
            style={{ marginTop: 8 }}>
            {loading ? 'CARREGANDO...' : '► ENTRAR'}
          </button>
        </form>

        <div className="login-footer">
          Não tem conta? <Link to="/cadastro">REGISTRAR</Link>
        </div>
      </div>
    </div>
  );
}