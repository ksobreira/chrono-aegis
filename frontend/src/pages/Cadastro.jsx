import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registrar } from '../services/authService';
import './Cadastro.css';

export default function Cadastro() {
  const [form, setForm] = useState({ nomeJogador: '', email: '', senha: '' });
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro(''); setSucesso('');
    setLoading(true);
    try {
      await registrar(form);
      setSucesso('Conta criada! Redirecionando...');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setErro(err.response?.data || 'Erro ao criar conta.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="cadastro-root">
      <div className="cadastro-card">
        <div className="cadastro-logo">
          <span className="cadastro-logo-icon">✦</span>
          <div className="cadastro-logo-title">NOVO HERÓI</div>
          <div className="cadastro-logo-sub">► Junte-se ao reino ◄</div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="pix-field">
            <label className="pix-label">NOME DO HERÓI</label>
            <input className="pix-input" type="text" name="nomeJogador"
              value={form.nomeJogador} onChange={handleChange}
              placeholder="Seu nome de aventureiro" required />
          </div>
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
          {sucesso && <div className="pix-sucesso">{sucesso}</div>}

          <button className="pix-btn pix-btn-full" type="submit" disabled={loading}
            style={{ marginTop: 8 }}>
            {loading ? 'CRIANDO...' : '► REGISTRAR'}
          </button>
        </form>

        <div className="cadastro-footer">
          Já tem conta? <Link to="/login">ENTRAR</Link>
        </div>
      </div>
    </div>
  );
}