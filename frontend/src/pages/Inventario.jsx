import { useState, useEffect } from 'react';
import { buscarPorUsuario } from '../services/personagemService';
import { buscarInventario, listarArmas, equiparItem } from '../services/inventarioService';
import './Inventario.css';

export default function Inventario() {
  const usuarioId = localStorage.getItem('usuarioId') || 1;
  const [personagens, setPersonagens] = useState([]);
  const [personagemSel, setPersonagemSel] = useState(null);
  const [inventario, setInventario] = useState(null);
  const [armas, setArmas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');
  const [showEquipar, setShowEquipar] = useState(false);
  const [armaId, setArmaId] = useState('');

  useEffect(() => {
    buscarPorUsuario(usuarioId).then((r) => setPersonagens(r.data)).catch(() => {});
    listarArmas().then((r) => setArmas(r.data)).catch(() => {});
  }, []);

  useEffect(() => {
    if (!personagemSel) return;
    setLoading(true);
    buscarInventario(personagemSel.id)
      .then((r) => setInventario(r.data))
      .catch(() => setInventario(null))
      .finally(() => setLoading(false));
  }, [personagemSel]);

  const recarregar = async () => {
    if (!personagemSel) return;
    const r = await buscarInventario(personagemSel.id).catch(() => ({ data: null }));
    setInventario(r.data);
  };

  const handleEquipar = async (e) => {
    e.preventDefault();
    if (!personagemSel || !armaId) return;
    try {
      await equiparItem({ personagemId: personagemSel.id, armaId: Number(armaId) });
      setShowEquipar(false);
      setArmaId('');
      await recarregar();
    } catch (err) {
      setErro(err.response?.data || 'Erro ao equipar arma.');
    }
  };

  const armaEquipada = inventario?.armaEquipada;

  return (
    <div className="pix-main">
      <div className="pix-page-header">
        <h1 className="pix-page-title">🎒 INVENTÁRIO</h1>
      </div>

      {erro && <div className="pix-erro">{erro}</div>}

      <div style={{ marginBottom: 8 }}>
        <div className="pix-label" style={{ marginBottom: 8 }}>SELECIONE UM PERSONAGEM:</div>
        <div className="inv-selector">
          {personagens.map((p) => (
            <button key={p.id}
              className={`inv-char-btn ${personagemSel?.id === p.id ? 'active' : ''}`}
              onClick={() => setPersonagemSel(p)}>
              {p.nome}
              <span className="inv-char-nivel">NV.{p.nivel}</span>
            </button>
          ))}
        </div>
      </div>

      {personagemSel && (
        <>
          {loading ? (
            <div className="pix-loading">⟳ CARREGANDO...</div>
          ) : (
            <>
              <div className="pix-card inv-equip-card">
                <div className="pix-card-title">🗡️ EQUIPAMENTO ATUAL</div>
                <div className="inv-equip-slot">
                  <span className="inv-equip-label">ARMA EQUIPADA</span>
                  <span className={`inv-equip-value ${armaEquipada ? 'equipped' : ''}`}>
                    {armaEquipada ? `⚔️ ${armaEquipada.nome} (${armaEquipada.danoBase} DMG)` : '— Nenhuma —'}
                  </span>
                </div>
              </div>

              <div className="inv-actions">
                <button className="pix-btn" onClick={() => setShowEquipar(!showEquipar)}>
                  {showEquipar ? '✕ CANCELAR' : '⚔️ EQUIPAR ARMA'}
                </button>
              </div>

              {showEquipar && (
                <div className="pix-card" style={{ maxWidth: 420, marginBottom: 16 }}>
                  <div className="pix-card-title">EQUIPAR ARMA</div>
                  <form onSubmit={handleEquipar}>
                    <div className="pix-field">
                      <label className="pix-label">ARMA DISPONÍVEL</label>
                      <select className="pix-select" value={armaId}
                        onChange={(e) => setArmaId(e.target.value)} required>
                        <option value="">Selecione uma arma...</option>
                        {armas.map((a) => (
                          <option key={a.id} value={a.id}>
                            ⚔️ {a.nome} — {a.danoBase} DMG
                          </option>
                        ))}
                      </select>
                    </div>
                    <button className="pix-btn" type="submit">► EQUIPAR</button>
                  </form>
                </div>
              )}

              {inventario?.armas?.length > 0 && (
                <div>
                  <div className="pix-label" style={{ marginBottom: 10 }}>ARMAS NO INVENTÁRIO:</div>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                    {inventario.armas.map((arma) => (
                      <div key={arma.id} className="inv-arma-card">
                        <span className="inv-arma-icon">⚔️</span>
                        <div>
                          <span className="inv-arma-nome">{arma.nome}</span>
                          <span className="inv-arma-dano">{arma.danoBase} DMG</span>
                        </div>
                        {armaEquipada?.id === arma.id && (
                          <span className="inv-arma-equipped-badge">EQUIPADA</span>
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {!inventario?.armas?.length && !armaEquipada && (
                <div className="pix-empty">
                  <p>Nenhuma arma no inventário.</p>
                </div>
              )}
            </>
          )}
        </>
      )}

      {!personagemSel && personagens.length > 0 && (
        <div className="pix-empty">
          <p>Selecione um personagem para ver o inventário.</p>
        </div>
      )}
    </div>
  );
}