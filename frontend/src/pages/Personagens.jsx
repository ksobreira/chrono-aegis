import { useState, useEffect } from 'react';
import { buscarPorUsuario, criarPersonagem, evoluirPersonagem } from '../services/personagemService';
import './Personagens.css';

const CLASSES = ['GUERREIRO', 'MAGO', 'ARQUEIRO', 'CLERIGO'];

const ARQUETIPOS_POR_CLASSE = {
  GUERREIRO: ['PALADINO', 'BERSERKER'],
  MAGO:      ['FEITICEIRO', 'BATTLEMAGE'],
  ARQUEIRO:  ['SNIPER', 'RANGER'],
  CLERIGO:   ['SACERDOTE', 'INQUISIDOR'],
};

const CLASSE_ICON = { GUERREIRO: '⚔️', MAGO: '🧙', ARQUEIRO: '🏹', CLERIGO: '✨' };

const ARQUETIPO_ICON = {
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
    setLoading(true);
    try {
      const res = await buscarPorUsuario(usuarioId);
      setPersonagens(res.data);
    } catch {
      setErro('Erro ao carregar personagens.');
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

  const xpMax = (nivel) => nivel * 100;

  return (
    <div className="pix-main">
      <div className="pix-page-header">
        <h1 className="pix-page-title">⚔️ PERSONAGENS</h1>
        <button className="pix-btn" onClick={() => setShowForm(!showForm)}>
          {showForm ? '✕ CANCELAR' : '+ NOVO'}
        </button>
      </div>

      {erro && <div className="pix-erro">{erro}</div>}

      {showForm && (
        <div className="pix-card form-card">
          <div className="pix-card-title">CRIAR HERÓI</div>
          <form onSubmit={handleCriar}>
            <div className="pix-field">
              <label className="pix-label">NOME</label>
              <input className="pix-input" type="text" value={form.nome}
                onChange={(e) => setForm({ ...form, nome: e.target.value })}
                placeholder="Nome do herói" required />
            </div>
            <div className="pix-field">
              <label className="pix-label">CLASSE</label>
              <select className="pix-select" value={form.classe}
                onChange={(e) => setForm({ ...form, classe: e.target.value })}>
                {CLASSES.map((c) => (
                  <option key={c} value={c}>{CLASSE_ICON[c]} {c}</option>
                ))}
              </select>
            </div>
            <button className="pix-btn" type="submit" disabled={criando}>
              {criando ? 'CRIANDO...' : '► CRIAR'}
            </button>
          </form>
        </div>
      )}

      {loading ? (
        <div className="pix-loading">⟳ CARREGANDO...</div>
      ) : personagens.length === 0 ? (
        <div className="pix-empty">
          <p>Nenhum herói criado ainda.</p>
          <p style={{ marginTop: 12, fontSize: 7 }}>Crie seu primeiro personagem!</p>
        </div>
      ) : (
        <div className="pix-cards-grid">
          {personagens.map((p) => {
            const xpPct = Math.min((p.xp / xpMax(p.nivel)) * 100, 100);
            const podeEvoluir = p.arquetipo === 'NENHUM' && p.nivel >= 4;

            return (
              <div key={p.id} className="perg-card">
                <div className="perg-header">
                  <span className="perg-sprite">{CLASSE_ICON[p.classeBase]}</span>
                  <div style={{ flex: 1 }}>
                    <div className="perg-nome">{p.nome}</div>
                    <span className="perg-classe">{p.classeBase}</span>
                  </div>
                  <span className="perg-nivel">NV.{p.nivel}</span>
                </div>

                <div className="perg-stats">
                  <div className="perg-stat">
                    <span className="perg-stat-label">❤️ VIDA</span>
                    <span className="perg-stat-value">{p.vidaMax}</span>
                  </div>
                  <div className="perg-stat">
                    <span className="perg-stat-label">⚔️ ATAQUE</span>
                    <span className="perg-stat-value">{p.ataque}</span>
                  </div>
                  <div className="perg-stat">
                    <span className="perg-stat-label">🛡️ DEFESA</span>
                    <span className="perg-stat-value">{p.defesa}</span>
                  </div>
                  <div className="perg-stat">
                    <span className="perg-stat-label">🏅 ARQUÉTIPO</span>
                    <span className="perg-stat-value" style={{ fontSize: 6 }}>
                      {ARQUETIPO_ICON[p.arquetipo]} {p.arquetipo}
                    </span>
                  </div>
                </div>

                <div>
                  <div className="perg-xp-row">
                    <span>XP</span>
                    <span>{p.xp} / {xpMax(p.nivel)}</span>
                  </div>
                  <div className="pix-xp-bg">
                    <div className="pix-xp-fill" style={{ width: `${xpPct}%` }} />
                  </div>
                </div>

                {podeEvoluir && (
                  evoluindo === p.id ? (
                    <div className="perg-evolucao-panel">
                      <div className="perg-evolucao-label">✦ ESCOLHA O ARQUÉTIPO:</div>
                      <div className="perg-arquetipos">
                        {ARQUETIPOS_POR_CLASSE[p.classeBase]?.map((arq) => (
                          <button key={arq}
                            className={`perg-arq-btn ${arquetipoSel === arq ? 'selected' : ''}`}
                            onClick={() => setArquetipoSel(arq)}>
                            {ARQUETIPO_ICON[arq]} {arq}
                          </button>
                        ))}
                      </div>
                      <div className="perg-evolucao-actions">
                        <button className="pix-btn-gold" onClick={() => handleEvoluir(p.id)} disabled={!arquetipoSel}>
                          ► EVOLUIR
                        </button>
                        <button className="pix-btn-ghost" onClick={() => { setEvoluindo(null); setArquetipoSel(''); }}>
                          CANCELAR
                        </button>
                      </div>
                    </div>
                  ) : (
                    <button className="perg-btn-evoluir" onClick={() => setEvoluindo(p.id)}>
                      ✦ EVOLUIR ARQUÉTIPO
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