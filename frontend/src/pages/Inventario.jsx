import { useState, useEffect } from 'react';
import { buscarPorUsuario } from '../services/personagemService';
import {
  buscarItensPorPersonagem,
  buscarInventario,
  listarArmas,
  equiparItem,
  deletarItem,
} from '../services/inventarioService';

const TIPO_ICONS = { ARMA: '⚔️', ARMADURA: '🛡️', POCAO: '🧪', ACESSORIO: '💍', default: '📦' };

export default function Inventario() {
  const usuarioId = localStorage.getItem('usuarioId') || 1;
  const [personagens, setPersonagens] = useState([]);
  const [personagemSel, setPersonagemSel] = useState(null);
  const [itens, setItens] = useState([]);
  const [inventario, setInventario] = useState(null);
  const [armas, setArmas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');
  const [showEquipar, setShowEquipar] = useState(false);
  const [equiparForm, setEquiparForm] = useState({ itemId: '' });

  useEffect(() => {
    buscarPorUsuario(usuarioId)
      .then((r) => setPersonagens(r.data))
      .catch(() => setErro('Erro ao carregar personagens.'));

    listarArmas()
      .then((r) => setArmas(r.data))
      .catch(() => {});
  }, []);

  useEffect(() => {
    if (!personagemSel) return;
    setLoading(true);
    setErro('');
    Promise.all([
      buscarItensPorPersonagem(personagemSel.id),
      buscarInventario(personagemSel.id).catch(() => ({ data: null })),
    ])
      .then(([itensRes, invRes]) => {
        setItens(itensRes.data);
        setInventario(invRes.data);
      })
      .catch(() => setErro('Erro ao carregar inventário.'))
      .finally(() => setLoading(false));
  }, [personagemSel]);

  const handleEquipar = async (e) => {
    e.preventDefault();
    if (!personagemSel || !equiparForm.itemId) return;
    try {
      await equiparItem({ personagemId: personagemSel.id, itemId: Number(equiparForm.itemId) });
      setShowEquipar(false);
      setEquiparForm({ itemId: '' });
      const [itensRes, invRes] = await Promise.all([
        buscarItensPorPersonagem(personagemSel.id),
        buscarInventario(personagemSel.id).catch(() => ({ data: null })),
      ]);
      setItens(itensRes.data);
      setInventario(invRes.data);
    } catch (err) {
      setErro(err.response?.data || 'Erro ao equipar item.');
    }
  };

  const handleDeletar = async (id) => {
    if (!confirm('Remover este item?')) return;
    try {
      await deletarItem(id);
      setItens((prev) => prev.filter((i) => i.id !== id));
    } catch {
      setErro('Erro ao remover item.');
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">🎒 Inventário</h1>
      </div>

      {erro && <p className="form-error">{erro}</p>}

      <div className="select-section">
        <label className="select-label">Selecione um personagem:</label>
        <div className="personagem-selector">
          {personagens.map((p) => (
            <button
              key={p.id}
              className={`btn-personagem ${personagemSel?.id === p.id ? 'active' : ''}`}
              onClick={() => setPersonagemSel(p)}
            >
              {p.nome} <span className="nivel-small">Nv.{p.nivel}</span>
            </button>
          ))}
        </div>
      </div>

      {personagemSel && (
        <>
          {inventario && (
            <div className="card inventario-equipado">
              <h2 className="card-title">🗡️ Equipamento Atual</h2>
              <div className="equipamento-grid">
                <div className="equip-slot">
                  <span className="slot-label">Arma equipada</span>
                  <span className="slot-value">
                    {inventario.armaEquipada ? `${inventario.armaEquipada.nome} (${inventario.armaEquipada.danoBase} dano)` : 'Nenhuma'}
                  </span>
                </div>
              </div>
            </div>
          )}

          <div className="inv-actions">
            <button className="btn-primary" onClick={() => setShowEquipar(!showEquipar)}>
              {showEquipar ? 'Cancelar' : '⚔️ Equipar Arma'}
            </button>
          </div>

          {showEquipar && (
            <div className="card form-card">
              <h2 className="card-title">Equipar Arma</h2>
              <form onSubmit={handleEquipar} className="auth-form">
                <div className="form-group">
                  <label>Arma disponível</label>
                  <select
                    value={equiparForm.itemId}
                    onChange={(e) => setEquiparForm({ itemId: e.target.value })}
                    required
                  >
                    <option value="">Selecione uma arma...</option>
                    {armas.map((a) => (
                      <option key={a.id} value={a.id}>
                        ⚔️ {a.nome} — {a.danoBase} dano
                      </option>
                    ))}
                  </select>
                </div>
                <button type="submit" className="btn-primary">Equipar</button>
              </form>
            </div>
          )}

          {loading ? (
            <div className="loading-state"><span className="spinner">⟳</span> Carregando itens...</div>
          ) : itens.length === 0 ? (
            <div className="empty-state">
              <p>Nenhum item no inventário.</p>
            </div>
          ) : (
            <div className="cards-grid">
              {itens.map((item) => (
                <div key={item.id} className="card item-card">
                  <div className="item-header">
                    <span className="item-icon">{TIPO_ICONS[item.tipo] || TIPO_ICONS.default}</span>
                    <div>
                      <h3 className="item-nome">{item.nome}</h3>
                      <span className="item-tipo">{item.tipo}</span>
                    </div>
                  </div>
                  {item.descricao && <p className="item-descricao">{item.descricao}</p>}
                  <button
                    className="btn-danger-sm"
                    onClick={() => handleDeletar(item.id)}
                  >
                    🗑️ Remover
                  </button>
                </div>
              ))}
            </div>
          )}
        </>
      )}

      {!personagemSel && personagens.length > 0 && (
        <div className="empty-state">
          <p>Selecione um personagem para ver o inventário.</p>
        </div>
      )}
    </div>
  );
}
