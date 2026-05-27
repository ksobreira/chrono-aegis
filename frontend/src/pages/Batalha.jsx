import { useState, useEffect, useRef } from 'react';
import { buscarPorUsuario } from '../services/personagemService';
import { iniciarBatalha, executarTurno } from '../services/combateService';

const ACOES = [
  { valor: 'ATACAR', label: '⚔️ Atacar', desc: 'Ataque físico direto' },
  { valor: 'DEFENDER', label: '🛡️ Defender', desc: 'Postura defensiva' },
  { valor: 'HABILIDADE', label: '✨ Habilidade', desc: 'Usar habilidade especial' },
  { valor: 'FUGIR', label: '💨 Fugir', desc: 'Tentar escapar do combate' },
];

export default function Batalha() {
  const usuarioId = localStorage.getItem('usuarioId') || 1;
  const [personagens, setPersonagens] = useState([]);
  const [personagemSel, setPersonagemSel] = useState(null);
  const [batalha, setBatalha] = useState(null);
  const [log, setLog] = useState([]);
  const [acaoSel, setAcaoSel] = useState('ATACAR');
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');
  const [fimBatalha, setFimBatalha] = useState(null);
  const logRef = useRef(null);

  useEffect(() => {
    buscarPorUsuario(usuarioId)
      .then((r) => setPersonagens(r.data))
      .catch(() => setErro('Erro ao carregar personagens.'));
  }, []);

  useEffect(() => {
    if (logRef.current) {
      logRef.current.scrollTop = logRef.current.scrollHeight;
    }
  }, [log]);

  const handleIniciar = async () => {
    if (!personagemSel) return;
    setLoading(true);
    setErro('');
    setLog([]);
    setFimBatalha(null);
    try {
      const res = await iniciarBatalha({ personagemId: personagemSel.id });
      setBatalha(res.data);
      addLog('⚔️ Batalha iniciada!', 'system');
      addLog(`Você enfrenta: ${res.data.inimigo?.nome || 'Inimigo desconhecido'}`, 'system');
    } catch (err) {
      setErro(err.response?.data || 'Erro ao iniciar batalha.');
    } finally {
      setLoading(false);
    }
  };

  const handleTurno = async () => {
    if (!batalha) return;
    setLoading(true);
    try {
      const res = await executarTurno(batalha.id, { acao: acaoSel });
      const resultado = res.data;

      if (resultado.descricao) addLog(resultado.descricao, 'action');
      if (resultado.danoHeroi != null) addLog(`💥 Você recebeu ${resultado.danoHeroi} de dano`, 'damage');
      if (resultado.danoInimigo != null) addLog(`⚔️ Você causou ${resultado.danoInimigo} de dano`, 'hit');
      if (resultado.xpGanho) addLog(`✨ +${resultado.xpGanho} XP ganho!`, 'xp');

      if (resultado.fim) {
        const vitoria = resultado.vencedor === 'HEROI';
        setFimBatalha(vitoria ? 'vitoria' : 'derrota');
        addLog(vitoria ? '🏆 Vitória!' : '💀 Derrota...', vitoria ? 'vitoria' : 'derrota');
        setBatalha(null);
      } else {
        setBatalha(resultado.batalhaAtualizada || batalha);
      }
    } catch (err) {
      setErro(err.response?.data || 'Erro ao executar turno.');
    } finally {
      setLoading(false);
    }
  };

  const addLog = (msg, tipo = 'info') => {
    setLog((prev) => [...prev, { msg, tipo, id: Date.now() + Math.random() }]);
  };

  const resetar = () => {
    setBatalha(null);
    setLog([]);
    setFimBatalha(null);
    setErro('');
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">🔥 Batalha</h1>
      </div>

      {erro && <p className="form-error">{erro}</p>}

      {!batalha && !fimBatalha && (
        <div className="card batalha-setup">
          <h2 className="card-title">Preparar para o combate</h2>

          <div className="select-section">
            <label className="select-label">Escolha seu guerreiro:</label>
            <div className="personagem-selector">
              {personagens.map((p) => (
                <button
                  key={p.id}
                  className={`btn-personagem ${personagemSel?.id === p.id ? 'active' : ''}`}
                  onClick={() => setPersonagemSel(p)}
                >
                  {p.nome}
                  <span className="nivel-small">Nv.{p.nivel} | ❤️{p.vidaMax}</span>
                </button>
              ))}
            </div>
          </div>

          <button
            className="btn-batalha"
            onClick={handleIniciar}
            disabled={!personagemSel || loading}
          >
            {loading ? 'Iniciando...' : '⚔️ Entrar em Batalha'}
          </button>
        </div>
      )}

      {batalha && (
        <div className="batalha-arena">
          <div className="arena-combatentes">
            <div className="combatente heroi">
              <div className="combatente-nome">{personagemSel?.nome}</div>
              <div className="combatente-icon">🧙</div>
              <div className="vida-bar">
                <div
                  className="vida-fill heroi-vida"
                  style={{ width: `${Math.max(0, (batalha.vidaHeroi / personagemSel?.vidaMax) * 100)}%` }}
                />
              </div>
              <div className="vida-num">❤️ {batalha.vidaHeroi}</div>
            </div>

            <div className="vs-badge">VS</div>

            <div className="combatente inimigo">
              <div className="combatente-nome">{batalha.inimigo?.nome || 'Inimigo'}</div>
              <div className="combatente-icon">👹</div>
              <div className="vida-bar">
                <div
                  className="vida-fill inimigo-vida"
                  style={{ width: `${Math.max(0, (batalha.vidaInimigo / (batalha.inimigo?.vidaMax || 100)) * 100)}%` }}
                />
              </div>
              <div className="vida-num">❤️ {batalha.vidaInimigo}</div>
            </div>
          </div>

          <div className="batalha-log" ref={logRef}>
            {log.map((entry) => (
              <p key={entry.id} className={`log-entry log-${entry.tipo}`}>{entry.msg}</p>
            ))}
          </div>

          <div className="acoes-section">
            <p className="acoes-label">Escolha sua ação:</p>
            <div className="acoes-grid">
              {ACOES.map(({ valor, label, desc }) => (
                <button
                  key={valor}
                  className={`btn-acao ${acaoSel === valor ? 'selected' : ''}`}
                  onClick={() => setAcaoSel(valor)}
                >
                  <span className="acao-label">{label}</span>
                  <span className="acao-desc">{desc}</span>
                </button>
              ))}
            </div>
            <button
              className="btn-batalha"
              onClick={handleTurno}
              disabled={loading}
            >
              {loading ? 'Executando...' : '▶ Executar Turno'}
            </button>
          </div>
        </div>
      )}

      {fimBatalha && (
        <div className={`batalha-fim ${fimBatalha}`}>
          <div className="fim-icon">{fimBatalha === 'vitoria' ? '🏆' : '💀'}</div>
          <h2 className="fim-titulo">
            {fimBatalha === 'vitoria' ? 'Vitória!' : 'Derrota...'}
          </h2>
          <div className="batalha-log-final" ref={logRef}>
            {log.map((entry) => (
              <p key={entry.id} className={`log-entry log-${entry.tipo}`}>{entry.msg}</p>
            ))}
          </div>
          <button className="btn-batalha" onClick={resetar}>
            ↩ Nova Batalha
          </button>
        </div>
      )}
    </div>
  );
}
