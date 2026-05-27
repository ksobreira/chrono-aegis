import { useState, useEffect } from 'react';
import { buscarPorUsuario, criarPersonagem, evoluirPersonagem } from '../services/personagemService';

const CLASSES = ['GUERREIRO', 'MAGO', 'ARQUEIRO', 'CLERIGO'];

const ARQUETIPOS_POR_CLASSE = {
  GUERREIRO: ['PALADINO', 'BERSERKER'],
  MAGO: ['FEITICEIRO', 'BATTLEMAGE'],
  ARQUEIRO: ['SNIPER', 'RANGER'],
  CLERIGO: ['SACERDOTE', 'INQUISIDOR'],
};

const CLASSE_ICONS = {
  GUERREIRO: '⚔️',
  MAGO: '🔮',
  ARQUEIRO: '🏹',
  CLERIGO: '✨',
};

const ARQUETIPO_ICONS = {
  PALADINO: '🛡️', BERSERKER: '💢', FEITICEIRO: '💫', BATTLEMAGE: '⚡',
  SNIPER: '🎯', RANGER: '🌿', SACERDOTE: '🌟', INQUISIDOR: '🔥', NENHUM: '—',
};

export default function Personagens() {
  const usuarioId = localStorage.getItem('usuarioId') || 1;
  const [personagens, setPersonagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ nome: '', classe: 'GUERREIRO' });
  const [criando, setCriando] = useState(false);
  const [evoluindo, setEvoluindo] = useState(null);
  const [arquetipoSel, setArquetipoSel] = useState('');

  const carregar = async () => {
    try {
      setLoading(true);
      const res = await buscarPorUsuario(usuarioId);
      setPersonagens(res.data);
    } catch {
      setErro('Não foi possível carregar os personagens.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { carregar(); }, []);

  const handleCriar = async (e) => {
    e.preventDefault();
    setCriando(true);
    try {
      await criarPersonagem({ ...form, usuarioId: Number(usuarioId) });
      setShowForm(false);
      setForm({ nome: '', classe: 'GUERREIRO' });
      await carregar();
    } catch (err) {
      setErro(err.response?.data || 'Erro ao criar personagem.');
    } finally {
      setCriando(false);
    }
  };

  const handleEvoluir = async (id) => {
    if (!arquetipoSel) return;
    try {
      await evoluirPersonagem(id, arquetipoSel);
      setEvoluindo(null);
      setArquetipoSel('');
      await carregar();
    } catch (err) {
      setErro(err.response?.data || 'Erro ao evoluir personagem.');
    }
  };

  const xpParaProximoNivel = (nivel) => nivel * 100;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">⚔️ Personagens</h1>
        <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : '+ Novo Personagem'}
        </button>
      </div>

      {erro && <p className="form-error">{erro}</p>}

      {showForm && (
        <div className="card form-card">
          <h2 className="card-title">Criar Personagem</h2>
          <form onSubmit={handleCriar} className="auth-form">
            <div className="form-group">
              <label>Nome</label>
              <input
                type="text"
                value={form.nome}
                onChange={(e) => setForm({ ...form, nome: e.target.value })}
                placeholder="Nome do herói"
                required
              />
            </div>
            <div className="form-group">
              <label>Classe</label>
              <select
                value={form.classe}
                onChange={(e) => setForm({ ...form, classe: e.target.value })}
              >
                {CLASSES.map((c) => (
                  <option key={c} value={c}>{CLASSE_ICONS[c]} {c}</option>
                ))}
              </select>
            </div>
            <button type="submit" className="btn-primary" disabled={criando}>
              {criando ? 'Criando...' : 'Criar'}
            </button>
          </form>
        </div>
      )}

      {loading ? (
        <div className="loading-state">
          <span className="spinner">⟳</span> Carregando...
        </div>
      ) : personagens.length === 0 ? (
        <div className="empty-state">
          <p>Nenhum personagem criado ainda.</p>
          <p>Comece sua jornada criando um herói!</p>
        </div>
      ) : (
        <div className="cards-grid">
          {personagens.map((p) => {
            const xpNeeded = xpParaProximoNivel(p.nivel);
            const xpPct = Math.min((p.xp / xpNeeded) * 100, 100);
            const podeEvoluir = p.arquetipo === 'NENHUM' && p.nivel >= 5;

            return (
              <div key={p.id} className="card personagem-card">
                <div className="personagem-header">
                  <span className="classe-icon">{CLASSE_ICONS[p.classeBase]}</span>
                  <div>
                    <h2 className="personagem-nome">{p.nome}</h2>
                    <span className="personagem-classe">{p.classeBase}</span>
                  </div>
                  <span className="nivel-badge">Nv.{p.nivel}</span>
                </div>

                <div className="personagem-stats">
                  <div className="stat">
                    <span className="stat-label">❤️ Vida</span>
                    <span className="stat-value">{p.vidaMax}</span>
                  </div>
                  <div className="stat">
                    <span className="stat-label">⚔️ Ataque</span>
                    <span className="stat-value">{p.ataque}</span>
                  </div>
                  <div className="stat">
                    <span className="stat-label">🛡️ Defesa</span>
                    <span className="stat-value">{p.defesa}</span>
                  </div>
                  <div className="stat">
                    <span className="stat-label">🏅 Arquétipo</span>
                    <span className="stat-value">
                      {ARQUETIPO_ICONS[p.arquetipo]} {p.arquetipo}
                    </span>
                  </div>
                </div>

                <div className="xp-section">
                  <div className="xp-label">
                    <span>XP</span>
                    <span>{p.xp} / {xpNeeded}</span>
                  </div>
                  <div className="xp-bar">
                    <div className="xp-fill" style={{ width: `${xpPct}%` }} />
                  </div>
                </div>

                {podeEvoluir && (
                  evoluindo === p.id ? (
                    <div className="evolucao-panel">
                      <p className="evolucao-label">Escolha o arquétipo:</p>
                      <div className="arquetipo-options">
                        {ARQUETIPOS_POR_CLASSE[p.classeBase]?.map((arq) => (
                          <button
                            key={arq}
                            className={`btn-arquetipo ${arquetipoSel === arq ? 'selected' : ''}`}
                            onClick={() => setArquetipoSel(arq)}
                          >
                            {ARQUETIPO_ICONS[arq]} {arq}
                          </button>
                        ))}
                      </div>
                      <div className="evolucao-actions">
                        <button
                          className="btn-primary"
                          onClick={() => handleEvoluir(p.id)}
                          disabled={!arquetipoSel}
                        >
                          Evoluir
                        </button>
                        <button
                          className="btn-secondary"
                          onClick={() => { setEvoluindo(null); setArquetipoSel(''); }}
                        >
                          Cancelar
                        </button>
                      </div>
                    </div>
                  ) : (
                    <button
                      className="btn-evolucao"
                      onClick={() => setEvoluindo(p.id)}
                    >
                      ✦ Evoluir Arquétipo
                    </button>
                  )
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
